package com.dnd.niceteam.comment.dto;

import com.dnd.niceteam.domain.code.Type;
import com.dnd.niceteam.domain.comment.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public interface CommentFind {
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class ResponseDto {
        @NotNull
        private Long commentId;
        @NotNull
        private Long parentId;
        @NotNull
        private String content;
        @NotNull
        private Long recruitingId;
        @NotNull
        private LocalDateTime createdAt;

        // 목록
        private String nickname;
        //내가 쓴글
        private String recruitingTitle;
        private Type recruitingType;

        public static ResponseDto fromMyComments(Comment comment) {
            ResponseDto dto = createCommonListResponseDto(comment);
            dto.setNickname(comment.getMember().getNickname());
            return dto;
        }
        public static ResponseDto fromRecruitingComments(Comment comment) {
            ResponseDto dto = createCommonListResponseDto(comment);
            dto.setRecruitingTitle(comment.getRecruiting().getTitle());
            dto.setRecruitingType(comment.getRecruiting().getRecruitingType());
            return dto;
        }

        private static ResponseDto createCommonListResponseDto(Comment comment) {
            ResponseDto dto = new ResponseDto();
            dto.setCommentId(comment.getId());
            dto.setParentId(comment.getParentId());
            dto.setRecruitingId(comment.getRecruiting().getId());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedDate());
            return dto;
        }
    }
}
