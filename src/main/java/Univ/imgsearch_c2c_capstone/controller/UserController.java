package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.entity.PointHistory;
import Univ.imgsearch_c2c_capstone.entity.Users;
import Univ.imgsearch_c2c_capstone.repository.PointHistoryRepository;
import Univ.imgsearch_c2c_capstone.repository.ProductRepository;
import Univ.imgsearch_c2c_capstone.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "*")
@Transactional
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UserController(UserRepository userRepository,
                          ProductRepository productRepository,
                          PointHistoryRepository pointHistoryRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users users) {
        if (userRepository.findByEmail(users.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 회원입니다.");
        }
        userRepository.save(users);
        return ResponseEntity.ok("회원가입에 성공했습니다");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        return userRepository.findByEmail(email)
                .filter(users -> users.getPassword().equals(password))
                .map(users -> ResponseEntity.ok("로그인에 성공했습니다.|" + users.getName()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 정보가 일치하지 않습니다."));
    }

    // 회원 탈퇴
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email,
                                             @RequestBody Map<String, String> request) {
        String password = request.get("password");

        return userRepository.findByEmail(email)
                .map(user -> {
                    if (!user.getPassword().equals(password)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
                    }
                    productRepository.deleteByEmail(email);
                    userRepository.delete(user);
                    return ResponseEntity.ok("회원 탈퇴가 완료되었습니다. 보유 포인트 및 등록 상품이 모두 삭제되었습니다.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이메일의 회원이 없습니다."));
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email) {
        boolean exists = userRepository.findByEmail(email).isPresent();
        return ResponseEntity.ok(exists);
    }

    // 사용자 이름 조회
    @GetMapping("/name")
    public ResponseEntity<String> getUserName(@RequestParam("email") String email) {
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(user.getName()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다."));
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
    public ResponseEntity<?> addPoint(@RequestParam(name = "email") String email,
                                      @RequestParam(name = "amount") int amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("양의 정수만 가능합니다.");
        }

        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        users.setPoint(users.getPoint() + amount);
        userRepository.save(users);

        PointHistory history = new PointHistory();
        history.setEmail(email);
        history.setAmount(amount);
        history.setDescription("포인트 충전");
        history.setCreatedAt(LocalDateTime.now());
        history.setBalance(users.getPoint()); // ✅ 현재 포인트 기록

        pointHistoryRepository.saveAndFlush(history);

        return ResponseEntity.ok(users.getPoint());
    }

    // 포인트 사용 (예: 상품 구매)
    @PostMapping("/point/use")
    public ResponseEntity<?> usePoint(@RequestParam(name = "email") String email,
                                      @RequestParam(name = "amount") int amount,
                                      @RequestParam(name = "description", defaultValue = "포인트 사용") String description) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("양의 정수만 가능합니다.");
        }

        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (users.getPoint() < amount) {
            return ResponseEntity.badRequest().body("포인트가 부족합니다.");
        }

        users.setPoint(users.getPoint() - amount);
        userRepository.save(users);

        PointHistory history = new PointHistory();
        history.setEmail(email);
        history.setAmount(-amount);
        history.setDescription(description);
        history.setCreatedAt(LocalDateTime.now());
        history.setBalance(users.getPoint()); // ✅ 사용 후 포인트 기록

        pointHistoryRepository.saveAndFlush(history);

        return ResponseEntity.ok(users.getPoint());
    }

    // 포인트 획득 기록 (예: 상품 판매 후)
    @PostMapping("/point/earn")
    public ResponseEntity<?> earnPoint(@RequestParam(name = "email") String email,
                                       @RequestParam(name = "amount") int amount,
                                       @RequestParam(name = "description", defaultValue = "포인트 획득") String description) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("양의 정수만 가능합니다.");
        }

        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        users.setPoint(users.getPoint() + amount);
        userRepository.save(users);

        PointHistory history = new PointHistory();
        history.setEmail(email);
        history.setAmount(amount);
        history.setDescription(description);
        history.setCreatedAt(LocalDateTime.now());
        history.setBalance(users.getPoint()); // ✅ 획득 후 포인트 기록

        pointHistoryRepository.saveAndFlush(history);

        return ResponseEntity.ok(users.getPoint());
    }
}
