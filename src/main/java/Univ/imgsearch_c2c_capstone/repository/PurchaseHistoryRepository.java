package Univ.imgsearch_c2c_capstone.repository;

import Univ.imgsearch_c2c_capstone.entity.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByBuyerEmailOrderByPurchaseTimeDesc(String buyerEmail);   // ✅ 수정됨
    List<PurchaseHistory> findBySellerEmailOrderByPurchaseTimeDesc(String sellerEmail); // ✅ 수정됨
}