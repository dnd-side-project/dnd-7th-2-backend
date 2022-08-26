package com.dnd.niceteam.comment.dto;

import com.dnd.niceteam.domain.comment.Comment;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface CommentModify {
    @Data
    class RequestDto {
        @NotNull
        private Long id;
        @Size(max = 255)
        @NotNull
        private String content;
    }

    @Data
    class ResponseDto {
        @NotNull
        private Long id;
        @NotNull
        private Long parentId;
        @NotNull
        private Long groupNo;

        public static CommentModify.ResponseDto from(Comment comment) {
            CommentModify.ResponseDto responseDto = new CommentModify.ResponseDto();
            responseDto.setId(comment.getId());
            responseDto.setParentId(comment.getParentId());
            responseDto.setGroupNo(comment.getGroupNo());
            return responseDto;
        }
    }
}
