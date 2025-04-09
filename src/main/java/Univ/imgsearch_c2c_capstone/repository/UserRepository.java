package Univ.imgsearch_c2c_capstone.repository;

import Univ.imgsearch_c2c_capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}

