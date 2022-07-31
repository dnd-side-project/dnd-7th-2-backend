package com.dnd.niceteam.domain.university;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "university")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id")
    private Long id;

    @Column(name = "name", length = 25, nullable = false)
    private String name;

    @Column(name = "email_domain", length = 25, nullable = false)
    private String emailDomain;
}
