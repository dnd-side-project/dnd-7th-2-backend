package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.dnd.niceteam.domain.project.QProject.*;
import static com.dnd.niceteam.domain.project.QProjectMember.*;

@RequiredArgsConstructor
public class ProjectMemberRepositoryCustomImpl implements ProjectMemberRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<ProjectMember> findDoneProjectMemberByMember(Member member) {
        return query
                .selectFrom(projectMember)
                .join(projectMember.project, project)
                .where(
                        projectMember.member.eq(member),
                        projectMember.project.status.eq(ProjectStatus.DONE))
                .fetch();
    }
}
