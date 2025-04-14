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
    private Long id;  // 기본키

    private String title;       // 상품 이름 (프론트엔드와 일치)
    private String description; // 상품 설명
    private int price;          // 상품 가격
    private String email;       // 등록한 사용자 이메일

    private String imageUrl;    // 이미지 전체 URL이 필요한 경우 (옵션)

    @Column(name = "image_path")
    private String imagePath;   // 이미지 파일 경로 (예: "/images/uploads/img.jpg")
}
