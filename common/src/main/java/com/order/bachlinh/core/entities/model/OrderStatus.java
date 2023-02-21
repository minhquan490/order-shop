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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ORDER_STATUS")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Validator(validators = OrderStatusValidator.class)
@EqualsAndHashCode(callSuper = true)
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
}
