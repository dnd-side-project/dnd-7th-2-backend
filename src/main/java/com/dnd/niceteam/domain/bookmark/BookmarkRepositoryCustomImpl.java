package com.dnd.niceteam.domain.bookmark;

import com.dnd.niceteam.domain.bookmark.dto.LectureBookmarkDto;
import com.dnd.niceteam.domain.bookmark.dto.LectureTimeDto;
import com.dnd.niceteam.domain.bookmark.dto.QLectureBookmarkDto;
import com.dnd.niceteam.domain.bookmark.dto.QLectureTimeDto;
import com.dnd.niceteam.domain.member.Member;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dnd.niceteam.domain.account.QAccount.account;
import static com.dnd.niceteam.domain.bookmark.QBookmark.bookmark;
import static com.dnd.niceteam.domain.member.QMember.member;
import static com.dnd.niceteam.domain.project.QLectureProject.lectureProject;
import static com.dnd.niceteam.domain.project.QLectureTime.lectureTime;
import static com.dnd.niceteam.domain.project.QProject.project;
import static com.dnd.niceteam.domain.recruiting.QRecruiting.recruiting;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public boolean existsByIdAndEmail(long bookmarkId, String email) {
        return Optional.ofNullable(query
                        .selectOne()
                        .from(bookmark)
                        .join(bookmark.member, member)
                        .join(member.account, account)
                        .where(bookmark.id.eq(bookmarkId), account.email.eq(email))
                        .fetchFirst())
                .isPresent();
    }

    @Override
    public Page<LectureBookmarkDto> findLectureBookmarkDtoPageByMember(Pageable pageable, Member member) {
        List<LectureBookmarkDto> content = query
                .select(new QLectureBookmarkDto(
                        bookmark.id,
                        recruiting.id,
                        project.id,
                        recruiting.title,
                        recruiting.recruitingEndDate,
                        recruiting.commentCount,
                        recruiting.bookmarkCount,
                        recruiting.recruitingMemberCount,
                        project.name))
                .from(bookmark)
                .join(bookmark.member)
                .join(bookmark.recruiting, recruiting)
                .join(bookmark.recruiting.project, project)
                .join(lectureProject).on(lectureProject.eq(project))
                .where(bookmark.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Long, List<LectureTimeDto>> projectIdToLectureTimeDtosMap =
                findProjectIdToLectureTimeDtosMap(content);

        content.forEach(dto -> dto.setLectureTimes(projectIdToLectureTimeDtosMap.get(dto.getProjectId())));

        JPAQuery<Long> countQuery = countQueryByMember(member);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private Map<Long, List<LectureTimeDto>> findProjectIdToLectureTimeDtosMap(List<LectureBookmarkDto> content) {
        List<Long> projectIds = content.stream()
                .map(LectureBookmarkDto::getProjectId)
                .collect(Collectors.toList());
        List<LectureTimeDto> lectureTimeDtos = query
                .select(new QLectureTimeDto(
                        project.id,
                        lectureTime.dayOfWeek,
                        lectureTime.startTime))
                .from(project)
                .join(lectureProject).on(lectureProject.eq(project))
                .leftJoin(lectureProject.lectureTimes, lectureTime)
                .where(project.id.in(projectIds))
                .fetch();
        Map<Long, List<LectureTimeDto>> projectIdToLectureTimeDtosMap = lectureTimeDtos.stream()
                .collect(Collectors.groupingBy(LectureTimeDto::getProjectId));

        projectIdToLectureTimeDtosMap.keySet().forEach(id -> {
            if (projectIdToLectureTimeDtosMap.get(id).size() == 1) {
                LectureTimeDto lectureTimeDto = projectIdToLectureTimeDtosMap.get(id).get(0);
                if (isNull(lectureTimeDto.getDayOfWeek()) && isNull(lectureTimeDto.getStartTime())) {
                    projectIdToLectureTimeDtosMap.put(id, Collections.emptyList());
                }
            }
        });
        return projectIdToLectureTimeDtosMap;
    }

    private JPAQuery<Long> countQueryByMember(Member member) {
        return query
                .select(bookmark.count())
                .from(bookmark)
                .join(bookmark.member)
                .where(bookmark.member.eq(member));
    }
}
