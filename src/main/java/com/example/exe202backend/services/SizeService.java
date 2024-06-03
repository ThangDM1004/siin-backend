package com.example.exe202backend.services;

import com.example.exe202backend.repositories.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SizeService {
    @Autowired
    private SizeRepository sizeRepository;
}
