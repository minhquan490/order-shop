package com.order.bachlinh.core.entities.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(
        name = "CUSTOMER_HISTORY",
        indexes = @Index(name = "idx_history_customer", columnList = "CUSTOMER_HISTORY_ID"))
@EqualsAndHashCode(callSuper = true)
public class CustomerHistory extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, columnDefinition = "int")
    private Integer id;

    @Column(name = "PATH_REQUEST", length = 50)
    private String pathRequest;

    // Use RequestType enum
    @Column(name = "REQUEST_TYPE", length = 15, nullable = false, updatable = false)
    private String requestType;

    @Column(name = "ENDPOINT_ID", length = 30, updatable = false)
    private String endpointId;

    @Column(name = "REMOVE_DATE", nullable = false)
    private Date removeDate;

    @Column(name = "CUSTOMER_HISTORY_ID", nullable = false, length = 10, updatable = false)
    private String customerId;

    @Override
    public void setId(Object id) {
        if (!(id instanceof Integer)) {
            throw new PersistenceException("Id of history must be int");
        }
        this.id = (Integer) id;
    }

    public void setRequestType(String requestType) {
        RequestType check = RequestType.valueOf(requestType);
        this.requestType = check.name();
    }
}
