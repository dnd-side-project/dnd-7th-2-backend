package com.dnd.niceteam.domain.member;

import com.dnd.niceteam.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", length = 45, unique = true, nullable = false)
    private String username;

    @Column(name = "password", length = 105, nullable = false)
    private String password;

    @Column(name="role", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "email", length = 65, unique = true, nullable = false)
    private String email;

    @Column(name = "name", length = 25, nullable = false)
    private String name;

    @Column(name = "refresh_token")
    @Setter
    private String refreshToken;
}
