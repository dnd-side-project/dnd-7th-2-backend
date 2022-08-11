package com.dnd.niceteam.comment.service;

import com.dnd.niceteam.comment.dto.CommentCreation;
import com.dnd.niceteam.domain.comment.Comment;
import com.dnd.niceteam.domain.comment.CommentRepository;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.member.MemberRepository;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import com.dnd.niceteam.domain.recruiting.RecruitingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dnd.niceteam.comment.service.CommentTestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks CommentService commentService;

    @Mock CommentRepository commentRepository;
    @Mock MemberRepository memberRepository;
    @Mock RecruitingRepository recruitingRepository;

    @DisplayName("신규 댓글을 작성합니다.")
    @Test
    void create() {
        //given
        Member member = mock(Member.class);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        Recruiting recruiting = mock(Recruiting.class);
        when(recruitingRepository.findById(anyLong())).thenReturn(Optional.of(recruiting));

        CommentCreation.RequestDto commentRequest = createCommentRequest();
        Comment createdComment = mock(Comment.class, RETURNS_DEEP_STUBS);
        when(commentRepository.save(any(Comment.class))).thenReturn(createdComment);

        //when
        CommentCreation.ResponseDto response = commentService.addComment(MEMBER_ID, commentRequest);

        //then
        assertThat(response.getId()).isEqualTo(createdComment.getId());
    }

}