package Univ.imgsearch_c2c_capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    private String name;       // 상품 이름
    private String description; // 상품 설명
    private int price;         // 상품 가격
    private String imageUrl;   // 이미지 경로 (또는 URL)
    private String email;      // 등록한 사용자 이메일
}
