package com.order.bachlinh.core.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.bachlinh.core.annotation.Label;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.spi.validator.CategoryValidator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CATEGORY", indexes = @Index(name = "idx_category_name", columnList = "NAME", unique = true))
@Getter
@Setter
@Label("CTR-")
@Validator(validators = CategoryValidator.class)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "category")
public class Category extends AbstractEntity {

    @Id
    @Column(name = "ID", unique = true, nullable = false, columnDefinition = "varchar(10)")
    private String id;

    @Column(name = "NAME", unique = true, nullable = false, length = 60)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "PRODUCT_CATEGORY",
            joinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID"),
            indexes = @Index(name = "idx_product_category", columnList = "CATEGORY_ID, PRODUCT_ID"),
            uniqueConstraints = @UniqueConstraint(name = "udx_product_category_product", columnNames = "CATEGORY_ID")
    )
    @JsonIgnore
    private Set<Product> products;

    @Override
    public void setId(Object id) {
        if (!(id instanceof String)) {
            throw new PersistenceException("Id of category must be string");
        }
        this.id = (String) id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return getId() != null && Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
