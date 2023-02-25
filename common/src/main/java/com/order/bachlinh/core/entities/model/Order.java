package com.order.bachlinh.core.entities.model;

import com.order.bachlinh.core.annotation.Label;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.spi.validator.OrderValidator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "ORDERS", indexes = @Index(name = "idx_order_customer", columnList = "CUSTOMER_ID"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "order")
@Label("ODR-")
@Validator(validators = OrderValidator.class)
public class Order extends AbstractEntity {

    @Id
    @Column(name = "ID", unique = true, nullable = false, columnDefinition = "varchar(10)")
    private String id;

    @Column(name = "DEPOSITED", nullable = false, columnDefinition = "bit")
    private boolean deposited;

    @Column(name = "TOTAL_DEPOSIT", nullable = false)
    private int totalDeposit;

    @Column(name = "ORDER_TIME", nullable = false)
    private Timestamp timeOrder;

    @OneToOne(fetch = LAZY, orphanRemoval = true, optional = false)
    @JoinColumn(name = "ORDER_STATUS_ID", unique = true, nullable = false, updatable = false)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false, updatable = false)
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "order")
    private Set<OrderDetail> orderDetails;

    @Override
    public void setId(Object id) {
        if (!(id instanceof String)) {
            throw new PersistenceException("Id of order must be string");
        }
        this.id = (String) id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
