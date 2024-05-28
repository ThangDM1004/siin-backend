package com.example.exe202backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CartCleanupScheduler {

    @Autowired
    private CartService cartService;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUpExpiredCarts() {
        cartService.cleanUpExpiredCarts();
    }
}
