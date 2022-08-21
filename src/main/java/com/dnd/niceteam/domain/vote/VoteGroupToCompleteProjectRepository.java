package com.dnd.niceteam.domain.vote;

import com.dnd.niceteam.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteGroupToCompleteProjectRepository extends JpaRepository<VoteGroupToCompleteProject, Long> {

    Optional<VoteGroupToCompleteProject> findByProject(Project project);

}
