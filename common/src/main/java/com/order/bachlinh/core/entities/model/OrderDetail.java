package com.order.bachlinh.core.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.spi.validator.OrderDetailValidator;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "ORDER_DETAIL", indexes = @Index(name = "idx_order", columnList = "ORDER_ID"))
@Validator(validators = OrderDetailValidator.class)
public class OrderDetail extends AbstractEntity {

    @Id
    @Column(name = "ID", nullable = false, unique = true, columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    @JsonIgnore
    private Order order;

    @Override
    public void setId(Object id) {
        if (!(id instanceof Integer)) {
            throw new PersistenceException("Id of OrderDetail must be int");
        }
        this.id = (Integer) id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderDetail that = (OrderDetail) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
