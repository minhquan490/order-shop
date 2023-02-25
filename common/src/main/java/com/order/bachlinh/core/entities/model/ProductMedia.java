package com.order.bachlinh.core.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.spi.validator.ProductPictureValidator;
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

import java.util.Objects;

@Entity
@Table(name = "PRODUCT_MEDIA", indexes = @Index(name = "idx_product_media_product", columnList = "PRODUCT_ID"))
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Validator(validators = ProductPictureValidator.class)
public class ProductMedia extends AbstractEntity {

    @Id
    @Column(name = "ID", nullable = false, unique = true, columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "URL", nullable = false, length = 100)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    @JsonIgnore
    private Product product;

    @Override
    public void setId(Object id) {
        if (!(id instanceof Integer)) {
            throw new PersistenceException("Id of product picture must be int");
        }
        this.id = (Integer) id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductMedia that = (ProductMedia) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
