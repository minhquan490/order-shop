package com.order.bachlinh.web.component.dto.form;

import com.google.common.base.Objects;

import java.util.Arrays;

public record ProductSearchForm(String productName,
                                String price,
                                String productSize,
                                String color,
                                String enable,
                                String[] categories) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSearchForm that = (ProductSearchForm) o;
        return Objects.equal(productName, that.productName) && Objects.equal(price, that.price) && Objects.equal(productSize, that.productSize) && Objects.equal(color, that.color) && Objects.equal(enable, that.enable) && Objects.equal(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productName, price, productSize, color, enable, categories);
    }

    @Override
    public String toString() {
        return "ProductSearchForm{" +
                "productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                ", productSize='" + productSize + '\'' +
                ", color='" + color + '\'' +
                ", enable='" + enable + '\'' +
                ", categories=" + Arrays.toString(categories) +
                '}';
    }
}
