package Univ.imgsearch_c2c_capstone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String productImageUrl;

    private String buyerEmail;
    private String sellerEmail;
    private Long productId;
    private String productTitle;
    private int price;
    private LocalDateTime purchaseTime;
}
