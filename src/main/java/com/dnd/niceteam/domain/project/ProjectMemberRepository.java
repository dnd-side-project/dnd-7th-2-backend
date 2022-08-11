package com.dnd.niceteam.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long>, ProjectMemberRepositoryCustom {

    List<ProjectMember> findByProject(Project project);
}
