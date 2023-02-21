package com.order.bachlinh.web.dto.resp;

import com.google.common.base.Objects;
import com.order.bachlinh.core.entities.model.Category;
import com.order.bachlinh.core.entities.model.Product;
import com.order.bachlinh.core.entities.model.ProductMedia;

import java.util.Arrays;

public record ProductDto(String id,
                         String name,
                         String price,
                         String size,
                         String color,
                         String taobaoUrl,
                         String description,
                         String[] pictures,
                         String[] categories) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equal(id, that.id) && Objects.equal(name, that.name) && Objects.equal(price, that.price) && Objects.equal(size, that.size) && Objects.equal(color, that.color) && Objects.equal(taobaoUrl, that.taobaoUrl) && Objects.equal(description, that.description) && Objects.equal(pictures, that.pictures) && Objects.equal(categories, that.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, price, size, color, taobaoUrl, description, pictures, categories);
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", taobaoUrl='" + taobaoUrl + '\'' +
                ", description='" + description + '\'' +
                ", pictures=" + Arrays.toString(pictures) +
                ", categories=" + Arrays.toString(categories) +
                '}';
    }

    public static ProductDto toDto(Product product) {
        return new ProductDto(
                (String) product.getId(),
                product.getName(),
                String.valueOf(product.getPrice()),
                product.getSize(),
                product.getColor(),
                product.getTaobaoUrl(),
                product.getDescription(),
                (String[]) product.getPictures().stream().map(ProductMedia::getUrl).toArray(),
                (String[]) product.getCategories().stream().map(Category::getName).toArray()
        );
    }
}
