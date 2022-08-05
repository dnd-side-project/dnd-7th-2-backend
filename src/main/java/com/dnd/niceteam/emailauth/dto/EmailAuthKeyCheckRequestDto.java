package com.dnd.niceteam.emailauth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class EmailAuthKeyCheckRequestDto {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String authKey;
}
