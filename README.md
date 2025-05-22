# 🃏 RO-TCG (Red Onion Trading Card Game Marketplace)

RO-TCG adalah aplikasi web marketplace untuk menampilkan dan menjual kartu koleksi dari berbagai kategori seperti Pokémon, Yu-Gi-Oh!, dan Magic: The Gathering. Dibangun dengan Spring Boot dan Thymeleaf, proyek ini memungkinkan pengguna untuk menelusuri koleksi kartu secara interaktif, dengan modal popup untuk melihat detail kartu secara visual dan dinamis.

## ✨ Fitur Unggulan

- 🔍 **Browse kartu berdasarkan kategori** (Pokémon, Yu-Gi-Oh!, MTG)
- 📸 **Popup modal interaktif** untuk melihat detail kartu: gambar besar, deskripsi, kondisi, rarity, stok, dan harga
- 💫 **Efek visual 3D dan animasi** untuk rarity seperti *Shiny Ultra Rare* dan *Extremely Rare*
- 📁 Upload gambar kartu langsung ke folder `uploads/images/`
- 📦 Stok real-time dari database
- 💬 UI responsif dan modern dengan styling CSS custom

## 🛠️ Teknologi yang Digunakan

- **Spring Boot** - Backend Framework
- **Thymeleaf** - Template Engine
- **HTML/CSS/JavaScript** - Frontend
- **VanillaTilt.js** - Efek 3D pada kartu
- **MySQL** (atau database lain) - Penyimpanan data kartu
- **Java** - Bahasa utama backend
  


## 🚀 Cara Menjalankan

1. **Clone repo ini:**

```bash
https://github.com/NickyAditya/tcg-blueonion.git
cd ro-tcg

````

2. **Setup database (misalnya MySQL)**

Buat database dan sesuaikan `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/redoinon_tcg
spring.datasource.username=root
spring.datasource.password=(password jika menggunakan jika tidak kosongkan)
```

3. **Jalankan aplikasi:**

```bash
./mvnw spring-boot:run
```

4. **Akses di browser:**

```
http://localhost:8080/
```

## 🖼️ Tampilan UI
-
-

## 🔮 Rarity Special Effects

* `Shiny Ultra Rare` — Efek holografik mengkilap dengan kilatan
* `Extremely Rare` — Menyala-nyala dengan animasi api berdenyut
---

### 🔗 Made with ❤️ by Red Onion Dev
