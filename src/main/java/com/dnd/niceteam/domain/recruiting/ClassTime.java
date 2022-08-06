package com.dnd.niceteam.domain.recruiting;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ClassTime {
    @Column(nullable = false)
    private char day;

    @Column(nullable = false, name = "start_time")
    private LocalTime startTime;
}