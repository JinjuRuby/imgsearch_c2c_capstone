package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.dto.ProductResponse;
import Univ.imgsearch_c2c_capstone.dto.ProductSimilarity;
import Univ.imgsearch_c2c_capstone.entity.Products;
import Univ.imgsearch_c2c_capstone.entity.Users;
import Univ.imgsearch_c2c_capstone.repository.ProductRepository;
import Univ.imgsearch_c2c_capstone.repository.UserRepository;
import Univ.imgsearch_c2c_capstone.util.CosineSimilarityUtil;
import Univ.imgsearch_c2c_capstone.dto.ProductSimpleResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final String uploadDir = "C:\\Users\\user\\Desktop\\건국대_글로컬\\4학년_2학기\\소프트웨어종합설계\\스냅쇼핑\\imgsearch\\uploads";

    @Value("${python.server.url:http://localhost:8000}")
    private String pythonServerUrl;

    public ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<ProductResponse> getAllProducts() {
        List<Products> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return products.stream().map(ProductResponse::from).toList();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProduct(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
            @RequestParam("email") String email,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
                return ResponseEntity.status(500).body("업로드 디렉토리 생성 실패");
            }

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            File savedFile = new File(uploadFolder, fileName);
            image.transferTo(savedFile);

            EmbeddingAndColor embeddingAndColor = requestEmbeddingAndColor(savedFile);

            Products product = new Products();
            product.setTitle(title);
            product.setDescription(description);
            product.setCategory(category);
            product.setPrice(price);
            product.setEmail(email);
            product.setImagePath(savedFile.getAbsolutePath());
            product.setImageUrl("/images/" + fileName);
            product.setImageEmbedding(embeddingAndColor.embeddingJson);
            product.setColorR(embeddingAndColor.colorR);
            product.setColorG(embeddingAndColor.colorG);
            product.setColorB(embeddingAndColor.colorB);

            productRepository.save(product);

            return ResponseEntity.ok("상품이 성공적으로 등록되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("이미지 업로드 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/category")
    public List<ProductResponse> getProductsByCategory(@RequestParam("value") String category) {
        List<Products> products;
        if (category.equals("전체")) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByCategory(category);
        }
        return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    @GetMapping
    public Page<ProductResponse> getProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findAll(pageable).map(ProductResponse::from);
    }

    @GetMapping("/my")
    public Page<ProductResponse> getMyProducts(
            @RequestParam("email") String email,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findByEmail(email, pageable).map(ProductResponse::from);
    }

    @GetMapping("/by-email-paged")
    public ResponseEntity<Page<ProductResponse>> getProductsByEmailPaged(
            @RequestParam("email") String email,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Products> productsPage = productRepository.findByEmail(email, pageable);
        Page<ProductResponse> responsePage = productsPage.map(ProductResponse::from);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
        Optional<Products> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Products product = optionalProduct.get();
        Optional<Users> optionalUser = userRepository.findByEmail(product.getEmail());
        String sellerName = optionalUser.map(Users::getName).orElse("알 수 없음");
        ProductResponse response = new ProductResponse(
                product.getId(), product.getTitle(), product.getDescription(), product.getPrice(),
                product.getImageUrl(), product.getEmail(), sellerName, product.getCategory()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable("id") Long id, @RequestBody Products updatedProduct) {
        Optional<Products> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }
        Products product = optionalProduct.get();
        product.setTitle(updatedProduct.getTitle());
        product.setPrice(updatedProduct.getPrice());
        product.setDescription(updatedProduct.getDescription());
        product.setCategory(updatedProduct.getCategory());
        productRepository.save(product);
        return ResponseEntity.ok("상품이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        Optional<Products> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }
        Products product = optionalProduct.get();
        String imagePath = product.getImagePath();
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok("상품과 이미지가 성공적으로 삭제되었습니다.");
    }

    @PostMapping(value = "/search-by-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Map<String, Object>>> searchByImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "r", required = false) Integer r,
            @RequestParam(value = "g", required = false) Integer g,
            @RequestParam(value = "b", required = false) Integer bColor
    ) {
        try {
            byte[] imageBytes = image.getBytes();
            ByteArrayResource imageResource = new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", imageResource);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            String url = pythonServerUrl + "/embed";
            ResponseEntity<EmbeddingAndColor> response = restTemplate.postForEntity(url, requestEntity, EmbeddingAndColor.class);
            EmbeddingAndColor embeddingAndColor = response.getBody();

            String queryEmbedding = embeddingAndColor.embeddingJson;
            int queryR = (r != null) ? r : embeddingAndColor.colorR;
            int queryG = (g != null) ? g : embeddingAndColor.colorG;
            int queryB = (bColor != null) ? bColor : embeddingAndColor.colorB;

            List<Products> allProducts = productRepository.findAll();
            List<Map<String, Object>> result = allProducts.stream().map(p -> {
                try {
                    double imageSim = CosineSimilarityUtil.similarity(queryEmbedding, p.getImageEmbedding());
                    double colorSim = calculateColorSimilarity(queryR, queryG, queryB, p.getColorR(), p.getColorG(), p.getColorB());
                    double finalSim = imageSim * 0.5 + colorSim * 0.5;
                    if (finalSim >= 0.7) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", p.getId());
                        map.put("title", p.getTitle());
                        map.put("description", p.getDescription());
                        map.put("price", p.getPrice());
                        map.put("imageUrl", p.getImageUrl());
                        map.put("similarity", finalSim);
                        map.put("simImage", imageSim);
                        map.put("simColor", colorSim);
                        return map;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    return null;
                }
            }).filter(Objects::nonNull).sorted((a, b) -> Double.compare((double) b.get("similarity"), (double) a.get("similarity"))).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private double calculateColorSimilarity(int r1, int g1, int b1, int r2, int g2, int b2) {
        double distance = Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
        double maxDistance = Math.sqrt(255 * 255 * 3);
        return 1 - (distance / maxDistance);
    }

    private EmbeddingAndColor requestEmbeddingAndColor(File imageFile) {
        RestTemplate restTemplate = new RestTemplate();
        String url = pythonServerUrl + "/embed";
        byte[] imageBytes;
        try {
            imageBytes = Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일을 읽을 수 없습니다: " + imageFile.getAbsolutePath());
        }
        ByteArrayResource imageResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return imageFile.getName();
            }
        };
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageResource);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<EmbeddingAndColor> response = restTemplate.postForEntity(url, requestEntity, EmbeddingAndColor.class);
        return response.getBody();
    }

    private static class EmbeddingAndColor {
        public String embeddingJson;
        public Integer colorR;
        public Integer colorG;
        public Integer colorB;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("buyerEmail") String buyerEmail
    ) {
        Optional<Products> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }

        Products product = optionalProduct.get();

        // 구매자 조회
        Optional<Users> optionalBuyer = userRepository.findByEmail(buyerEmail);
        if (optionalBuyer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("구매자 정보를 찾을 수 없습니다.");
        }

        Users buyer = optionalBuyer.get();

        if (buyer.getPoint() < product.getPrice()) {
            return ResponseEntity.badRequest().body("포인트가 부족합니다.");
        }

        // 구매자 포인트 차감
        buyer.setPoint(buyer.getPoint() - product.getPrice());
        userRepository.save(buyer);

        // 판매자 조회
        Optional<Users> optionalSeller = userRepository.findByEmail(product.getEmail());
        if (optionalSeller.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("판매자 정보를 찾을 수 없습니다.");
        }

        Users seller = optionalSeller.get();

        // 판매자 포인트 증가
        seller.setPoint(seller.getPoint() + product.getPrice());
        userRepository.save(seller);

        // (선택) 구매 기록 저장 가능

        return ResponseEntity.ok("구매가 완료되었습니다.");
    }

    @GetMapping("/recommend")
    public List<ProductSimpleResponse> recommendProducts(
            @RequestParam("category") String category,
            @RequestParam("excludeId") Long excludeId
    ) {
        List<Products> list = productRepository.findTop4ByCategoryAndIdNot(category, excludeId);
        return list.stream().map(ProductSimpleResponse::from).toList();
    }

}
