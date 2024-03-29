package com.dnd.niceteam.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(name = "created_by", length = 45, updatable = false, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 45, nullable = false)
    private String lastModifiedBy;
}
