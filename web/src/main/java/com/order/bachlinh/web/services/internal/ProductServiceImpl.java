package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.core.entities.model.Category;
import com.order.bachlinh.core.entities.model.Product;
import com.order.bachlinh.core.entities.model.Product_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.ProductRepository;
import com.order.bachlinh.web.component.dto.form.ProductForm;
import com.order.bachlinh.web.component.dto.form.ProductSearchForm;
import com.order.bachlinh.web.component.dto.resp.ProductDto;
import com.order.bachlinh.core.component.search.SearchEngine;
import com.order.bachlinh.core.repositories.CategoryRepository;
import com.order.bachlinh.web.services.spi.business.ProductSearchingService;
import com.order.bachlinh.web.services.spi.common.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService, ProductSearchingService {
    private final ProductRepository productRepository;
    private final EntityFactory entityFactory;
    private final CategoryRepository categoryRepository;
    private final SearchEngine searchEngine;

    @Override
    public ProductDto getProductByName(String productName) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(Product_.NAME, productName);
        Product product = productRepository.getProductByCondition(conditions);
        return ProductDto.toDto(product);
    }

    @Override
    public ProductDto getProductById(String productId) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(Product_.ID, productId);
        Product product = productRepository.getProductByCondition(conditions);
        return ProductDto.toDto(product);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProductDto saveProduct(ProductForm form) {
        return ProductDto.toDto(productRepository.saveProduct(toProduct(form)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProductDto updateProduct(ProductForm form) {
        return saveProduct(form);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteProduct(String productId) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(Product_.ID, productId);
        Product product = productRepository.getProductByCondition(conditions);
        if (productRepository.deleteProduct(product)) {
            return "Delete success";
        } else {
            return "Delete failure";
        }
    }

    @Override
    public Page<ProductDto> productList(Pageable pageable) {
        Map<String, Object> conditions = new HashMap<>();
        Page<Product> products = productRepository.getProductsByCondition(conditions, pageable);
        return products.map(ProductDto::toDto);
    }

    @Override
    public Page<ProductDto> searchProduct(ProductSearchForm form, Pageable pageable) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(Product_.NAME, form.productName());
        conditions.put(Product_.PRICE, Integer.parseInt(form.price()));
        conditions.put(Product_.SIZE, form.productSize());
        conditions.put(Product_.COLOR, form.color());
        conditions.put(Product_.ENABLED, Boolean.parseBoolean(form.enable()));
        if (form.categories() != null) {
            List<Category> categories = Arrays.stream(form.categories()).map(categoryRepository::getCategoryByName).toList();
            conditions.put(Product_.CATEGORIES, categories);
        }
        return productRepository.getProductsByCondition(conditions, pageable).map(ProductDto::toDto);
    }

    @Override
    public Page<ProductDto> getProductsWithId(Collection<Object> ids) {
        Pageable pageable = Pageable.ofSize(ids.size());
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("IDS", ids);
        return productRepository.getProductsByCondition(conditions, pageable).map(ProductDto::toDto);
    }

    @Override
    public Page<ProductDto> search(ProductSearchForm form, Pageable pageable) {
        return searchProduct(form, pageable);
    }

    @Override
    public Page<ProductDto> fullTextSearch(ProductSearchForm form, Pageable pageable) {
        Collection<String> productIds = searchEngine.searchIds(Product.class, form.productName());
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("IDS", productIds);
        return productRepository.getProductsByCondition(conditions, pageable).map(ProductDto::toDto);
    }

    private Product toProduct(ProductForm form) {
        Product product = entityFactory.getEntity(Product.class);
        product.setName(form.name());
        product.setPrice(Integer.parseInt(form.price()));
        product.setColor(form.color());
        product.setTaobaoUrl(form.taobaoUrl());
        product.setDescription(form.description());
        product.setEnabled(Boolean.parseBoolean(form.enabled()));
        List<Category> categories = Arrays.stream(form.categories()).map(categoryName -> {
            Category category = categoryRepository.getCategoryByName(categoryName);
            category.getProducts().add(product);
            return category;
        }).toList();
        product.setCategories(new HashSet<>(categories));
        return product;
    }
}
