package com.example.exe202backend.repositories;

import com.example.exe202backend.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
