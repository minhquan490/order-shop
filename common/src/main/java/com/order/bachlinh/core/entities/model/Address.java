package com.order.bachlinh.core.entities.model;

import com.order.bachlinh.core.annotation.Label;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.spi.validator.AddressValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Label("ADR-")
@Entity
@Table(name = "ADDRESS")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Validator(validators = AddressValidator.class)
public class Address extends AbstractEntity {

    @Id
    @Column(name = "ID", nullable = false, unique = true, columnDefinition = "varchar(10)")
    private String id;

    @Column(name = "VALUE", nullable = false)
    private String value;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "COUNTRY")
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public void setId(Object id) {
        if (!(id instanceof String)) {
            throw new PersistenceException("Address entity must be string");
        }
        this.id = (String) id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Address address = (Address) o;
        return getId() != null && Objects.equals(getId(), address.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
