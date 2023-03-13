package com.order.bachlinh.processor;

import com.order.bachlinh.processor.spi.GenericController;

public class TestController implements GenericController<String, Integer, Integer> {

    @Override
    public String invoke(Integer target, Integer param) {
        return null;
    }
}
