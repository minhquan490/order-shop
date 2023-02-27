package com.order.bachlinh.core.entities.repositories;

import com.order.bachlinh.core.entities.model.District;

import java.util.Collection;

public interface DistrictRepository {

    boolean saveAllDistrict(Collection<District> districts);

    long countDistrict();

    Collection<District> getAllDistrict();
}
