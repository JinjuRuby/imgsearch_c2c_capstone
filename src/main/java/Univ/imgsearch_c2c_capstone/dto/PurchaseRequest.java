package Univ.imgsearch_c2c_capstone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {
    private String email;
    private Long productId;
}
