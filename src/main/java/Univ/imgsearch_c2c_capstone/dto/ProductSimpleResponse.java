package Univ.imgsearch_c2c_capstone.dto;

import Univ.imgsearch_c2c_capstone.entity.Products;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSimpleResponse {
    private Long id;
    private String title;
    private int price;
    private String imageUrl;
    private String category;

    public static ProductSimpleResponse from(Products product) {
        return new ProductSimpleResponse(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory()
        );
    }
}
