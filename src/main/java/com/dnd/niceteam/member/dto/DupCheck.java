package com.dnd.niceteam.member.dto;

import lombok.Data;

public interface DupCheck {

    @Data
    class ResponseDto {

        private Boolean duplicated;
    }
}
