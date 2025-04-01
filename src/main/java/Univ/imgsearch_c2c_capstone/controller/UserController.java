package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.entity.User;
import Univ.imgsearch_c2c_capstone.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:8000")  // Next.js 포트 맞게 설정
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 회원입니다.");
        }

        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공!");
    }


    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        return userRepository.findByEmailAndPassword(email, password)
                .map(u -> "로그인 성공!")
                .orElse("로그인 실패: 정보가 일치하지 않습니다.");
    }

    // 회원 탈퇴
    @DeleteMapping("/delete/{email}")
    public String deleteUser(@PathVariable("email") String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    userRepository.delete(user);
                    return "회원 탈퇴 되었습니다";
                })
                .orElse("해당 이메일의 회원이 없습니다.");
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public String checkEmail(@RequestParam("email") String email) {
        boolean exists = userRepository.findByEmail(email).isPresent();
        return exists ? "이미 존재하는 이메일입니다." : "사용 가능한 이메일입니다.";
    }



}
