package com.dnd.niceteam.emailauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class EmailAuthKeyCreator {

    public static final int KEY_SIZE = 6;

    private final Random random = new Random();

    public String createEmailAuthKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < KEY_SIZE; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
