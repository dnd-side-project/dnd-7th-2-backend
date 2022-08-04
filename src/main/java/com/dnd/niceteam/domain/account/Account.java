package com.dnd.niceteam.domain.account;

import com.dnd.niceteam.domain.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "account")
@Where(clause = "use_yn = true")
@SQLDelete(sql = "UPDATE account SET use_yn = false, deleted_at = NOW() where account_id = ?")
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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
