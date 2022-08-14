package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.code.DayOfWeek;
import com.dnd.niceteam.domain.project.LectureTime;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LectureTimeRequest {

        private DayOfWeek dayOfWeek;

        private LocalTime startTime;

        public LectureTime toEntity() {
                return LectureTime.builder()
                        .dayOfWeek(dayOfWeek)
                        .startTime(startTime)
                        .build();
        }

}
