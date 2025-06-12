package Univ.imgsearch_c2c_capstone.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;           // 댓글 ID (수정/삭제 시 사용)
    private Long productId;    // 해당 상품 ID
    private Long parentId;     // 대댓글이면 부모 댓글 ID, 아니면 null
    private String content;    // 댓글 내용
    private String nickname;   // 작성자 닉네임 (users.name에서 가져옴)
    private String email;      // 작성자 이메일 (기본 인증용)
}
