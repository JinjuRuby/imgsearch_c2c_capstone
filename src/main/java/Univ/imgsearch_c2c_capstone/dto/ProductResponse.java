package Univ.imgsearch_c2c_capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import Univ.imgsearch_c2c_capstone.entity.Products;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private int price;
    private String imageUrl;

    private String sellerEmail;  // 상품 등록자의 이메일
    private String sellerName;   // ✅ 판매자의 이름 (Users 테이블에서 조회)
    private String category;

    public static ProductResponse from(Products product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getEmail(),
                null,
                product.getCategory()

        );
    }
}
