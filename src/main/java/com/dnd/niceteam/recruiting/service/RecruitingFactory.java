package com.dnd.niceteam.recruiting;

import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.exception.InvalidRecruitingType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecruitingFactory {
    private final SideRecruitingService sideRecruitingService;
    private final LectureRecruitingService lectureRecruitingService;

    public RecruitingServiceStrategy getSubRecruitingService(Type recruitingType){
        final RecruitingServiceStrategy recruitingServiceStrategy;
        switch (recruitingType) {
            case SIDE:
                recruitingServiceStrategy = sideRecruitingService;
                break;
            case LECTURE:
                recruitingServiceStrategy = lectureRecruitingService;
                break;
            default:
                throw new InvalidRecruitingType("recruiting type = " + recruitingType);
        }
        return recruitingServiceStrategy;
    }

    /*@Transactional
    public RecruitingCreation.ResponseDto addRecruiting(RecruitingCreation.RequestDto reqDto) {
        Member member = memberRepository.findById(reqDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("memberId = " + reqDto.getMemberId()));
        Project project = projectRepository.findById(reqDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("pojectId = " + reqDto.getProjectId()));

        // TODO 프로젝트 TB에 활동 시작, 종료일 update
//        projectRepository

        RecruitingService recruitingService = setSubRecruitingService(reqDto.getRecruitingType());

        return recruitingService.saveRecruitingTypeInfo(member, project, reqDto);
    }

    protected RecruitingService setSubRecruitingService(Type recruitingType){
        switch (recruitingType) {
            case SIDE: return SideRecruitingService.of();
            case LECTURE: return LectureRecruitingService.of();
        }
        throw new InvalidRecruitingType("recruiting type = " + recruitingType);
    }

    protected abstract RecruitingCreation.ResponseDto saveRecruitingTypeInfo(Member member, Project project, RecruitingCreation.RequestDto recruitingReqDto);
*/
}
