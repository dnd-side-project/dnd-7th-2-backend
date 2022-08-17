package com.dnd.niceteam.recruiting.service;

import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.code.ProgressStatus;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.project.*;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.exception.InvalidRecruitingTypeException;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import com.dnd.niceteam.member.util.MemberUtils;
import com.dnd.niceteam.project.dto.ProjectRequest;
import com.dnd.niceteam.project.dto.ProjectResponse;
import com.dnd.niceteam.project.service.ProjectService;
import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import com.dnd.niceteam.recruiting.dto.RecruitingFind;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;

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

    // 모집글 상세
    public RecruitingFind.DetailResponseDto getRecruiting(Long recruitingId) {
        Recruiting recruiting = recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));

        return RecruitingFind.DetailResponseDto.from(recruiting);
    }

    // 내가 쓴글
    public Pagination<RecruitingFind.ListResponseDto> getMyRecruitings(int page, int perSize, ProgressStatus progressStatus, String username) {    // 현재 페이지, 페이지 데이터 수
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);

        Pageable pageable = PageRequest.of(page - 1, perSize);
        PageImpl<Recruiting> recruitingPages = findRecruitingsDependsOnStatus(pageable, progressStatus, member.getId());
        List<RecruitingFind.ListResponseDto> pageContents = recruitingPages
                .stream().map(RecruitingFind.ListResponseDto::from).collect(Collectors.toList());

        pageable = recruitingPages.getPageable();

        return Pagination.<RecruitingFind.ListResponseDto>builder()
                .page(pageable.getPageNumber() + 1)
                .perSize(pageable.getPageSize())
                .totalCount(recruitingPages.getTotalElements())
                .contents(pageContents)
                .build();
    }

    // 추천 사이드 모집글 조회
    public Pagination<RecruitingFind.RecommendedListResponseDto> getRecommendedRecruiting(int page, int perSize, String username) {
        // 1. 사용자 조회
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);

        // 2. Field로 필터링 & 해당 Recruiting의 모집자 레벨 순 정렬
        Pageable pageable = PageRequest.of(page - 1, perSize);
        PageImpl<Recruiting> recommendedRecruitings = recruitingRepository.findAllByInterestingFieldsOrderByWriterLevel(member.getInterestingFields(), pageable);
        List<RecruitingFind.RecommendedListResponseDto> pageContents = recommendedRecruitings
                .stream().map(RecruitingFind.RecommendedListResponseDto::fromRecommendedRecruiting).collect(Collectors.toList());

        pageable = recommendedRecruitings.getPageable();

        return Pagination.<RecruitingFind.RecommendedListResponseDto>builder()
                .page(pageable.getPageNumber() + 1)
                .perSize(pageable.getPageSize())
                .totalCount(recommendedRecruitings.getTotalElements())
                .contents(pageContents)
                .build();    }

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

    private PageImpl<Recruiting> findRecruitingsDependsOnStatus(Pageable pageable, ProgressStatus progressStatus, Long memberId) {
        if (isNull(progressStatus))
            return recruitingRepository.findAllByMemberId(memberId, pageable);
        else {
            return recruitingRepository.findAllByMemberIdAndStatus(memberId, progressStatus, pageable);
        }
    }

}
