package Univ.imgsearch_c2c_capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본키

    private String title;       // 상품 이름
    private String description; // 상품 설명
    private int price;          // 상품 가격
    private String email;       // 등록한 사용자 이메일

    private String imageUrl;    // 이미지 전체 URL (옵션)

    @Column(name = "image_path")
    private String imagePath;   // 이미지 파일 경로

    @Column(nullable = false)
    private String category;

    @Lob
    @Column(name = "image_embedding", columnDefinition = "LONGTEXT")
    private String imageEmbedding;   // 이미지 임베딩 (JSON 문자열로 저장)

    // ✅ 색깔(RGB) 추가
    private Integer colorR;     // 빨간색 (0~255)
    private Integer colorG;     // 초록색 (0~255)
    private Integer colorB;     // 파란색 (0~255)

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
