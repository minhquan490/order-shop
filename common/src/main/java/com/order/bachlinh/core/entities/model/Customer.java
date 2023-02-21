package com.order.bachlinh.core.entities.model;

import com.order.bachlinh.core.annotation.Label;
import com.order.bachlinh.core.annotation.Validator;
import com.order.bachlinh.core.entities.spi.validator.CustomerValidator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@Entity
@Label("CSR-")
@Table(
        name = "CUSTOMER",
        indexes = {
                @Index(name = "idx_customer_username", columnList = "USER_NAME", unique = true),
                @Index(name = "idx_customer_phone", columnList = "PHONE_NUMBER", unique = true),
                @Index(name = "idx_customer_email", columnList = "EMAIL", unique = true)
        }
)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Validator(validators = CustomerValidator.class)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "customer")
@EqualsAndHashCode(callSuper = true)
public class Customer extends AbstractEntity implements UserDetails {

    @Id
    @Column(name = "ID", unique = true, nullable = false, columnDefinition = "varchar(10)")
    private String id;

    @Column(name = "USER_NAME", unique = true, nullable = false, length = 24)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "FIRST_NAME", nullable = false, length = 36)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 36)
    private String lastName;

    @Column(name = "PHONE_NUMBER", nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 32)
    private String email;

    @Column(name = "GENDER", nullable = false, length = 8)
    private String gender;

    @Column(name = "ROLE", nullable = false, length = 10)
    private String role;

    @Column(name = "ACTIVATED", nullable = false, columnDefinition = "BIT")
    private boolean activated = false;

    @Column(name = "ACCOUNT_NON_EXPIRED", nullable = false, columnDefinition = "BIT")
    private boolean accountNonExpired = true;

    @Column(name = "ACCOUNT_NON_LOCKED", nullable = false, columnDefinition = "BIT")
    private boolean accountNonLocked = true;

    @Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false, columnDefinition = "BIT")
    private boolean credentialsNonExpired = true;

    @Column(name = "ENABLED", nullable = false, columnDefinition = "BIT")
    private boolean enabled = true;

    @OneToOne(optional = false, mappedBy = "customer", fetch = FetchType.LAZY)
    private Cart cart;

    @OneToOne(optional = false, mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
    private Set<Address> addresses;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
    private Set<Order> orders;

    public void setId(Object id) {
        if (id instanceof String casted) {
            this.id = casted;
        }
        throw new PersistenceException("Id of customer is only string");
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role));
    }
}
