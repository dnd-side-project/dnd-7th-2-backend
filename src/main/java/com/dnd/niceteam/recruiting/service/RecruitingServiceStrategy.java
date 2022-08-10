package com.dnd.niceteam.recruiting;

import com.dnd.niceteam.recruiting.dto.RecruitingCreation;
import org.springframework.transaction.annotation.Transactional;

public interface RecruitingServiceStrategy {
    // TODO: 2022-08-09 Transactional이 먹히나?
    @Transactional
    RecruitingCreation.ResponseDto saveProjectAndRecruiting(RecruitingCreation.RequestDto requestDto);

}
