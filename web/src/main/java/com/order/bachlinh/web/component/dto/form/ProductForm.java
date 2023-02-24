package com.order.bachlinh.web.component.dto.form;

import com.google.common.base.Objects;

import java.util.Arrays;

public record ProductForm(String name,
                          String price,
                          String size,
                          String color,
                          String taobaoUrl,
                          String description,
                          String enabled,
                          String[] categories) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductForm that = (ProductForm) o;
        return Objects.equal(name, that.name) && Objects.equal(price, that.price) && Objects.equal(size, that.size) && Objects.equal(color, that.color) && Objects.equal(taobaoUrl, that.taobaoUrl) && Objects.equal(description, that.description) && Objects.equal(enabled, that.enabled) && Objects.equal(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, price, size, color, taobaoUrl, description, enabled, categories);
    }

    @Override
    public String toString() {
        return "ProductForm{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", taobaoUrl='" + taobaoUrl + '\'' +
                ", description='" + description + '\'' +
                ", enabled='" + enabled + '\'' +
                ", categories=" + Arrays.toString(categories) +
                '}';
    }
}
