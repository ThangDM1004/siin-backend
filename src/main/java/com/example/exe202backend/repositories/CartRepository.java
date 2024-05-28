package com.example.exe202backend.repositories;

import com.example.exe202backend.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findBySessionId(String sessionId);
    List<Cart> findByExpirationTimeBefore(LocalDateTime dateTime);
}
