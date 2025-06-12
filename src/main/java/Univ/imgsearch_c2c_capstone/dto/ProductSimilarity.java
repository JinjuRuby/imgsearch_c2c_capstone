package Univ.imgsearch_c2c_capstone.dto;

import Univ.imgsearch_c2c_capstone.entity.Products;

public class ProductSimilarity {
    public Products product;
    public double similarity;   // 종합 유사도
    public double simImage;     // 이미지 유사도
    public double simColor;     // 색상 유사도

    public ProductSimilarity(Products product, double similarity, double simImage, double simColor) {
        this.product = product;
        this.similarity = similarity;
        this.simImage = simImage;
        this.simColor = simColor;
    }
}
