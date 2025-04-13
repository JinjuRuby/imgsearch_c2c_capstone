package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.entity.Products;
import Univ.imgsearch_c2c_capstone.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 상품 등록
    @PostMapping("/upload")
    public ResponseEntity<String> uploadProduct(@RequestBody Products product) {
        productRepository.save(product);
        return ResponseEntity.ok("상품이 성공적으로 등록되었습니다.");
    }

    // 모든 상품 조회 (테스트용 또는 목록 표시용)
    @GetMapping("/all")
    public ResponseEntity<List<Products>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
}
