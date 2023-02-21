package com.order.bachlinh.core.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.bachlinh.core.annotation.Label;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "CART", indexes = @Index(name = "idx_cart_customer", columnList = "CUSTOMER_ID"))
@Label("CRT-")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
public class Cart extends AbstractEntity {

    @Id
    @Column(name = "ID", nullable = false, unique = true, columnDefinition = "varchar(10)")
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CUSTOMER_ID", unique = true, nullable = false, updatable = false)
    @JsonIgnore
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "PRODUCT_CART",
            joinColumns = @JoinColumn(name = "CART_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID"),
            indexes = @Index(name = "idx_product_cart", columnList = "CART_ID, PRODUCT_ID"),
            uniqueConstraints = @UniqueConstraint(name = "udx", columnNames = "CART_ID")
    )
    private Set<Product> products;

    @Override
    public void setId(Object id) {
        if (!(id instanceof String)) {
            throw new PersistenceException("Id of cart must be string");
        }
        this.id = (String) id;
    }
}
