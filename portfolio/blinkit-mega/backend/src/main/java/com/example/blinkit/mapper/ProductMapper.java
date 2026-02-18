package com.example.blinkit.mapper;

import com.example.blinkit.dto.ProductDtos.ProductView;
import com.example.blinkit.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductView toView(Product product);
}
