package com.dnd.niceteam.emailauth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class EmailAuthKeySendRequestDto {

    @Email
    @NotEmpty
    private String email;

    @NotNull
    private Long universityId;
}
