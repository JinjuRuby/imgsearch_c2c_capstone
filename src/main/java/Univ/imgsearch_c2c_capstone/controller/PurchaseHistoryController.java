package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.entity.PurchaseHistory;
import Univ.imgsearch_c2c_capstone.repository.PurchaseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-history")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PurchaseHistoryController {

    private final PurchaseHistoryRepository purchaseHistoryRepository;

    // ✅ 구매 내역 조회 (purchaseTime 기준 정렬)
    @GetMapping("/buy")
    public ResponseEntity<List<PurchaseHistory>> getMyPurchases(@RequestParam("email") String email) {
        List<PurchaseHistory> purchases = purchaseHistoryRepository.findByBuyerEmailOrderByPurchaseTimeDesc(email);
        return ResponseEntity.ok(purchases);
    }

    // ✅ 판매 내역 조회 (purchaseTime 기준 정렬)
    @GetMapping("/sell")
    public ResponseEntity<List<PurchaseHistory>> getMySales(@RequestParam("email") String email) {
        List<PurchaseHistory> sales = purchaseHistoryRepository.findBySellerEmailOrderByPurchaseTimeDesc(email);
        return ResponseEntity.ok(sales);
    }
}
