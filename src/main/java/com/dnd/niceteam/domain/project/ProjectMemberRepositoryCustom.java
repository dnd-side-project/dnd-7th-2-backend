package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.member.Member;

import java.util.List;

public interface ProjectMemberRepositoryCustom {

    List<ProjectMember> findDoneProjectMemberByMember(Member member);
}
