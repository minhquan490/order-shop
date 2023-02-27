package com.order.bachlinh.core.entities.repositories;

import com.order.bachlinh.core.entities.model.Ward;

import java.util.Collection;

public interface WardRepository {

    boolean saveAllWard(Collection<Ward> wards);

    long countAllWards();

    Collection<Ward> getAllWards();
}
