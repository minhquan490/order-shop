package com.order.bachlinh.core.entities.spi;

import jakarta.persistence.EntityManager;

public interface EntityManagerHolder {

    EntityManager getEntityManager();
}
