package com.dnd.niceteam.comment;

import com.dnd.niceteam.comment.dto.CommentCreation;

public class CommentTestFactory {
    public static final String COMMENT_CONTENT = "모집글의 댓글입니다.";
    public static final Long PARENT_ID = 0L;
    public static final Long RECRUITING_ID = 1L;

    public static CommentCreation.RequestDto createCommentRequest() {
        CommentCreation.RequestDto dto = new CommentCreation.RequestDto();
        dto.setContent(COMMENT_CONTENT);
        dto.setParentId(PARENT_ID);
        return dto;
    }

    public static final Long COMMENT_ID = 1L;
    public static CommentCreation.ResponseDto createCommentResponse() {
        CommentCreation.ResponseDto dto = new CommentCreation.ResponseDto();
        dto.setId(COMMENT_ID);
        return dto;
    }

}