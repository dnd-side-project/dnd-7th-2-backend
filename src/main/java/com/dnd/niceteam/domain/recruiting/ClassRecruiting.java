package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.department.Department;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CLASS")
@PrimaryKeyJoinColumn(name = "class_recruiting_id")
@Table(name = "class_recruiting")
public class ClassRecruiting extends Recruiting{
    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(length = 50, nullable = false)
    private String professor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Column(nullable = false)
    private Department department;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name="class_time", joinColumns = @JoinColumn(name= "recruiting_id", nullable = false))
    private Set<ClassTime> classTimes = new HashSet<>();

}