package com.dnd.niceteam.project.service;

import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.department.Department;
import com.dnd.niceteam.domain.department.DepartmentRepository;
import com.dnd.niceteam.domain.department.exception.DepartmentNotFoundException;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.member.util.MemberUtils;
import com.dnd.niceteam.project.dto.LectureTimeRequest;
import com.dnd.niceteam.project.dto.ProjectRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.exception.InvalidProjectSchedule;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
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

    private final ProjectRepository projectRepository;
    private final LectureProjectRepository lectureSideProjectRepository;
    private final SideProjectRepository sideProjectRepository;
    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    
    // TODO: 기획 논의 후 startDate, endDate에 @Past, @Future 등의 유효성검사 어노테이션 붙이기
    @Transactional
    public ProjectResponse.Detail registerProject(ProjectRequest.Register request) {
        LocalDate startDate =           request.getStartDate();
        LocalDate endDate =             request.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new InvalidProjectSchedule("startDate = " + startDate + ", endDate = " + endDate);
        }

        return saveProject(request);
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
        return ProjectResponse.Detail.from(project);
    }

    /* private 메서드 */
    // 프로젝트 등록 관련
    private ProjectResponse.Detail saveProject(ProjectRequest.Register request) {
        if (request.getType() == Type.LECTURE) {
            return ProjectResponse.Detail.from(saveLectureProject(request));
        } else {
            return ProjectResponse.Detail.from(saveSideProject(request));
        }
    }

    // 프로젝트 수정 관련
    private void modifyProject(Project project, ProjectRequest.Update request) {
        project.setName(request.getName());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        if (project.getType() == Type.LECTURE)      modifyLectureProject((LectureProject) project, request);
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

    /* JPA 메서드 */
    private LectureProject saveLectureProject(ProjectRequest.Register request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("id = " + request.getDepartmentId()));

        LectureProject lectureProject = request.toLectureProject(department);
        return lectureSideProjectRepository.save(lectureProject);
    }

    private SideProject saveSideProject(ProjectRequest.Register request) {
        SideProject sideProject = request.toSideProject();
        return sideProjectRepository.save(sideProject);
    }

    // DB : 팀플 조회
    private Project findProject(Long projectId, Long memberId) {
        return findOptionalProject(projectId, memberId)
                .orElseThrow(() -> new ProjectNotFoundException("id = " + projectId));
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

}
