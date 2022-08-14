package com.dnd.niceteam.bookmark.service;

import com.dnd.niceteam.bookmark.dto.BookmarkCreation;
import com.dnd.niceteam.comment.exception.RecruitingNotFoundException;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.bookmark.Bookmark;
import com.dnd.niceteam.domain.bookmark.BookmarkRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    private BookmarkService bookmarkService;

    @Mock
    private BookmarkRepository mockBookmarkRepository;

    @Mock
    private MemberRepository mockMemberRepository;

    @Mock
    private RecruitingRepository mockRecruitingRepository;

    @Test
    @DisplayName("북마크 생성")
    void createBookmark() {
        // given
        String givenEmail = "test@email.com";
        Member givenMember = Member.builder()
                .account(Account.builder().email(givenEmail).build())
                .build();
        given(mockMemberRepository.findByEmail(givenEmail))
                .willReturn(Optional.of(givenMember));

        long givenRecruitingId = 1L;
        Recruiting givenRecruiting = Recruiting.builder().id(givenRecruitingId).build();
        given(mockRecruitingRepository.findById(givenRecruitingId))
                .willReturn(Optional.of(givenRecruiting));

        long givenBookmarkId = 2L;
        Bookmark givenBookmark = Bookmark.builder()
                .id(givenBookmarkId)
                .member(givenMember)
                .recruiting(givenRecruiting)
                .build();
        given(mockBookmarkRepository.save(any(Bookmark.class)))
                .willReturn(givenBookmark);

        // when
        BookmarkCreation.ResponseDto responseDto = bookmarkService.createBookmark(
                "test@email.com", 1L);

        // then
        assertThat(responseDto.getId()).isEqualTo(givenBookmarkId);
    }

    @Test
    @DisplayName("북마크 생성 - 존재하지 않는 회원")
    void createBookmark_MemberNotFound() {
        // given
        given(mockMemberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> bookmarkService.createBookmark("test@email.com", 1L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("북마크 생성 - 존재하지 않는 모집글")
    void createBookmark_RecruitingNotFound() {
        // given
        String givenEmail = "test@email.com";
        Member givenMember = Member.builder()
                .account(Account.builder().email(givenEmail).build())
                .build();
        given(mockMemberRepository.findByEmail(givenEmail))
                .willReturn(Optional.of(givenMember));

        given(mockRecruitingRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> bookmarkService.createBookmark("test@email.com", 1L))
                .isInstanceOf(RecruitingNotFoundException.class);
    }
}