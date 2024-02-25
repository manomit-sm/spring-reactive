package com.bsolz.productservice.utils;

import com.bsolz.productservice.dtos.ProductDto;
import com.bsolz.productservice.entities.Product;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getDescription(), product.getPrices());
    }

    public static Product toEntity(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
