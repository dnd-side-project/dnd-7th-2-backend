package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>, ProjectMemberRepositoryCustom {

    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);

}
