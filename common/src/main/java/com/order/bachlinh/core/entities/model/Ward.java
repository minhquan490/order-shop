package com.order.bachlinh.core.entities.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(
        name = "WARD",
        indexes = {
                @Index(name = "idx_ward_name", columnList = "NAME"),
                @Index(name = "idx_ward_code", columnList = "CODE")
        }
)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ward")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Ward extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Integer id;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "CODE")
    private Integer code;

    @Column(name = "CODE_NAME", length = 50)
    private String codeName;

    @Column(name = "DIVISION_TYPE", length = 50)
    private String divisionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISTRICT_ID", nullable = false, unique = true)
    private District district;

    @Override
    public void setId(Object id) {
        if (!(id instanceof Integer)) {
            throw new PersistenceException("Id of ward must be int");
        }
        this.id = (Integer) id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Ward ward = (Ward) o;
        return getId() != null && Objects.equals(getId(), ward.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
