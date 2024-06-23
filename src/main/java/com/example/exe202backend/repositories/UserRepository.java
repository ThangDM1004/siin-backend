package com.example.exe202backend.repositories;

import com.example.exe202backend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    @Query("SELECT u FROM UserModel u WHERE u.cart.id = :cartId")
    Optional<UserModel> findUserByCartId(Long cartId);
}
