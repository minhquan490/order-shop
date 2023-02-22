package com.order.bachlinh.core.repositories;

import com.order.bachlinh.core.entities.model.Province;

import java.util.Collection;

public interface ProvinceRepository {

    boolean saveAllProvinces(Collection<Province> provinces);

    long countProvince();

    Collection<Province> getAllProvinces();
}
