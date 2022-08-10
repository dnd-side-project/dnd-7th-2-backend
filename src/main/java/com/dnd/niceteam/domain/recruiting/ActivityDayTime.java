package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Getter
@SQLDelete(sql = "UPDATE activity_day_time SET use_yn = false WHERE activity_day_time_id = ?")
@Where(clause = "use_yn = true")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_day_time")
public class ActivityDayTime extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_day_time_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recruiting_id", nullable = false)
    private Recruiting recruiting;

    //'월'~'금'
    @Column(nullable = false)
    private char day;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

}

