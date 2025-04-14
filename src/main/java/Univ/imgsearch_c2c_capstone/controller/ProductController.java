package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.entity.Products;
import Univ.imgsearch_c2c_capstone.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    // 저장 경로 설정 (application.properties에서 설정 가능)
    @Value("${upload.dir:uploads}")
    private String uploadDir;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ 상품 등록 (이미지 포함)
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadProduct(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
            @RequestParam("email") String email,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            // uploads 폴더 없으면 생성
            File uploadFolder = new File(System.getProperty("user.dir") + File.separator + uploadDir);
            if (!uploadFolder.exists()) {
                boolean created = uploadFolder.mkdirs();
                if (!created) {
                    return ResponseEntity.status(500).body("업로드 디렉토리 생성 실패");
                }
            }

            // 고유한 파일 이름 생성
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            File savedFile = new File(uploadFolder, fileName);

            // 실제 파일 저장
            image.transferTo(savedFile);

            // 상품 객체 생성 후 저장
            Products product = new Products();
            product.setTitle(title);
            product.setDescription(description);
            product.setPrice(price);
            product.setEmail(email);
            product.setImagePath(savedFile.getAbsolutePath()); // 로컬 파일 경로
            product.setImageUrl("/images/" + fileName); // 클라이언트 접근용 URL

            productRepository.save(product);
            return ResponseEntity.ok("상품이 성공적으로 등록되었습니다.");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("이미지 업로드 중 오류 발생: " + e.getMessage());
        }
    }

    // ✅ 전체 상품 목록 조회
    @GetMapping("/all")
    public ResponseEntity<List<Products>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
}
