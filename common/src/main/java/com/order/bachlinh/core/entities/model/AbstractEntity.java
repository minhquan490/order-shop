package com.order.bachlinh.core.entities.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.Objects;

@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract non-sealed class AbstractEntity implements BaseEntity {

    @Column(name = "CREATED_BY", nullable = false)
    private Object createdBy;

    @Column(name = "MODIFIED_BY", nullable = false)
    private Object modifiedBy;

    @Column(name = "CREATED_DATE", nullable = false)
    private Timestamp createdDate;

    @Column(name = "MODIFIED_DATE", nullable = false)
    private Timestamp modifiedDate;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public int compareTo(@NonNull BaseEntity that) {
        if (this.equals(that)) return 0;
        if (this.hashCode() - that.hashCode() > 0) {
            return 1;
        } else {
            return -1;
        }
    }
}
