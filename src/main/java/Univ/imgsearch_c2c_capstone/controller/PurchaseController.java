/*
package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.dto.PurchaseRequest;
import Univ.imgsearch_c2c_capstone.entity.Products;
import Univ.imgsearch_c2c_capstone.entity.PurchaseHistory;
import Univ.imgsearch_c2c_capstone.entity.Users;
import Univ.imgsearch_c2c_capstone.repository.ProductRepository;
import Univ.imgsearch_c2c_capstone.repository.PurchaseHistoryRepository;
import Univ.imgsearch_c2c_capstone.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PurchaseController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public PurchaseController(ProductRepository productRepository, UserRepository userRepository, PurchaseHistoryRepository purchaseHistoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseProduct(@RequestBody PurchaseRequest request) {
        Optional<Products> optionalProduct = productRepository.findById(request.getProductId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }
        Products product = optionalProduct.get();

        Optional<Users> optionalBuyer = userRepository.findByEmail(request.getEmail());
        if (optionalBuyer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("구매자를 찾을 수 없습니다.");
        }
        Users buyer = optionalBuyer.get();

        if (product.getEmail().equals(buyer.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("본인이 등록한 상품은 구매할 수 없습니다.");
        }

        if (buyer.getPoint() < product.getPrice()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("포인트가 부족합니다.");
        }

        Optional<Users> optionalSeller = userRepository.findByEmail(product.getEmail());
        if (optionalSeller.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("판매자를 찾을 수 없습니다.");
        }
        Users seller = optionalSeller.get();

        buyer.setPoint(buyer.getPoint() - product.getPrice());
        seller.setPoint(seller.getPoint() + product.getPrice());
        userRepository.save(buyer);
        userRepository.save(seller);

        PurchaseHistory history = new PurchaseHistory();
        history.setBuyerEmail(buyer.getEmail());
        history.setSellerEmail(seller.getEmail());
        history.setProductId(product.getId());
        history.setProductTitle(product.getTitle());
        history.setPurchaseTime(LocalDateTime.now());
        history.setPrice(product.getPrice());
        history.setProductImageUrl(product.getImageUrl());
        purchaseHistoryRepository.save(history);

        productRepository.delete(product);

        return ResponseEntity.ok("구매가 완료되었습니다.");
    }
}
*/








package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.dto.PurchaseRequest;
import Univ.imgsearch_c2c_capstone.entity.Products;
import Univ.imgsearch_c2c_capstone.entity.PurchaseHistory;
import Univ.imgsearch_c2c_capstone.entity.Users;
import Univ.imgsearch_c2c_capstone.entity.PointHistory;
import Univ.imgsearch_c2c_capstone.repository.ProductRepository;
import Univ.imgsearch_c2c_capstone.repository.PurchaseHistoryRepository;
import Univ.imgsearch_c2c_capstone.repository.UserRepository;
import Univ.imgsearch_c2c_capstone.repository.PointHistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class PurchaseController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PurchaseController(ProductRepository productRepository,
                              UserRepository userRepository,
                              PurchaseHistoryRepository purchaseHistoryRepository,
                              PointHistoryRepository pointHistoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseProduct(@RequestBody PurchaseRequest request) {
        Optional<Products> optionalProduct = productRepository.findById(request.getProductId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }
        Products product = optionalProduct.get();

        Optional<Users> optionalBuyer = userRepository.findByEmail(request.getEmail());
        if (optionalBuyer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("구매자를 찾을 수 없습니다.");
        }
        Users buyer = optionalBuyer.get();

        if (product.getEmail().equals(buyer.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("본인이 등록한 상품은 구매할 수 없습니다.");
        }

        if (buyer.getPoint() < product.getPrice()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("포인트가 부족합니다.");
        }

        Optional<Users> optionalSeller = userRepository.findByEmail(product.getEmail());
        if (optionalSeller.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("판매자를 찾을 수 없습니다.");
        }
        Users seller = optionalSeller.get();

        int price = product.getPrice();

        // ✅ 포인트 차감 및 적립
        buyer.setPoint(buyer.getPoint() - price);
        seller.setPoint(seller.getPoint() + price);
        userRepository.save(buyer);
        userRepository.save(seller);

        // ✅ 포인트 내역 기록 (구매자)
        PointHistory buyerHistory = new PointHistory();
        buyerHistory.setEmail(buyer.getEmail());
        buyerHistory.setAmount(-price);
        buyerHistory.setDescription("상품 구매: " + product.getTitle());
        buyerHistory.setCreatedAt(LocalDateTime.now());
        buyerHistory.setBalance(buyer.getPoint()); // 현재 잔액
        pointHistoryRepository.save(buyerHistory);

        // ✅ 포인트 내역 기록 (판매자)
        PointHistory sellerHistory = new PointHistory();
        sellerHistory.setEmail(seller.getEmail());
        sellerHistory.setAmount(price);
        sellerHistory.setDescription("상품 판매: " + product.getTitle());
        sellerHistory.setCreatedAt(LocalDateTime.now());
        sellerHistory.setBalance(seller.getPoint()); // 현재 잔액
        pointHistoryRepository.save(sellerHistory);

        // ✅ 구매 내역 저장
        PurchaseHistory history = new PurchaseHistory();
        history.setBuyerEmail(buyer.getEmail());
        history.setSellerEmail(seller.getEmail());
        history.setProductId(product.getId());
        history.setProductTitle(product.getTitle());
        history.setProductImageUrl(product.getImageUrl());
        history.setPrice(price);
        history.setPurchaseTime(LocalDateTime.now());
        purchaseHistoryRepository.save(history);

        // ✅ 상품 삭제
        productRepository.delete(product);

        return ResponseEntity.ok("구매가 완료되었습니다.");
    }
}
