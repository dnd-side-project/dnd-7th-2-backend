package com.dnd.niceteam.comment.service;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.comment.exception.RecruitingNotFoundException;
import com.dnd.niceteam.domain.comment.Comment;
import com.dnd.niceteam.domain.comment.CommentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.member.exception.MemberNotFoundException;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final RecruitingRepository recruitingRepository;

    @Transactional
    public CommentCreation.ResponseDto addComment(Long recruitingId, CommentCreation.RequestDto commentDto) {
        Comment savedComment = commentRepository.save(commentDto.toEntity(getMemberEntity(recruitingId),
                getRecruitingtEntity(recruitingId)));

        CommentCreation.ResponseDto response = new CommentCreation.ResponseDto();
        response.setId(savedComment.getId());
        return response;
    }

    private Recruiting getRecruitingtEntity(Long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }

    private Member getMemberEntity(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("memberId = " + memberId));
    }
}
