package com.dnd.niceteam.recruiting.service;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.exception.InvalidRecruitingTypeException;
import com.dnd.niceteam.member.util.MemberUtils;
import com.dnd.niceteam.project.dto.ProjectRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.service.ProjectService;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitingService {
    private final RecruitingRepository recruitingRepository;
    private final MemberRepository memberRepository;
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

    @Transactional
    public RecruitingCreation.ResponseDto addProjectAndRecruiting(String username, RecruitingCreation.RequestDto recruitingReqDto) {
        // 프로젝트
        ProjectRequest.Register projectRequestDto = setProjectRequestDto(recruitingReqDto);
        ProjectResponse.Detail detail = projectService.registerProject(projectRequestDto);
        Project project = projectRepository.getReferenceById(detail.getId());

        Member member = MemberUtils.findMemberByEmail(username, memberRepository);
        // 모집글
        Recruiting createdRecruiting = recruitingRepository.save(recruitingReqDto.toEntity(project, member));

        return RecruitingCreation.ResponseDto.from(createdRecruiting);
    }

    private ProjectRequest.Register setProjectRequestDto(RecruitingCreation.RequestDto recruitingReqDto) {
        ProjectRequest.Register projectDto = new ProjectRequest.Register();
        projectDto.setType(recruitingReqDto.getRecruitingType());
        projectDto.setStartDate(recruitingReqDto.getProjectStartDate());
        projectDto.setEndDate(recruitingReqDto.getProjectEndDate());
        projectDto.setName(recruitingReqDto.getProjectName());

        switch (recruitingReqDto.getRecruitingType()) {
            case SIDE:
                projectDto.setField(recruitingReqDto.getField());
                projectDto.setFieldCategory(recruitingReqDto.getFieldCategory());
                break;
            case LECTURE:
                projectDto.setProfessor(recruitingReqDto.getProfessor());
                projectDto.setDepartmentId(recruitingReqDto.getDepartmentId());
                projectDto.setLectureTimes(recruitingReqDto.getLectureTimes());
                break;
            default:
                throw new InvalidRecruitingTypeException("Unexpected Type: " + recruitingReqDto.getRecruitingType());
        }
        return projectDto;
    }
}
