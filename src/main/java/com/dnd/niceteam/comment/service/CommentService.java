package com.dnd.niceteam.comment.service;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import com.dnd.niceteam.domain.comment.Comment;
import com.dnd.niceteam.domain.comment.CommentRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.member.util.MemberUtils;
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
    public CommentCreation.ResponseDto addComment(Long recruitingId, String username, CommentCreation.RequestDto commentDto) {
        Recruiting recruiting = getRecruitingtEntity(recruitingId);
        recruiting.plusCommentCount();

        Member member = MemberUtils.findMemberByEmail(username, memberRepository);
        Comment savedComment = commentRepository.save(
                commentDto.toEntity(member, recruiting)
        );

        CommentCreation.ResponseDto response = new CommentCreation.ResponseDto();
        response.setId(savedComment.getId());
        return response;
    }

    private Recruiting getRecruitingtEntity(Long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }

}
