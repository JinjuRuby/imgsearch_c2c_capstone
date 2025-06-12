package Univ.imgsearch_c2c_capstone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private int amount;

    private String description;

    private LocalDateTime createdAt;

    private int balance;  // 💡 잔액 필드 추가

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
