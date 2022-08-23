package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.project.Project;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

import static com.dnd.niceteam.domain.project.QLectureProject.lectureProject;
import static com.dnd.niceteam.domain.project.QProject.project;
import static com.dnd.niceteam.domain.project.QSideProject.sideProject;
import static com.dnd.niceteam.domain.recruiting.QRecruiting.recruiting;

@RequiredArgsConstructor
public class RecruitingRepositoryCustomImpl implements RecruitingRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public PageImpl<Recruiting> findAllByInterestingFieldsOrderByWriterLevel(Set<Field> interestingFields, Pageable pageable) {
        List<Recruiting> recruitings = query
                .select(recruiting)
                .from(recruiting)
                .join(recruiting.project, project)
                .where(project.in(
                        query.selectFrom(project)
                                .leftJoin(sideProject).on(sideProject.eq(project))
                                .where(sideProject.field.in(interestingFields))
                                .fetch()
                        )
                )
                .orderBy(recruiting.member.memberScore.level.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = query.select(recruiting.count())
                .from(recruiting)
                .join(recruiting.project, project)
                .where(project.in(
                                query.selectFrom(project)
                                        .leftJoin(sideProject).on(sideProject.eq(project))
                                        .where(sideProject.field.in(interestingFields))
                                        .fetch()
                        )
                )
                .fetchOne();

        return new PageImpl<>(recruitings, pageable, totalCount);
    }
    // SIDE
    // 검색어 o -> 해당 단어를 포함하는 프로젝트명과 모집글 제목 필터링
    // 분야 o -> 분야 필터링
    @Override
    public Page<Recruiting> findAllSideBySearchWordAndFieldOrderByCreatedDate(String searchWord, Field field, Pageable pageable) {
        List<Recruiting> recruitings = query
                .select(recruiting)
                .from(recruiting)
                .join(recruiting.project, project)
                .where(project
                        .in(findSideProjectsBySearchWordAndField(searchWord, field))
                        .and(project.type.eq(Type.SIDE))
                        .or(nameContains(recruiting.title, searchWord))
                )
                .orderBy(recruiting.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = query
                .select(recruiting.count())
                .from(recruiting)
                .join(recruiting.project, project)
                .where(project
                        .in(findSideProjectsBySearchWordAndField(searchWord, field))
                        .and(project.type.eq(Type.SIDE))
                        .or(nameContains(recruiting.title, searchWord)) //프로젝트 내에서는 searchWord랑 필드가 같은 데이터 혹은 리크루팅 내에서는 리크루팅 제목에 포함되는 데이터
                )
                .fetchOne();
        return new PageImpl<>(recruitings, pageable, totalCount);
    }

    // LECTURE
    // 검색어 x -> 회원 전공 필터링
    // 검색어 o -> 해당 단어를 포함하는 강의명 or 교수명 or 전공명 or 모집글 제목 필터링 & 회원 전공 필터링 X
    @Override
    public Page<Recruiting> findAllLectureBySearchWordAndDepartmentOrderByCreatedDate (String searchWord, String memberDepartment, Pageable pageable) {
        List<Recruiting> recruitings = query
                .select(recruiting)
                .from(recruiting)
                .join(recruiting.project, project)
                .where(project
                        .in(findLectureProjectsBySearchWordAndDepartment(searchWord, memberDepartment))
                        .and(project.type.eq(Type.LECTURE))
                        .or(nameContains(recruiting.title, searchWord))
                )
                .orderBy(recruiting.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = query
                .select(recruiting.count())
                .from(recruiting)
                .join(recruiting.project, project)
                .where(project
                        .in(findLectureProjectsBySearchWordAndDepartment(searchWord, memberDepartment))
                        .and(project.type.eq(Type.LECTURE))
                        .or(nameContains(recruiting.title, searchWord))
                )
                .fetchOne();

        return new PageImpl<>(recruitings, pageable, totalCount);
    }

    private List<Project> findLectureProjectsBySearchWordAndDepartment(String searchWord, String department) {
        return query.selectFrom(project)
                .leftJoin(lectureProject).on(lectureProject.eq(project))
                .where(nameContains(lectureProject.name, searchWord)
                        .or(nameContains(lectureProject.professor, searchWord))
                        .or(nameContains(lectureProject.name, searchWord))
                        .or(nameContains(lectureProject.department.name, searchWord))
                        .and(departmentEqWhenSearchWordisNull(lectureProject.department.name, department, searchWord))
                )
                .fetch();
    }

    private List<Project> findSideProjectsBySearchWordAndField(String searchWord, Field field) {
        return query.selectFrom(project)
                .leftJoin(sideProject).on(sideProject.eq(project))
                .where(
                        nameContains(sideProject.name, searchWord)
                                .and(fieldEq(field))    // searchWord가 null이면 무시하고, 필드가 같아야 한다.)
                )
                .fetch();
    }


    private BooleanBuilder departmentEqWhenSearchWordisNull(StringPath name, String memberDepartment, String searchWord) {
        return searchWord == null ? new BooleanBuilder(name.eq(memberDepartment)) : new BooleanBuilder();
    }

    private BooleanBuilder nameContains(StringPath name, String searchWord) {
        return searchWord != null ? new BooleanBuilder(name.contains(searchWord)) : new BooleanBuilder();
    }

    private BooleanBuilder fieldEq(Field filteredField) {
        return filteredField != null ? new BooleanBuilder(sideProject.field.eq(filteredField)) : new BooleanBuilder();
    }
}