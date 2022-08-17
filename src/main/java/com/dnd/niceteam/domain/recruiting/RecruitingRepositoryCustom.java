package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.ProgressStatus;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface RecruitingRepositoryCustom {
    PageImpl<Recruiting> findAllByMemberId(Long memberId, Pageable pageable);

    PageImpl<Recruiting> findAllByMemberIdAndStatus(Long memberId, ProgressStatus progressStatus, Pageable pageable);

    PageImpl<Recruiting> findAllByInterestingFieldsOrderByWriterLevel(Set<Field> interestingFields, Pageable pageable);
}
