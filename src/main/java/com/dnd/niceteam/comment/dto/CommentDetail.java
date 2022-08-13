package com.dnd.niceteam.comment.dto;

import lombok.Data;

public interface CommentDetail {
    @Data
    public class ResponseDto {
        private Long commentId;
        private Long memberId;

        private String content;
        private String createdAt;
        private String updatedAt;
    }
}
