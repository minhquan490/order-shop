package com.order.bachlinh.core.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.bachlinh.core.annotation.Label;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.spi.validator.ProductValidator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Set;

@Entity
@Table(
        name = "PRODUCT",
        indexes = {
                @Index(name = "idx_product_name", columnList = "NAME", unique = true)
        }
)
@Getter
@Setter
@Label("PRD-")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Validator(validators = ProductValidator.class)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "product")
public class Product extends AbstractEntity {

    @Id
    @Column(name = "ID", nullable = false, unique = true, columnDefinition = "varchar(10)")
    private Object id;

    @Column(name = "NAME", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "SIZE", length = 3, nullable = false)
    private String size;

    @Column(name = "COLOR", length = 30, nullable = false)
    private String color;

    @Column(name = "TAO_BAO_URL")
    private String taobaoUrl;

    @Column(name = "DESCRIPTION", length = 400)
    private String description;

    @Column(name = "ENABLED", columnDefinition = "bit", nullable = false)
    private boolean enabled = true;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private Set<ProductMedia> pictures;

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private Set<Category> categories;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private Set<Cart> carts;

    @Override
    public void setId(Object id) {
        if (!(id instanceof String)) {
            throw new PersistenceException("Id of product must be string");
        }
        this.id = id;
    }
}
