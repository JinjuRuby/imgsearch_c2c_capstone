package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.entity.Users;
import Univ.imgsearch_c2c_capstone.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")  // Next.js 포트 맞게 설정
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users users) {
        if (userRepository.findByEmail(users.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 회원입니다.");
        }

        userRepository.save(users);
        return ResponseEntity.ok("회원가입 성공!");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        return userRepository.findByEmail(email)
                .filter(users -> users.getPassword().equals(password))
                .map(users -> ResponseEntity.ok("로그인 성공!|" + users.getName()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 정보가 일치하지 않습니다."));
    }

    // 회원 탈퇴
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email,
                                             @RequestBody Map<String, String> request) {
        String password = request.get("password");

        return userRepository.findByEmail(email)
                .map(users -> {
                    if (!users.getPassword().equals(password)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("비밀번호가 일치하지 않습니다.");
                    }

                    userRepository.delete(users);
                    return ResponseEntity.ok("회원 탈퇴 되었습니다");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("해당 이메일의 회원이 없습니다."));
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public String checkEmail(@RequestParam("email") String email) {
        boolean exists = userRepository.findByEmail(email).isPresent();
        return exists ? "이미 존재하는 이메일입니다." : "사용 가능한 이메일입니다.";
    }

    // 포인트 조회
    @GetMapping("/point")
    public ResponseEntity<?> getPoint(@RequestParam("email") String email) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(users.getPoint());
    }


    // 포인트 충전
    @PostMapping("/point")
    public ResponseEntity<?> addPoint(@RequestParam(name = "email") String email, @RequestParam(name = "amount") int amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("양의 정수만 가능합니다.");
        }

        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        users.setPoint(users.getPoint() + amount);
        userRepository.save(users);

        return ResponseEntity.ok(users.getPoint());
    }



}
