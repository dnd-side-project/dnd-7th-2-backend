package com.dnd.niceteam.member.repository;

import com.dnd.niceteam.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByUsername(String username);
}
