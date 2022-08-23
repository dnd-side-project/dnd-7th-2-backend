package com.dnd.niceteam.recruiting.service;

import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.common.util.PaginationUtil;
import com.dnd.niceteam.domain.bookmark.BookmarkRepository;
import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.ProgressStatus;
import com.dnd.niceteam.domain.code.Type;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitingService {
    private final RecruitingRepository recruitingRepository;
    private final MemberRepository memberRepository;
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public RecruitingCreation.ResponseDto addProjectAndRecruiting(String username, RecruitingCreation.RequestDto recruitingReqDto) {
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);
        // 프로젝트
        ProjectRequest.Register projectRequestDto = setProjecRegistertRequestDto(recruitingReqDto);
        ProjectResponse.Detail detail = projectService.registerProject(projectRequestDto, member);
        Project project = projectRepository.getReferenceById(detail.getId());

        // 모집글
        Recruiting createdRecruiting = recruitingRepository.save(recruitingReqDto.toEntity(project, member));

        return RecruitingCreation.ResponseDto.from(createdRecruiting);
    }

    // 모집글 상세
    public RecruitingFind.DetailResponseDto getRecruiting(Long recruitingId, String username) {
        Recruiting recruiting = recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
        // TODO: 2022-08-20 북마크 체크 테스트 추가 필요
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);
        boolean isBookmarked = bookmarkRepository.existsByMemberAndRecruiting(member, recruiting);

        return RecruitingFind.DetailResponseDto.from(recruiting, isBookmarked);
    }

    // 내가 쓴글
    public Pagination<RecruitingFind.ListResponseDto> getMyRecruitings(int page, int perSize, ProgressStatus progressStatus, String username) {    // 현재 페이지, 페이지 데이터 수
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);

        Pageable pageable = PageRequest.of(page - 1, perSize);

        Page<RecruitingFind.ListResponseDto> recruitingPages = findRecruitingsDependsOnStatus(pageable, progressStatus, member)
                .map(RecruitingFind.ListResponseDto::fromMyList);

        return PaginationUtil.pageToPagination(recruitingPages);
    }

    // 추천 사이드 모집글 목록 조회
    public Pagination<RecruitingFind.RecommendedListResponseDto> getRecommendedRecruitings(int page, int perSize, String username) {
        Member member = MemberUtils.findMemberByEmail(username, memberRepository);

        // Field로 필터링 & 해당 Recruiting의 모집자 레벨 순 정렬
        Pageable pageable = PageRequest.of(page - 1, perSize);
        Page<RecruitingFind.RecommendedListResponseDto> recommendedRecruitingPages = recruitingRepository.findAllByInterestingFieldsOrderByWriterLevel(member.getInterestingFields(), pageable)
                .map(RecruitingFind.RecommendedListResponseDto::fromRecommendedList);

        return PaginationUtil.pageToPagination(recommendedRecruitingPages);
    }

    // 검색 목록 조회
    public Pagination<RecruitingFind.ListResponseDto> getSearchRecruitings (int page, int perSize, Field field, Type type, String searchWord, String email) {
        Member member = MemberUtils.findMemberByEmail(email, memberRepository);

        Page<Recruiting> recruitingPages;
        Pageable pageable = PageRequest.of(page - 1, perSize);

        switch(type) {
            case SIDE:
                recruitingPages = recruitingRepository.findAllSideBySearchWordAndFieldOrderByCreatedDate(searchWord, field, pageable);
                break;
            case LECTURE:
                recruitingPages = recruitingRepository.findAllLectureBySearchWordAndDepartmentOrderByCreatedDate(searchWord, member.getDepartment().getName(), pageable);
                break;
            default: throw new InvalidRecruitingTypeException(type.name());
        }

        Page<RecruitingFind.ListResponseDto> searchRecruitingPages = recruitingPages.map(RecruitingFind.ListResponseDto::fromSearchList);
        return PaginationUtil.pageToPagination(searchRecruitingPages);
    }

    private ProjectRequest.Register setProjecRegistertRequestDto(RecruitingCreation.RequestDto recruitingReqDto) {
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

    private Page<Recruiting> findRecruitingsDependsOnStatus(Pageable pageable, ProgressStatus progressStatus, Member member) {
        if (isNull(progressStatus))
            return recruitingRepository.findPageByMemberOrderByCreatedDateDesc(pageable, member);
        else {
            return recruitingRepository.findPageByMemberAndStatusOrderByCreatedDateDesc(pageable, member, progressStatus);
        }
    }
}
