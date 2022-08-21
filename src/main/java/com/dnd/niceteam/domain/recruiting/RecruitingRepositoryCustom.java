package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.code.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface RecruitingRepositoryCustom {
    Page<Recruiting> findAllByInterestingFieldsOrderByWriterLevel(Set<Field> interestingFields, Pageable pageable);

    Page<Recruiting> findAllSideBySearchWordAndFieldOrderByCreatedDate(String searchWord, Field field, Pageable pageable);

    Page<Recruiting> findAllLectureBySearchWordAndDepartmentOrderByCreatedDate(String searchWord, String department, Pageable pageable);

}
