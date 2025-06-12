package Univ.imgsearch_c2c_capstone.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/image-search")
public class ImageSearchController {

    @Value("${clip.api.url}")
    private String clipApiUrl;  // ex: http://localhost:8000/api/products/search-by-image

    @PostMapping
    public ResponseEntity<String> searchByImage(@RequestParam("image") MultipartFile file) throws IOException {
        // 1. Multipart 파일을 임시 파일로 저장
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        // 2. 요청 구성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(tempFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 3. FastAPI로 POST 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(clipApiUrl, requestEntity, String.class);

        // 4. 임시 파일 삭제
        tempFile.delete();

        // 5. 결과 반환
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}