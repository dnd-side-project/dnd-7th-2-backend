package com.dnd.niceteam.domain.vote;

import com.dnd.niceteam.domain.project.Project;
import com.dnd.niceteam.domain.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VoteGroupToExpelRepository extends JpaRepository<VoteGroupToExpel, Long> {

    Optional<VoteGroupToExpel> findByProjectAndCandidateAndCreatedDateGreaterThan(
            @Param("project") Project project,
            @Param("candidate") ProjectMember candidate,
            @Param("createdDate")LocalDateTime createdDate
    );

}
