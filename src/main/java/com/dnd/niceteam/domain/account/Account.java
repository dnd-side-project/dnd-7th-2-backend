package com.dnd.niceteam.domain.account;

import com.dnd.niceteam.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "email", length = 65, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 105, nullable = false)
    private String password;

    @Setter
    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 25, nullable = false)
    private Role role = Role.USER;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
