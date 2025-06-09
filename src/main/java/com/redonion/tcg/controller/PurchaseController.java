package com.redonion.tcg.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redonion.tcg.model.Card;
import com.redonion.tcg.model.Transaction;
import com.redonion.tcg.model.User;
import com.redonion.tcg.model.UserInventory;
import com.redonion.tcg.repository.CardRepository;
import com.redonion.tcg.repository.TransactionRepository;
import com.redonion.tcg.repository.UserInventoryRepository;
import com.redonion.tcg.service.UserService;

@RestController
@RequestMapping("/api")
public class PurchaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserInventoryRepository userInventoryRepository;

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseCard(@RequestBody Map<String, Object> request) {
        try {            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || "anonymousUser".equals(auth.getName())) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }            User user = userService.findByUsername(auth.getName());
            Long cardId = ((Number) request.get("cardId")).longValue();
            String paymentMethod = request.get("paymentMethod").toString();
            int quantity = Integer.parseInt(request.get("quantity").toString());

            // Get card details
            Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NoSuchElementException("Card not found"));

            // Calculate total price (card.getHarga() returns BigDecimal)
            BigDecimal totalPrice = card.getHarga().multiply(BigDecimal.valueOf(quantity));

            // Create transaction record
            Transaction transaction = new Transaction();
            transaction.setIdUser(user.getId_user());
            transaction.setIdKartu(cardId);
            transaction.setQuantity(quantity);
            transaction.setTotalPrice(totalPrice);
            transaction.setPaymentMethod(paymentMethod);
            transaction.setStatus("COMPLETED");
            transactionRepository.save(transaction);

            // Update or create user inventory record
            UserInventory inventory = userInventoryRepository.findByIdUserAndIdKartu(user.getId_user(), cardId)
                .orElse(new UserInventory());
            if (inventory.getId() == null) {
                inventory.setIdUser(user.getId_user());
                inventory.setIdKartu(cardId);
                inventory.setQuantity(quantity);
            } else {
                inventory.setQuantity(inventory.getQuantity() + quantity);
            }
            userInventoryRepository.save(inventory);            // Success response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Purchase successful");            response.put("transactionId", transaction.getId());
            return ResponseEntity.ok(response);        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid number format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid argument: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @GetMapping("/inventory")
    public ResponseEntity<?> getUserInventory() {
        try {            // Get current user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || "anonymousUser".equals(auth.getName())) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }

            String username = auth.getName();
            User user = userService.findByUsername(username);
            return ResponseEntity.ok(userInventoryRepository.findByIdUser(user.getId_user()));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to get inventory: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }
}
