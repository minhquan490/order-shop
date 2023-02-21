package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.web.services.spi.business.InvalidateJwtService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
class InvalidateJwtServiceImpl implements InvalidateJwtService {

    private final List<String> invalidateTokens = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void invalidateJwt(String jwt) {
        invalidateTokens.add(jwt);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void clear() {
        if (!invalidateTokens.isEmpty()) {
            invalidateTokens.clear();
        }
    }

    @Override
    public boolean isInvalidate(String jwt) {
        return invalidateTokens.contains(jwt);
    }
}
