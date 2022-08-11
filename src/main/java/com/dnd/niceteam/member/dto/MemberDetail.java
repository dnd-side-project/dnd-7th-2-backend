package com.dnd.niceteam.member.dto;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.Personality;
import com.dnd.niceteam.domain.code.TagReview;
import lombok.Data;

import java.util.Map;
import java.util.Set;

public interface MemberDetail {

    @Data
    class ResponseDto {

        private Long id;

        private String nickname;

        private Personality personality;

        private String departmentName;

        private Set<Field> interestingFields;

        private Integer admissionYear;

        private String introduction;

        private String introductionUrl;

        private Integer level;

        private Double participationPct;

        private Map<TagReview, Integer> tagReviewToNums;

        private Integer numTotalEndProject;

        private Integer numCompleteProject;
    }
}
