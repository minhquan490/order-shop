package com.order.bachlinh.core.entities.repositories;

import com.order.bachlinh.core.annotation.IdentifierGenerator;
import com.order.bachlinh.core.annotation.Validated;
import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.spi.EntityManagerHolder;
import com.order.bachlinh.core.entities.spi.HintDecorator;
import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.NonNull;

import java.lang.reflect.ParameterizedType;

@Log4j2
public abstract class AbstractRepository<T extends BaseEntity, ID> extends SimpleJpaRepository<T, ID> implements HintDecorator, EntityManagerHolder {
    private SessionFactory sessionFactory;
    private EntityFactory entityFactory;
    private EntityManager entityManager;
    private Class<T> type;
    private boolean useCache;

    private AbstractRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    @SuppressWarnings("unchecked")
    protected AbstractRepository(Class<T> domainClass, EntityManager em, SessionFactory sessionFactory, EntityFactory entityFactory) {
        this(domainClass, em);
        this.sessionFactory = sessionFactory;
        this.entityFactory = entityFactory;
        this.entityManager = em;
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.useCache = type.isAnnotationPresent(Cacheable.class) && type.isAnnotationPresent(Cache.class);
    }

    @Override
    public void applyCacheQueryHints(TypedQuery<?> query, String region) {
        query.setHint(HibernateHints.HINT_CACHEABLE, true);
        query.setHint(HibernateHints.HINT_CACHE_REGION, region);
        query.setHint("jakarta.persistence.cache.storeMode", CacheStoreMode.REFRESH);
        query.setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected EntityFactory getEntityFactory() {
        return entityFactory;
    }

    protected T get(Specification<T> spec) {
        TypedQuery<T> query = getQuery(spec, type, Sort.unsorted());
        query.setMaxResults(2);
        T result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            log.info("No result found !");
            result = null;
        }
        return result;
    }

    @Override
    @NonNull
    protected <S extends T> TypedQuery<S> getQuery(Specification<S> spec, @NonNull Class<S> domainClass, @NonNull Sort sort) {
        TypedQuery<S> query = super.getQuery(spec, domainClass, sort);
        if (isUseCache()) {
            applyCacheQueryHints(query, getEntityFactory().getEntityContext(domainClass).getCacheRegion());
        }
        return query;
    }

    @Override
    @IdentifierGenerator
    @Validated(targetIndex = 0)
    @NonNull
    public <S extends T> S save(@NonNull S entity) {
        return super.save(entity);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    protected boolean isUseCache() {
        return useCache;
    }
}
