package com.dnd.niceteam.domain.memberscore;

import com.dnd.niceteam.domain.code.ReviewTag;
import com.dnd.niceteam.domain.review.MemberReview;
import com.dnd.niceteam.domain.review.MemberReviewTag;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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

    @Test
    void applyReview_TwoMember_OneReview() {
        // given
        MemberScore memberScore = MemberScore.createInitMemberScore();
        MemberReview mockReview = mock(MemberReview.class);
        given(mockReview.getSkipped()).willReturn(false);
        given(mockReview.getParticipationScore()).willReturn(5);
        given(mockReview.getTeamAgainScore()).willReturn(5);
        given(mockReview.getMemberReviewTags())
                .willReturn(Stream.of(ReviewTag.RESPONSIBILITY, ReviewTag.PUNCTUALITY)
                        .map(reviewTag -> new MemberReviewTag(reviewTag, null))
                        .collect(Collectors.toSet()));

        // when
        memberScore.applyReview(2, mockReview);

        // then
        assertThat(memberScore.getReviewNum()).isEqualTo(1);
        assertThat(memberScore.getTotalParticipationScore()).isEqualTo(5);
        assertThat(memberScore.getTotalTeamAgainScore()).isEqualTo(5);
        assertThat(memberScore.getLevel()).isEqualTo(1);
        assertThat(memberScore.getTotalFeeds()).isEqualTo(4);
        assertThat(memberScore.getReviewTagToNums()).isEqualTo(Map.of(
                ReviewTag.RESPONSIBILITY, 1,
                ReviewTag.PUNCTUALITY, 1,
                ReviewTag.MOOD_MAKER, 0,
                ReviewTag.TIME_MANNERS, 0,
                ReviewTag.POSITIVE, 0,
                ReviewTag.IDEA, 0,
                ReviewTag.FEEDBACK, 0,
                ReviewTag.DECISIVE, 0,
                ReviewTag.LIKE_MINE, 0
        ));
    }

    @Test
    void applyReview_ThreeMember_TwoReview() {
        // given
        MemberScore memberScore = MemberScore.createInitMemberScore();
        MemberReview mockReview = mock(MemberReview.class);
        given(mockReview.getSkipped()).willReturn(false);
        given(mockReview.getParticipationScore()).willReturn(5);
        given(mockReview.getTeamAgainScore()).willReturn(5);
        given(mockReview.getMemberReviewTags())
                .willReturn(Stream.of(ReviewTag.RESPONSIBILITY, ReviewTag.PUNCTUALITY)
                        .map(reviewTag -> new MemberReviewTag(reviewTag, null))
                        .collect(Collectors.toSet()));

        // when
        memberScore.applyReview(3, mockReview);
        memberScore.applyReview(3, mockReview);

        // then
        assertThat(memberScore.getReviewNum()).isEqualTo(2);
        assertThat(memberScore.getTotalParticipationScore()).isEqualTo(10);
        assertThat(memberScore.getTotalTeamAgainScore()).isEqualTo(10);
        assertThat(memberScore.getLevel()).isEqualTo(1);
        assertThat(memberScore.getTotalFeeds()).isEqualTo(((Math.log(3) / Math.log(2)) / 2) * (4 + 4));
        assertThat(memberScore.getReviewTagToNums()).isEqualTo(Map.of(
                ReviewTag.RESPONSIBILITY, 2,
                ReviewTag.PUNCTUALITY, 2,
                ReviewTag.MOOD_MAKER, 0,
                ReviewTag.TIME_MANNERS, 0,
                ReviewTag.POSITIVE, 0,
                ReviewTag.IDEA, 0,
                ReviewTag.FEEDBACK, 0,
                ReviewTag.DECISIVE, 0,
                ReviewTag.LIKE_MINE, 0
        ));
    }

    @Test
    void applyReview_FourMember_ThreeReview() {
        // given
        MemberScore memberScore = MemberScore.createInitMemberScore();
        MemberReview mockReview = mock(MemberReview.class);
        given(mockReview.getSkipped()).willReturn(false);
        given(mockReview.getParticipationScore()).willReturn(5);
        given(mockReview.getTeamAgainScore()).willReturn(5);
        given(mockReview.getMemberReviewTags())
                .willReturn(Stream.of(ReviewTag.RESPONSIBILITY, ReviewTag.PUNCTUALITY)
                        .map(reviewTag -> new MemberReviewTag(reviewTag, null))
                        .collect(Collectors.toSet()));

        // when
        memberScore.applyReview(4, mockReview);
        memberScore.applyReview(4, mockReview);
        memberScore.applyReview(4, mockReview);

        // then
        assertThat(memberScore.getReviewNum()).isEqualTo(3);
        assertThat(memberScore.getTotalParticipationScore()).isEqualTo(15);
        assertThat(memberScore.getTotalTeamAgainScore()).isEqualTo(15);
        assertThat(memberScore.getLevel()).isEqualTo(1);
        assertThat(memberScore.getTotalFeeds()).isEqualTo(((Math.log(4) / Math.log(2)) / 3) * (4 + 4 + 4));
        assertThat(memberScore.getReviewTagToNums()).isEqualTo(Map.of(
                ReviewTag.RESPONSIBILITY, 3,
                ReviewTag.PUNCTUALITY, 3,
                ReviewTag.MOOD_MAKER, 0,
                ReviewTag.TIME_MANNERS, 0,
                ReviewTag.POSITIVE, 0,
                ReviewTag.IDEA, 0,
                ReviewTag.FEEDBACK, 0,
                ReviewTag.DECISIVE, 0,
                ReviewTag.LIKE_MINE, 0
        ));
    }

    @Test
    void applyReview_TwoMember_ThreeReview_LevelUp() {
        // given
        MemberScore memberScore = MemberScore.createInitMemberScore();
        MemberReview mockReview = mock(MemberReview.class);
        given(mockReview.getSkipped()).willReturn(false);
        given(mockReview.getParticipationScore()).willReturn(5);
        given(mockReview.getTeamAgainScore()).willReturn(5);
        given(mockReview.getMemberReviewTags())
                .willReturn(Stream.of(ReviewTag.RESPONSIBILITY, ReviewTag.PUNCTUALITY)
                        .map(reviewTag -> new MemberReviewTag(reviewTag, null))
                        .collect(Collectors.toSet()));

        // when
        memberScore.applyReview(2, mockReview);
        memberScore.applyReview(2, mockReview);
        memberScore.applyReview(2, mockReview);

        // then
        assertThat(memberScore.getReviewNum()).isEqualTo(3);
        assertThat(memberScore.getTotalParticipationScore()).isEqualTo(15);
        assertThat(memberScore.getTotalTeamAgainScore()).isEqualTo(15);
        assertThat(memberScore.getLevel()).isEqualTo(2);
        assertThat(memberScore.getTotalFeeds()).isEqualTo(12);
        assertThat(memberScore.getReviewTagToNums()).isEqualTo(Map.of(
                ReviewTag.RESPONSIBILITY, 3,
                ReviewTag.PUNCTUALITY, 3,
                ReviewTag.MOOD_MAKER, 0,
                ReviewTag.TIME_MANNERS, 0,
                ReviewTag.POSITIVE, 0,
                ReviewTag.IDEA, 0,
                ReviewTag.FEEDBACK, 0,
                ReviewTag.DECISIVE, 0,
                ReviewTag.LIKE_MINE, 0
        ));
    }
}