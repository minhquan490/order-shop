package com.order.bachlinh.core.entities.repositories;

import com.order.bachlinh.core.entities.model.ProductMedia;

public interface ProductMediaRepository {
    ProductMedia loadMedia(int id);
}