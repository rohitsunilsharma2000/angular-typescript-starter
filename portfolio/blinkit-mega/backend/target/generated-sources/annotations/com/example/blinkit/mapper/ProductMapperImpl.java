package com.example.blinkit.mapper;

import com.example.blinkit.dto.ProductDtos;
import com.example.blinkit.entity.Product;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-18T20:17:54+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDtos.ProductView toView(Product product) {
        if ( product == null ) {
            return null;
        }

        Long id = null;
        Long categoryId = null;
        String name = null;
        String description = null;
        BigDecimal price = null;

        id = product.getId();
        categoryId = product.getCategoryId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();

        ProductDtos.ProductView productView = new ProductDtos.ProductView( id, categoryId, name, description, price );

        return productView;
    }
}
