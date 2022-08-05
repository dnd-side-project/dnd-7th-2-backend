package com.dnd.niceteam.domain.emailauth;


import java.util.Optional;

public interface EmailAuthRepositoryCustom {

    Optional<EmailAuth> findLatestByEmail(String email);
}
