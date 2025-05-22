package com.redonion.tcg.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.redonion.tcg.model.Card;
import com.redonion.tcg.repository.CardRepository;

@Controller
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/add")
    public String addCard(
            @RequestParam String nama,
            @RequestParam BigDecimal harga,
            @RequestParam String deskripsi,
            @RequestParam String rarity,
            @RequestParam String kondisi,
            @RequestParam int stok,
            @RequestParam int id_kategori,
            @RequestParam("gambar") MultipartFile gambar,
            @RequestParam int id_user) {
        try {
            if (gambar == null || gambar.isEmpty()) {
                // Bisa tangani kalau gambar kosong, misal return error atau set gambar default
                return "error"; // contoh sederhana
            }

            // Simpan gambar ke folder uploads/images/
            String uploadDir = "src/main/resources/static/textures";

            String fileName = gambar.getOriginalFilename();

            // Optional: Validasi fileName untuk keamanan

            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, gambar.getBytes());

            // Simpan data ke database
            Card card = new Card();
            card.setNama(nama);
            card.setHarga(harga);
            card.setDeskripsi(deskripsi);
            card.setRarity(rarity);
            card.setKondisi(kondisi);
            card.setStok(stok);
            card.setId_kategori(id_kategori);
            card.setId_user(id_user);
            card.setGambar(fileName);

            cardRepository.save(card);

            return "redirect:/"; // sesuaikan redirect-nya
        } catch (IOException e) {
            return "error";
        }
    }

    @GetMapping("/form")
    public String showForm() {
        return "tambah-kartu";
    }

    @GetMapping("/pokemon")
    public String showPokemonCards(Model model) {
        model.addAttribute("cards", cardRepository.findAll());
        return "pokemon";
    }

    @GetMapping("/yugioh")
    public String showYugiohCards(Model model) {
        model.addAttribute("cards", cardRepository.findAll());
        return "yugioh";
    }

    @GetMapping("/mtg")
    public String showMtgCards(Model model) {
        model.addAttribute("cards", cardRepository.findAll());
        return "mtg";
    }

}