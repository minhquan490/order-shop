package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.core.entities.model.Customer;
import com.order.bachlinh.core.entities.model.CustomerHistory;
import com.order.bachlinh.core.entities.model.RequestType;
import com.order.bachlinh.core.entities.model.Role;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.CustomerHistoryRepository;
import com.order.bachlinh.core.repositories.CustomerRepository;
import com.order.bachlinh.core.security.filter.AuthenticationFilter;
import com.order.bachlinh.web.services.spi.business.CustomerHistoryTrackingService;
import com.order.bachlinh.web.services.spi.business.HistoryClearService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;

@Service
@Log4j2
class CustomerHistoryTrackingImpl implements CustomerHistoryTrackingService, HistoryClearService {
    private static final int REMOVAL_POLICY_YEAR = 1;
    private final CustomerHistoryRepository customerHistoryRepository;
    private final CustomerRepository customerRepository;
    private final EntityFactory entityFactory;

    @Autowired
    CustomerHistoryTrackingImpl(ApplicationContext context) {
        this.customerHistoryRepository = context.getBean(CustomerHistoryRepository.class);
        this.entityFactory = context.getBean(EntityFactory.class);
        this.customerRepository = context.getBean(CustomerRepository.class);
    }

    @Override
    @EventListener(classes = AuthenticationFilter.CustomerHistoryTrackingEvent.class)
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomerHistoryTrackingEvent(AuthenticationFilter.CustomerHistoryTrackingEvent event) {
        Customer customer = (Customer) event.getSource();
        if (customer.getRole().equals(Role.ADMIN.name())) {
            return;
        }
        CustomerHistory history = entityFactory.getEntity(CustomerHistory.class);
        history.setCustomerId((String) customer.getId());
        history.setPathRequest(event.getUrl());
        String[] paths = event.getUrl().split("/");
        RequestType requestType = determineRequest(paths[1]);
        history.setRequestType(requestType.name());
        if (!requestType.equals(RequestType.NONE)) {
            history.setEndpointId(paths[paths.length - 1]);
        }
        history.setRemoveDate(calculateDateRemoval());
        customerHistoryRepository.saveCustomerHistory(history);
    }

    @Override
    public void syncCustomerHistory() {
        // TODO implement later
    }

    private RequestType determineRequest(String request) {
        if (request.contains("search")) {
            return RequestType.SEARCH;
        } else if (request.contains("category")) {
            return RequestType.CATEGORY;
        } else if (request.contains("product")) {
            return RequestType.PRODUCT;
        } else {
            return RequestType.NONE;
        }
    }

    private Date calculateDateRemoval() {
        LocalDate now = LocalDate.now();
        int year = now.getYear() + REMOVAL_POLICY_YEAR;
        return Date.valueOf(LocalDate.of(year, now.getMonth(), now.getDayOfMonth()));
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void clearHistory() {
        Collection<CustomerHistory> histories = customerHistoryRepository.getHistoriesExpireNow(Date.valueOf(LocalDate.now()));
        if (histories.isEmpty()) {
            return;
        }
        if (!customerHistoryRepository.deleteAll(histories)) {
            log.warn("Delete histories failure");
        }
    }
}
