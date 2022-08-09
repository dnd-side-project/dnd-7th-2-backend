package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.project.LectureTime;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LectureTimeRequest {

        private Character day;

        private LocalTime startTime;

        public LectureTime toEntity() {
                return LectureTime.builder()
                        .day(day)
                        .startTime(startTime)
                        .build();
        }

}
