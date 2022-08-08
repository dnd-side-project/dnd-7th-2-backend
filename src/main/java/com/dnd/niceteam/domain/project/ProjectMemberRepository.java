package com.dnd.niceteam.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>, ProjectMemberRepositoryCustom {
}
