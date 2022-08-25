package com.dnd.niceteam.comment.service;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.comment.dto.CommentFind;
import com.dnd.niceteam.comment.dto.CommentModify;
import com.dnd.niceteam.common.dto.Pagination;
import com.dnd.niceteam.common.util.PaginationUtil;
import com.dnd.niceteam.domain.comment.exception.CommentNotFoundException;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.recruiting.exception.RecruitingNotFoundException;
import com.dnd.niceteam.domain.comment.Comment;
import com.dnd.niceteam.domain.comment.CommentRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import com.dnd.niceteam.member.util.MemberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final RecruitingRepository recruitingRepository;

    @Transactional
    public CommentCreation.ResponseDto addComment(Long recruitingId, String username, CommentCreation.RequestDto requestDto) {
        Recruiting recruiting = getRecruitingtEntity(recruitingId);

        Member member = MemberUtils.findMemberByEmail(username, memberRepository);
        Comment createdComment = requestDto.toEntity(member, recruiting);

        if (requestDto.isChild()) {
            Comment parentComment = commentRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new CommentNotFoundException("Parent CommentId = " + requestDto.getParentId()));

            createdComment.setParentId(parentComment.getId());
            createdComment.setGroupNo(parentComment.getGroupNo());
        } else {
            Long maxGroupNo = commentRepository.findMaxGroupNo()
                            .orElse(0L);
            createdComment.setGroupNo(maxGroupNo + 1);
        }

        Comment savedComment = commentRepository.save(createdComment);
        recruiting.plusCommentCount();

        return CommentCreation.ResponseDto.from(savedComment);
    }

    public Pagination<CommentFind.ResponseDto> getComments(int page, int perSize, Long recruitingId, String email) {
        Pageable pageable = PageRequest.of(page - 1, perSize);

        Page<CommentFind.ResponseDto> commentPagesDto;
        if (isNull(email))
            commentPagesDto = commentRepository.findPageByRecruitingIdOrderByGroupNoAscCreatedDateAsc(pageable, recruitingId)
                    .map(CommentFind.ResponseDto::fromRecruitingComments);
        else {  // 내가 쓴 댓글 목록
            Member member = MemberUtils.findMemberByEmail(email, memberRepository);
            commentPagesDto = commentRepository.findPageByMemberOrderByCreatedDateDesc(pageable, member)
                    .map(CommentFind.ResponseDto::fromMyComments);
        }
        return PaginationUtil.pageToPagination(commentPagesDto);
    }

    @Transactional
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("comment ID = " + commentId));
        commentRepository.delete(comment);

        Recruiting recruiting = recruitingRepository.findById(comment.getRecruiting().getId())
                .orElseThrow(() -> new RecruitingNotFoundException("recruiting ID = " + comment.getRecruiting().getId()));
        recruiting.minusCommentCount();
    }

    @Transactional
    public CommentModify.ResponseDto modifyComment(CommentModify.RequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new CommentNotFoundException("comment ID = " + requestDto.getId()));

        comment.update(requestDto.getContent());

        return CommentModify.ResponseDto.from(comment);
    }

    private Recruiting getRecruitingtEntity(Long recruitingId) {
        return recruitingRepository.findById(recruitingId)
                .orElseThrow(() -> new RecruitingNotFoundException("recruitingId = " + recruitingId));
    }
}
