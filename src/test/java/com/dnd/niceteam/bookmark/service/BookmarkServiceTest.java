package com.dnd.niceteam.bookmark.service;

import com.dnd.niceteam.bookmark.dto.BookmarkCreation;
import com.dnd.niceteam.bookmark.dto.BookmarkDeletion;
import com.dnd.niceteam.bookmark.dto.BookmarkDto;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.domain.account.Account;
import com.dnd.niceteam.domain.bookmark.Bookmark;
import com.dnd.niceteam.domain.bookmark.BookmarkRepository;
import com.dnd.niceteam.domain.bookmark.exception.BookmarkExistingException;
import com.dnd.niceteam.domain.bookmark.exception.BookmarkNotFoundException;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


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
        Recruiting givenRecruiting = Recruiting.builder().id(givenRecruitingId).bookmarkCount(0).build();
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
        assertThat(givenRecruiting.getBookmarkCount()).isEqualTo(1);
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

    @Test
    @DisplayName("북마크 생성 - 이미 존재하는 북마크")
    void createBookmark_BookmarkExisting() {
        // given
        given(mockMemberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(mock(Member.class)));

        given(mockRecruitingRepository.findById(anyLong()))
                .willReturn(Optional.of(mock(Recruiting.class)));

        given(mockBookmarkRepository.existsByMemberAndRecruiting(any(Member.class), any(Recruiting.class)))
                .willReturn(true);

        // expected
        assertThatThrownBy(() -> bookmarkService.createBookmark("test@email.com", 1L))
                .isInstanceOf(BookmarkExistingException.class);
    }

    @Test
    @DisplayName("북마크 삭제")
    void deleteBookmark() {
        // given
        Bookmark mockBookmark = mock(Bookmark.class);
        Recruiting mockRecruiting = mock(Recruiting.class);
        long givenBookmarkId = 1L;
        given(mockBookmark.getId()).willReturn(givenBookmarkId);
        given(mockBookmark.getRecruiting()).willReturn(mockRecruiting);

        given(mockBookmarkRepository.findById(givenBookmarkId))
                .willReturn(Optional.of(mockBookmark));


        // when
        BookmarkDeletion.ResponseDto responseDto = bookmarkService.deleteBookmark(givenBookmarkId);

        // then
        assertThat(responseDto.getId()).isEqualTo(givenBookmarkId);
        then(mockRecruiting).should().minusBookmarkCount();
    }

    @Test
    @DisplayName("북마크 삭제 - 존재하지 않는 북마크")
    void deleteBookmark_BookmarkNotFound() {
        // given
        given(mockBookmarkRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> bookmarkService.deleteBookmark(1L))
                .isInstanceOf(BookmarkNotFoundException.class);
    }

    @Test
    @DisplayName("북마크 소유 여부 확인")
    void isBookmarkOwnedByMember() {
        // given
        ArgumentCaptor<Long> bookmarkIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);

        // when
        bookmarkService.isBookmarkOwnedByMember(1L, "test@email.com");

        // then
        then(mockBookmarkRepository).should().existsByIdAndEmail(bookmarkIdCaptor.capture(), emailCaptor.capture());
        assertThat(bookmarkIdCaptor.getValue()).isEqualTo(1L);
        assertThat(emailCaptor.getValue()).isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("북마크 페이지 조회")
    void getBookmarkPageByUsername() {
        // given
        Member mockMember = mock(Member.class);
        String givenEmail = "test@email.com";
        given(mockMemberRepository.findByEmail(givenEmail))
                .willReturn(Optional.of(mockMember));
        PageRequest givenPageRequest = PageRequest.of(0, 10);
        given(mockBookmarkRepository.findPageWithRecruitingByMember(givenPageRequest, mockMember))
                .willReturn(new PageImpl<>(Collections.emptyList(), givenPageRequest, 0L));

        // when
        Pagination<BookmarkDto> bookmarkDtoPagination = bookmarkService.getBookmarkPageByUsername(
                givenPageRequest, givenEmail);

        // then
        assertThat(bookmarkDtoPagination.getPage()).isZero();
        assertThat(bookmarkDtoPagination.getPerSize()).isEqualTo(10);
        assertThat(bookmarkDtoPagination.getTotalPages()).isZero();
        assertThat(bookmarkDtoPagination.getTotalCount()).isZero();
        assertThat(bookmarkDtoPagination.getContents()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("북마크 페이지 조회 - 존재하지 않는 회원")
    void getBookmarkPageByUsername_MemberNotFound() {
        // given
        given(mockMemberRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // expected
        assertThatThrownBy(() -> bookmarkService.getBookmarkPageByUsername(
                PageRequest.of(0, 10), "test@email.com"))
                .isInstanceOf(MemberNotFoundException.class);
    }
}