package com.order.bachlinh.core.entities.model;

import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.spi.validator.OrderStatusValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
@Entity
@Table(name = "ORDER_STATUS")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Validator(validators = OrderStatusValidator.class)
public class OrderStatus extends AbstractEntity {

    @Id
    @Column(name = "ID", nullable = false, unique = true, columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "STATUS", nullable = false, length = 30)
    private String status;

    @OneToOne(optional = false, mappedBy = "orderStatus")
    private Order order;

    @Override
    public void setId(Object id) {
        if (!(id instanceof Integer)) {
            throw new PersistenceException("Id of OrderStatus must be int");
        }
        this.id = (Integer) id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderStatus that = (OrderStatus) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
