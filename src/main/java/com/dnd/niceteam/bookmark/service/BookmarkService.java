package com.dnd.niceteam.bookmark.service;

import com.dnd.niceteam.bookmark.dto.BookmarkCreation;
import com.dnd.niceteam.domain.bookmark.Bookmark;
import com.dnd.niceteam.domain.bookmark.BookmarkRepository;
import com.dnd.niceteam.domain.bookmark.exception.BookmarkExistingException;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import lombok.RequiredArgsConstructor;
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

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("email = " + email));
    }

    private Recruiting getRecruitingById(long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }
}
