package com.dnd.niceteam.domain.recruiting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long>, ApplicantRepositoryCustom {
    Optional<Applicant> findByMemberIdAndRecruitingId(Long memberId, Long recruitingId);
}
