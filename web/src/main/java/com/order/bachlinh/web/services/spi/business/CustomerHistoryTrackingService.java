package com.order.bachlinh.web.services.spi.business;

import com.order.bachlinh.core.security.filter.AuthenticationFilter.CustomerHistoryTrackingEvent;

public interface CustomerHistoryTrackingService {

    void handleCustomerHistoryTrackingEvent(CustomerHistoryTrackingEvent event);

    void syncCustomerHistory();
}
