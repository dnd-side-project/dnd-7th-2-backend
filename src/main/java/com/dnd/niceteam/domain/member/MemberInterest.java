package com.dnd.niceteam.domain.member;

import com.dnd.niceteam.domain.code.Field;
import com.dnd.niceteam.domain.code.FieldCategory;
import lombok.*;

import javax.persistence.*;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MemberInterest {

    @Column(name = "field", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private Field field;

    @Column(name = "field_category", length = 25, nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldCategory fieldCategory;
}
