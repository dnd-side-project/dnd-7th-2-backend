package com.dnd.niceteam.recruiting.dto;

import com.dnd.niceteam.domain.code.DayOfWeek;
import com.dnd.niceteam.domain.recruiting.ActivityDayTime;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class ActivityDayTimeDto {
    // TODO id가 필요하다면 추후 추가
    @NotNull
    private DayOfWeek dayOfWeek;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    public ActivityDayTime toEntity() {
        return ActivityDayTime.builder()
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
    public static ActivityDayTimeDto from(ActivityDayTime activityDayTime) {
        ActivityDayTimeDto dto = new ActivityDayTimeDto();
        dto.setDayOfWeek(activityDayTime.getDayOfWeek());
        dto.setStartTime(activityDayTime.getStartTime());
        dto.setEndTime(activityDayTime.getEndTime());

        return dto;
    }
}
