package com.dnd.niceteam.domain.emailauth;

import com.dnd.niceteam.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "email_auth",
        indexes = @Index(name = "IX_email_auth_email", columnList = "email"))
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_auth_id")
    private Long id;

    @Column(name = "email", length = 65, nullable = false)
    private String email;

    @Column(name = "auth_key", length = 25, nullable = false)
    private String authKey;

    @Column(name = "authenticated", nullable = false)
    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    private boolean authenticated = false;

    public void authenticate() {
        setAuthenticated(true);
    }
}
