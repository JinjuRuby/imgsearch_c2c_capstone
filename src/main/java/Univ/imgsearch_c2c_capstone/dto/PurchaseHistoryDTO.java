package Univ.imgsearch_c2c_capstone.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchaseHistoryDTO {
    private Long id;
    private String productTitle;
    private int price;
    private String sellerEmail;
    private String buyerEmail;
    private LocalDateTime purchaseTime;

    private String productImageUrl; // ✅ 이미지 URL 필드 추가
}
