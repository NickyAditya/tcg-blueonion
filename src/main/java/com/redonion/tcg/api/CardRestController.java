package com.redonion.tcg.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.redonion.tcg.model.Card;
import com.redonion.tcg.repository.CardRepository;

@RestController
@RequestMapping("/api/cards")
public class CardRestController {
    @Autowired
    private CardRepository cardRepository;

    @PostMapping
    public ResponseEntity<?> addCard(
            @RequestParam String nama,
            @RequestParam BigDecimal harga,
            @RequestParam String deskripsi,
            @RequestParam String rarity,
            @RequestParam String kondisi,
            @RequestParam int stok,
            @RequestParam int id_kategori,
            @RequestParam("gambar") MultipartFile gambar
    ) {
        try {
            String uploadDir = "src/main/resources/static/textures";
            String fileName = gambar.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, gambar.getBytes());

            Card card = new Card();
            card.setNama(nama);
            card.setHarga(harga);
            card.setDeskripsi(deskripsi);
            card.setRarity(rarity);
            card.setKondisi(kondisi);
            card.setStok(stok);
            card.setId_kategori(id_kategori);
            card.setGambar(fileName);
            card.setId_user(1); // default admin

            cardRepository.save(card);

            return ResponseEntity.ok(card);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Gagal upload gambar");
        }
    }

    @GetMapping
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }
}
