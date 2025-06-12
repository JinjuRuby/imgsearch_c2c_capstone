package Univ.imgsearch_c2c_capstone.repository;

import Univ.imgsearch_c2c_capstone.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    List<Products> findByEmail(String email); // ✅ 사용자 이메일로 상품 조회
    List<Products> findByCategory(String category);
    List<Products> findTop4ByCategoryAndIdNot(String category, Long excludeId);
    long deleteByEmail(String email);  // 이메일 기준 삭제

    Page<Products> findByEmail(String email, Pageable pageable);
}
