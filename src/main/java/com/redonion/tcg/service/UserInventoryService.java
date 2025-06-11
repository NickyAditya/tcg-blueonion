package com.redonion.tcg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redonion.tcg.model.UserInventory;
import com.redonion.tcg.repository.UserInventoryRepository;

@Service
public class UserInventoryService {
    
    @Autowired
    private UserInventoryRepository userInventoryRepository;    public List<UserInventory> getUserInventory(int userId) {
        return userInventoryRepository.findByIdUser(userId);
    }
}
