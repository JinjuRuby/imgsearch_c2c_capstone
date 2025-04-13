package Univ.imgsearch_c2c_capstone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @Column(nullable = false, unique = true)
    private String email; // 이메일을 기본 키로 사용

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int point = 0;

}
