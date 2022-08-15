package com.dnd.niceteam.project.dto;

import com.dnd.niceteam.domain.code.DayOfWeek;
import com.dnd.niceteam.domain.project.LectureTime;
import lombok.Data;

import java.time.LocalTime;

@Data
public class LectureTimeResponse {

        private DayOfWeek dayOfWeek;

        private LocalTime startTime;

        public static LectureTimeResponse from(LectureTime lectureTime) {
                LectureTimeResponse dto = new LectureTimeResponse();

                dto.setDayOfWeek(lectureTime.getDayOfWeek());
                dto.setStartTime(lectureTime.getStartTime());

                return dto;
        }

}
