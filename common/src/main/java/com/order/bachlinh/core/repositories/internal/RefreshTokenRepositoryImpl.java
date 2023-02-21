package com.order.bachlinh.core.repositories.internal;

import com.order.bachlinh.core.entities.model.RefreshToken;
import com.order.bachlinh.core.entities.model.RefreshToken_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.AbstractRepository;
import com.order.bachlinh.core.repositories.RefreshTokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class RefreshTokenRepositoryImpl extends AbstractRepository<RefreshToken, String> implements RefreshTokenRepository {

    @Autowired
    public RefreshTokenRepositoryImpl(ApplicationContext applicationContext) {
        this(RefreshToken.class, applicationContext.getBean(EntityManager.class), applicationContext.getBean(SessionFactory.class), applicationContext.getBean(EntityFactory.class));
    }

    private RefreshTokenRepositoryImpl(Class<RefreshToken> domainClass, EntityManager em, SessionFactory sessionFactory, EntityFactory entityFactory) {
        super(domainClass, em, sessionFactory, entityFactory);
    }

    @Override
    public RefreshToken getRefreshToken(String token) {
        Specification<RefreshToken> spec = Specification.where(((root, query, criteriaBuilder) -> {
            query.multiselect(root.get(RefreshToken_.REFRESH_TOKEN_VALUE), root.get(RefreshToken_.CUSTOMER), root.get(RefreshToken_.TIME_EXPIRED));
            root.join("customer", JoinType.INNER);
            return criteriaBuilder.equal(root.get(RefreshToken_.REFRESH_TOKEN_VALUE), token);
        }));
        return get(spec);
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public RefreshToken saveRefreshToken(RefreshToken token) {
        return this.save(token);
    }

    @Override
    @Transactional(propagation = MANDATORY, isolation = READ_COMMITTED)
    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        return saveRefreshToken(refreshToken);
    }

    @Override
    @Transactional(propagation = MANDATORY)
    public boolean deleteRefreshToken(RefreshToken refreshToken) {
        if (StringUtils.hasText((CharSequence) refreshToken.getId())) {
            long numRowDeleted = this.delete(Specification.where((root, query, builder) -> builder.equal(root.get("id"), refreshToken.getId())));
            return numRowDeleted != 0;
        } else {
            return false;
        }
    }
}
