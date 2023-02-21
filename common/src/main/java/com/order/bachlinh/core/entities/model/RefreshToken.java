package com.order.bachlinh.core.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order.bachlinh.core.annotation.Label;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "REFRESH_TOKEN", indexes = @Index(name = "idx_token_value", columnList = "VALUE"))
@Getter
@Setter
@Label("RFT-")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class RefreshToken extends AbstractEntity {

    @Id
    @Column(name = "ID", unique = true, nullable = false, columnDefinition = "varchar(10)")
    @JsonIgnore
    private Object id;

    @Column(name = "TIME_CREATED", nullable = false)
    private Timestamp timeCreated;

    @Column(name = "TIME_EXPIRED", nullable = false)
    private Timestamp timeExpired;

    @Column(name = "VALUE", nullable = false, unique = true, updatable = false, length = 100)
    private String refreshTokenValue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", unique = true, nullable = false, updatable = false)
    @JsonIgnore
    private Customer customer;

    public void setId(Object id) {
        if (id instanceof String casted) {
            this.id = casted;
        }
        throw new PersistenceException("Id of refresh token is only string");
    }
}
