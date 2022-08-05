package com.dnd.niceteam.domain.recruiting;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("SIDE")
@PrimaryKeyJoinColumn(name = "side_recruiting_id")
@Table(name = "side_recruiting")
public class SideRecruiting extends Recruiting{
    @Column(name = "field", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private Field field;

    @Column(name = "field_category", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldCategory fieldCategory;
}
