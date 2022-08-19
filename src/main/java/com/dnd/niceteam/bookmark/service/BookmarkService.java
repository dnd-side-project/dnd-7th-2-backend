package com.dnd.niceteam.bookmark.service;

import com.dnd.niceteam.bookmark.dto.BookmarkCreation;
import com.dnd.niceteam.bookmark.dto.BookmarkDeletion;
import com.dnd.niceteam.bookmark.dto.BookmarkDto;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.common.util.PaginationUtil;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    private final MemberRepository memberRepository;

    private final RecruitingRepository recruitingRepository;

    @Transactional
    public BookmarkCreation.ResponseDto createBookmark(String username, long recruitingId) {
        Member member = getMemberByEmail(username);
        Recruiting recruiting = getRecruitingById(recruitingId);
        if (bookmarkRepository.existsByMemberAndRecruiting(member, recruiting)) {
            throw new BookmarkExistingException(String.format(
                    "memberId = %d, recruitingId = %d", member.getId(), recruiting.getId()));
        }
        Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
                .member(member)
                .recruiting(recruiting)
                .build());
        recruiting.plusBookmarkCount();
        BookmarkCreation.ResponseDto responseDto = new BookmarkCreation.ResponseDto();
        responseDto.setId(bookmark.getId());
        return responseDto;
    }

    @Transactional
    public BookmarkDeletion.ResponseDto deleteBookmark(long bookmarkId) {
        Bookmark bookmark = getBookmarkById(bookmarkId);
        Recruiting recruiting = bookmark.getRecruiting();
        recruiting.minusBookmarkCount();
        bookmarkRepository.delete(bookmark);
        BookmarkDeletion.ResponseDto responseDto = new BookmarkDeletion.ResponseDto();
        responseDto.setId(bookmark.getId());
        return responseDto;
    }

    public Pagination<BookmarkDto> getBookmarkPageByUsername(Pageable pageable, String username) {
        Member member = getMemberByEmail(username);
        Page<BookmarkDto> page = bookmarkRepository.findPageWithRecruitingByMember(pageable, member)
                .map(BookmarkDto::of);
        return PaginationUtil.pageToPagination(page);
    }

    public boolean isBookmarkOwnedByMember(long bookmarkId, String username) {
        return bookmarkRepository.existsByIdAndEmail(bookmarkId, username);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("email = " + email));
    }

    private Recruiting getRecruitingById(long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }

    private Bookmark getBookmarkById(long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkNotFoundException("bookmarkId = " + bookmarkId));
    }
}
