package com.order.bachlinh.web.services.spi.business;

public interface InvalidateJwtService {

    void invalidateJwt(String jwt);

    void clear();

    boolean isInvalidate(String jwt);
}
