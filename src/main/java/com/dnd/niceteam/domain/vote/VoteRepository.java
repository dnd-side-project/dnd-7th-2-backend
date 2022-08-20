package com.dnd.niceteam.domain.vote;

import com.dnd.niceteam.domain.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByVoteGroupAndVoter(VoteGroup voteGroup, ProjectMember voter);

}
