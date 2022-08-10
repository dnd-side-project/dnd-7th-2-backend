package com.dnd.niceteam.recruiting;

import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectRepository;
import com.dnd.niceteam.domain.recruiting.LectureRecruiting;
import com.dnd.niceteam.domain.recruiting.LectureRecruitingRepository;
import com.dnd.niceteam.project.exception.ProjectNotFoundException;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@AllArgsConstructor
public class LectureRecruitingService implements RecruitingServiceStrategy {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final LectureRecruitingRepository lectureRecruitingRepo;

    @Override
    public RecruitingCreation.ResponseDto saveProjectAndRecruiting(RecruitingCreation.RequestDto reqDto) {
        Member member = memberRepository.findById(reqDto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("memberId = " + reqDto.getMemberId()));
        Project project = projectRepository.findById(reqDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("pojectId = " + reqDto.getProjectId()));

        project.update(reqDto.getProjectStartDate(), reqDto.getProjectEndDate());
        projectRepository.save(project);

        LectureRecruiting save = lectureRecruitingRepo.save(LectureRecruiting.builder()
                .member(member)
                .project(project)
                .title(reqDto.getTitle())
                .content(reqDto.getContent())
                .recruitingMemberCount(reqDto.getRecruitingMemberCount())
                .recruitingType(reqDto.getRecruitingType())
                .activityArea(reqDto.getActivityArea())
                .status(reqDto.getStatus())
                .commentCount(0)
                .bookmarkCount(0)
                .poolUpCount(0)
                .introLink(reqDto.getIntroLink())
                .lectureName(reqDto.getLectureName())
                .professor(reqDto.getProfessor())
                .department(reqDto.getDepartment())
                .build()
        );

        RecruitingCreation.ResponseDto response = new RecruitingCreation.ResponseDto();
        response.setId(save.getId());
        return response;
    }
}
