package com.dnd.niceteam.domain.memberscore;

import com.dnd.niceteam.domain.code.ReviewTag;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class MemberScoreTest {

    @Test
    void createInitMemberScore() {
        // when
        MemberScore initMemberScore = MemberScore.createInitMemberScore();

        // then
        assertThat(initMemberScore.getId()).isNull();
        assertThat(initMemberScore.getLevel()).isEqualTo(1);
        assertThat(initMemberScore.getReviewNum()).isZero();
        assertThat(initMemberScore.getTotalParticipationScore()).isZero();
        assertThat(initMemberScore.getTotalTeamAgainScore()).isZero();
        assertThat(initMemberScore.getTotalFeeds()).isZero();
        assertThat(initMemberScore.getReviewTagToNums()).isEqualTo(
                IntStream.range(0, ReviewTag.values().length).boxed()
                        .collect(Collectors.toMap(i -> ReviewTag.values()[i], i -> 0)));
    }
}