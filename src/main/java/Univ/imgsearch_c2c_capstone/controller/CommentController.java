package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.dto.CommentDTO;
import Univ.imgsearch_c2c_capstone.entity.Comment;
import Univ.imgsearch_c2c_capstone.entity.Products;
import Univ.imgsearch_c2c_capstone.entity.Users;
import Univ.imgsearch_c2c_capstone.repository.CommentRepository;
import Univ.imgsearch_c2c_capstone.repository.ProductRepository;
import Univ.imgsearch_c2c_capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo; // ✅ 이름 정리

    @GetMapping
    public List<CommentDTO> getComments(@RequestParam("productId") Long productId) {
        return commentRepo.findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(comment -> {
                    CommentDTO dto = new CommentDTO();
                    dto.setId(comment.getId());
                    dto.setProductId(comment.getProduct().getId()); // ✅ product → productId
                    dto.setParentId(comment.getParentId());
                    dto.setContent(comment.getContent());
                    dto.setEmail(comment.getEmail());

                    Users user = userRepo.findById(comment.getEmail()).orElse(null);
                    dto.setNickname(user != null ? user.getName() : "알 수 없음");

                    return dto;
                })
                .toList();
    }

    @PostMapping("")
    public Comment postComment(@RequestBody CommentDTO dto) {
        Products product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setProduct(product); // ✅ setProductId → setProduct
        comment.setEmail(dto.getEmail());
        comment.setContent(dto.getContent());
        comment.setParentId(dto.getParentId());

        return commentRepo.save(comment);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable("id") Long id, @RequestBody Comment updated) {
        Comment comment = commentRepo.findById(id).orElseThrow();

        if (!comment.getEmail().equals(updated.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정할 수 있습니다.");
        }

        comment.setContent(updated.getContent());
        return commentRepo.save(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable("id") Long id, @RequestParam("email") String email) {
        Comment comment = commentRepo.findById(id).orElseThrow();

        if (!comment.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 삭제할 수 있습니다.");
        }

        commentRepo.deleteById(id);
    }
}
