package com.order.bachlinh.web.services.internal;

import com.order.bachlinh.core.entities.model.Category;
import com.order.bachlinh.core.entities.model.Product;
import com.order.bachlinh.core.entities.model.Product_;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.ProductRepository;
import com.order.bachlinh.core.exception.ResourceNotFoundException;
import com.order.bachlinh.web.component.dto.form.ProductForm;
import com.order.bachlinh.web.component.dto.form.ProductSearchForm;
import com.order.bachlinh.web.component.dto.resp.ProductResp;
import com.order.bachlinh.core.component.search.SearchEngine;
import com.order.bachlinh.core.entities.repositories.CategoryRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService, ProductSearchingService {
    private final ProductRepository productRepository;
    private final EntityFactory entityFactory;
    private final CategoryRepository categoryRepository;
    private final SearchEngine searchEngine;

    @Override
    public ProductResp getProductByName(String productName) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(Product_.NAME, productName);
        Product product = productRepository.getProductByCondition(conditions);
        return ProductResp.toDto(product);
    }

    @Override
    public ProductResp getProductById(String productId) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put(Product_.ID, productId);
        Product product = productRepository.getProductByCondition(conditions);
        return ProductResp.toDto(product);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProductResp saveProduct(ProductForm form) {
        return ProductResp.toDto(productRepository.saveProduct(toProduct(form)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ProductResp updateProduct(ProductForm form) {
        if (getProductById(form.id()) == null) {
            throw new ResourceNotFoundException("Product not exist");
        }
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
    public Page<ProductResp> productList(Pageable pageable) {
        Map<String, Object> conditions = new HashMap<>();
        Page<Product> products = productRepository.getProductsByCondition(conditions, pageable);
        return products.map(ProductResp::toDto);
    }

    @Override
    public Page<ProductResp> searchProduct(ProductSearchForm form, Pageable pageable) {
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
        conditions = conditions.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return productRepository.getProductsByCondition(conditions, pageable).map(ProductResp::toDto);
    }

    @Override
    public Page<ProductResp> getProductsWithId(Collection<Object> ids) {
        Pageable pageable = Pageable.ofSize(ids.size());
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("IDS", ids);
        return productRepository.getProductsByCondition(conditions, pageable).map(ProductResp::toDto);
    }

    @Override
    public Page<ProductResp> search(ProductSearchForm form, Pageable pageable) {
        return searchProduct(form, pageable);
    }

    @Override
    public Page<ProductResp> fullTextSearch(ProductSearchForm form, Pageable pageable) {
        Collection<String> productIds = searchEngine.searchIds(Product.class, form.productName());
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("IDS", productIds);
        conditions.put(Product_.COLOR, form.color());
        conditions.put(Product_.PRICE, form.price());
        conditions.put(Product_.SIZE, form.productSize());
        conditions.put(Product_.ENABLED, true);
        if (form.categories() != null) {
            List<Category> categories = Arrays.stream(form.categories()).map(categoryRepository::getCategoryByName).toList();
            conditions.put(Product_.CATEGORIES, categories);
        }
        conditions = conditions.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return productRepository.getProductsByCondition(conditions, pageable).map(ProductResp::toDto);
    }

    private Product toProduct(ProductForm form) {
        Product product = entityFactory.getEntity(Product.class);
        product.setId(form.id());
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
