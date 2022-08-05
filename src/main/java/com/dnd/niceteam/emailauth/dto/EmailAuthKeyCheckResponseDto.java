package com.dnd.niceteam.emailauth.dto;

import lombok.Data;

@Data
public class EmailAuthKeyCheckResponseDto {

    private String email;

    private Boolean authenticated;
}
