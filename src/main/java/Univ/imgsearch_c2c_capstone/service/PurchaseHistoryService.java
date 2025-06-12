package Univ.imgsearch_c2c_capstone.service;

import Univ.imgsearch_c2c_capstone.dto.PurchaseHistoryDTO;
import Univ.imgsearch_c2c_capstone.entity.PurchaseHistory;
import Univ.imgsearch_c2c_capstone.repository.PurchaseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository historyRepo;

    // 구매 내역 조회
    public List<PurchaseHistoryDTO> getPurchaseHistoryByBuyer(String email) {
        return historyRepo.findByBuyerEmailOrderByPurchaseTimeDesc(email)
                .stream()
                .map(history -> {
                    PurchaseHistoryDTO dto = new PurchaseHistoryDTO();
                    dto.setId(history.getId());
                    dto.setProductTitle(history.getProductTitle());
                    dto.setPrice(history.getPrice());
                    dto.setSellerEmail(history.getSellerEmail());
                    dto.setBuyerEmail(history.getBuyerEmail());
                    dto.setPurchaseTime(history.getPurchaseTime());
                    dto.setProductImageUrl(history.getProductImageUrl());  // ✅ 이미지 설정
                    return dto;
                })
                .toList();
    }

    // 판매 내역 조회
    public List<PurchaseHistoryDTO> getSalesHistoryBySeller(String email) {
        return historyRepo.findBySellerEmailOrderByPurchaseTimeDesc(email)
                .stream()
                .map(history -> {
                    PurchaseHistoryDTO dto = new PurchaseHistoryDTO();
                    dto.setId(history.getId());
                    dto.setProductTitle(history.getProductTitle());
                    dto.setPrice(history.getPrice());
                    dto.setSellerEmail(history.getSellerEmail());
                    dto.setBuyerEmail(history.getBuyerEmail());
                    dto.setPurchaseTime(history.getPurchaseTime());
                    dto.setProductImageUrl(history.getProductImageUrl());  // ✅ 이미지 설정
                    return dto;
                })
                .toList();
    }
}
