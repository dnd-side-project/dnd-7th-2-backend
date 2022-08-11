package com.dnd.niceteam.domain.project;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class LectureTime {

    @Column(name = "`day`", nullable = false)
    private Character day;

    @Column(nullable = false, name = "start_time")
    private LocalTime startTime;

    @Builder
    private LectureTime(Character day, LocalTime startTime) {
        this.day = day;
        this.startTime = startTime;
    }

}