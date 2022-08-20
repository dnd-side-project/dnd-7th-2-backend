package com.dnd.niceteam.vote.dto;

import com.dnd.niceteam.domain.code.VoteType;
import com.dnd.niceteam.domain.project.ProjectMember;
import com.dnd.niceteam.domain.vote.Vote;
import com.dnd.niceteam.domain.vote.VoteGroup;
import lombok.Data;

public interface VoteRequest {

    @Data
    class Add {

        private VoteType type;

        private Long projectId;

        private Long candidateMemberId;

        private Boolean choice;

        public Vote toEntity(VoteGroup voteGroup, ProjectMember voter) {
            return Vote.builder()
                    .choice(choice)
                    .voteGroup(voteGroup)
                    .voter(voter)
                    .build();
        }

    }

}
