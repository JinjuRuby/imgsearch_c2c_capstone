package Univ.imgsearch_c2c_capstone.repository;

import Univ.imgsearch_c2c_capstone.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByEmailOrderByCreatedAtDesc(String email);
}
