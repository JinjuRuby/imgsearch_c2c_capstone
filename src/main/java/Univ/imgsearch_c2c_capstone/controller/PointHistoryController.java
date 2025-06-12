package Univ.imgsearch_c2c_capstone.controller;

import Univ.imgsearch_c2c_capstone.entity.PointHistory;
import Univ.imgsearch_c2c_capstone.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/point-history")
@RequiredArgsConstructor
public class PointHistoryController {

    private final PointHistoryRepository pointHistoryRepo;

    @GetMapping
    public List<PointHistory> getPointHistory(@RequestParam("email") String email) {
        return pointHistoryRepo.findByEmailOrderByCreatedAtDesc(email);
    }
}
