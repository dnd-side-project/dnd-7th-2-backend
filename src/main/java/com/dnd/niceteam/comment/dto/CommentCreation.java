package com.dnd.niceteam.comment.dto;

import com.dnd.niceteam.domain.comment.Comment;
import com.dnd.niceteam.domain.member.Member;
import com.dnd.niceteam.domain.recruiting.Recruiting;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface CommentCreation {
    @Data
    class RequestDto {
        @Size(max = 255)
        @NotNull
        private String content;

        @Nullable
        private Long parentId;

        public Comment toEntity(Member member, Recruiting recruiting) {
            return Comment.builder()
                    .member(member)
                    .recruiting(recruiting)
                    .content(content)
                    .parentId(parentId)
                    .build();
        }
    }

    @Data
    class ResponseDto {
        @NotNull
        private Long id;
    }
}
