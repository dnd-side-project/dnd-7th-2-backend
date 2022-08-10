package com.dnd.niceteam.domain.project;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "side_project")
@NoArgsConstructor
@DiscriminatorValue("SIDE")
public class SideProject extends Project {

    @Column(name = "field", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private Field field;

    @Column(name = "field_category", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldCategory fieldCategory;

    @Builder
    private SideProject(
            String name,
            LocalDate startDate,
            LocalDate endDate,
            Field field,
            FieldCategory fieldCategory
    ) {
        super(name, startDate, endDate);
        this.field = field;
        this.fieldCategory = fieldCategory;
    }

}
