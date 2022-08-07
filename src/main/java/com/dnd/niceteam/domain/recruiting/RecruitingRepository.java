package com.dnd.niceteam.domain.recruiting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitingRepository<T extends Recruiting> extends JpaRepository<T, Long> {
}
