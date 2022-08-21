package com.dnd.niceteam.project.service;

import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.department.exception.DepartmentNotFoundException;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.recruiting.Applicant;
import com.dnd.niceteam.domain.recruiting.ApplicantRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.exception.ApplicantNotFoundException;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import com.dnd.niceteam.member.util.MemberUtils;
import com.dnd.niceteam.project.dto.*;
import com.dnd.niceteam.project.exception.InvalidProjectSchedule;
import com.dnd.niceteam.project.exception.ProjectMemberAlreadyJoinedException;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.recruiting.exception.OnlyRecruiterAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectMemberService projectMemberService;

    private final ProjectRepository projectRepository;
    private final LectureProjectRepository lectureProjectRepository;
    private final SideProjectRepository sideProjectRepository;
    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;

    private final RecruitingRepository recruitingRepository;
    private final ApplicantRepository applicantRepository;
    private final ProjectMemberRepository projectMemberRepository;
    
    // TODO: 기획 논의 후 startDate, endDate에 @Past, @Future 등의 유효성검사 어노테이션 붙이기
    @Transactional
    public ProjectResponse.Detail registerProject(ProjectRequest.Register request, Member currentMamber) {
        LocalDate startDate =           request.getStartDate();
        LocalDate endDate =             request.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new InvalidProjectSchedule("startDate = " + startDate + ", endDate = " + endDate);
        }

        return saveProject(request, currentMamber);
    }

    @Transactional
    public void modifyProject(Long projectId, Long memberId, ProjectRequest.Update request) {
        Project project =               findProject(projectId, memberId);
        modifyProject(project, request);
    }

    public Pagination<ProjectResponse.ListItem> getProjectList(int page, int perSize, ProjectStatus status, User currentUser) {
        Member currentMember =          MemberUtils.findMemberByEmail(currentUser.getUsername(), memberRepository);
        Page<Project> projectPages =    findProjectPages(currentMember.getId(), status, page, perSize);
        return mapProjectPagination(projectPages);
    }

    public ProjectResponse.Detail getProject(Long projectId, User currentUser) {
        Member currentMember =          MemberUtils.findMemberByEmail(currentUser.getUsername(), memberRepository);

        Project project = findProject(projectId, currentMember.getId());

        List<ProjectMemberResponse.Summary> memberList = findProjectMemberByReviewer(project, currentMember);
        return ProjectResponse.Detail.from(project, memberList);
    }

    @Transactional
    public void addProjectMember(ProjectMemberRequest.Add request, User user) {
        Member currentMember =          MemberUtils.findMemberByEmail(user.getUsername(), memberRepository);
        Recruiting recruiting =         findRecruiting(request.getRecruitingId());
        Project project =               recruiting.getProject();

        Long currentMemberId =          currentMember.getId();
        Long recruitingId =             recruiting.getId();
        Long applicantMemberId =        request.getApplicantMemberId();
        Long projectId =                project.getId();

        Applicant applicant =           findApplicant(applicantMemberId, recruitingId);

        if (!recruiting.checkRecruiter(currentMember))      throw new OnlyRecruiterAvailableException(currentMemberId, recruitingId);
        if (isApplicantJoined(project, applicant))   throw new ProjectMemberAlreadyJoinedException(projectId, applicantMemberId);

        ProjectMember projectMember = saveProjectMember(project, applicant.getMember());
        applicant.join(project, projectMember);
    }

    /* private 메서드 */
    // 프로젝트 등록 관련
    private ProjectResponse.Detail saveProject(ProjectRequest.Register request, Member currentMember) {
        Project project;

        if (request.getType() == Type.LECTURE)      project = saveLectureProject(request);
        else                                        project = saveSideProject(request);

        ProjectMember recruiter = saveProjectMember(project, currentMember);
        project.addMember(recruiter);
        return ProjectResponse.Detail.from(project);
    }

    // 프로젝트 수정 관련
    private void modifyProject(Project project, ProjectRequest.Update request) {
        project.setName(request.getName());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        if (project instanceof LectureProject)      modifyLectureProject((LectureProject) project, request);
        else                                        modifySideProject((SideProject) project, request);
    }

    private void modifyLectureProject(LectureProject lectureProject, ProjectRequest.Update request) {
        Department newDepartment =                  departmentRepository.getReferenceById(request.getDepartmentId());
        Set<LectureTime> lectureTimeSet =           request.getLectureTimes().stream()
                                                        .map(LectureTimeRequest::toEntity).collect(Collectors.toSet());

        lectureProject.setDepartment(newDepartment);
        lectureProject.setProfessor(request.getProfessor());
        lectureProject.setLectureTimes(lectureTimeSet);
    }

    private void modifySideProject(SideProject sideProject, ProjectRequest.Update request) {
        sideProject.setField(request.getField());
        sideProject.setFieldCategory(request.getFieldCategory());
    }

    // 프로젝트 상세 조회 관련
    private List<ProjectMemberResponse.Summary> findProjectMemberByReviewer(Project project, Member currentMember) {
        return project.getProjectMembers().stream()
                .map(projectMember -> projectMemberService.getProjectMemberByReviewer(
                        project.getId(),
                        projectMember.getMember().getId(),
                        currentMember.getId())
                ).collect(Collectors.toList());
    }

    // 프로젝트 목록 조회 관련
    private Specification<Project> getProjectListSearchSpecs(Long memberId, ProjectStatus status) {
        Specification<Project> spec =       ProjectSpecification.equalMemberId(memberId);
        if (status != null) spec = spec.and(ProjectSpecification.equalStatus(status));

        return spec;
    }

    private Pagination<ProjectResponse.ListItem> mapProjectPagination(Page<Project> projectPages) {
        Pageable pageable = projectPages.getPageable();

        List<ProjectResponse.ListItem> projectList = projectPages
                .stream().map(ProjectResponse.ListItem::of).collect(Collectors.toList());

        return Pagination.<ProjectResponse.ListItem>builder()
                .page(pageable.getPageNumber() + 1)
                .perSize(pageable.getPageSize())
                .totalCount(projectPages.getTotalElements())
                .contents(projectList)
                .build();
    }

    // 프로젝트 멤버 등록 관련
    private boolean isApplicantJoined(Project project, Applicant applicant) {
        return project.hasMember(applicant.getMember());
    }

    /* JPA 메서드 */
    private LectureProject saveLectureProject(ProjectRequest.Register request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("id = " + request.getDepartmentId()));

        LectureProject lectureProject = request.toLectureProject(department);
        return lectureProjectRepository.save(lectureProject);
    }

    private SideProject saveSideProject(ProjectRequest.Register request) {
        SideProject sideProject = request.toSideProject();
        return sideProjectRepository.save(sideProject);
    }

    private ProjectMember saveProjectMember(Project project, Member member) {
        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .member(member)
                .build();
        return projectMemberRepository.save(projectMember);
    }

    // DB : 팀플 조회
    private Project findProject(Long projectId, Long memberId) {
        return findOptionalProject(projectId, memberId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    private Optional<Project> findOptionalProject(Long projectId, Long memberId) {
        if (memberId != null) {
            Specification<Project> spec = ProjectSpecification.equalProjectId(projectId)
                                     .and(ProjectSpecification.equalMemberId(memberId));
            return projectRepository.findOne(spec);
        } else {
            return projectRepository.findById(projectId);
        }
    }

    private Page<Project> findProjectPages(Long memberId, ProjectStatus status, int page, int perSize) {
        Specification<Project> spec =   getProjectListSearchSpecs(memberId, status);
        Pageable pageable =             PageRequest.of(
                                            page - 1, perSize,
                                            Sort.by(Sort.Direction.DESC, "createdDate"));

        return projectRepository.findAll(spec, pageable);
    }

    private Recruiting findRecruiting(Long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }

    private Applicant findApplicant(Long applicantMemberId, Long recruitingId) {
        return applicantRepository.findByMemberIdAndRecruitingId(applicantMemberId, recruitingId)
                .orElseThrow(() -> new ApplicantNotFoundException("memberId = " + applicantMemberId));
    }
}
