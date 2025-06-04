package com.redonion.tcg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
// import java.io.Serializable;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Integer id_user;

    private String nama;
    private String email;
    private String password;
    private String avatar;
    private String about;    private String favorite_tags;
    
    @jakarta.persistence.Column(name = "created_at")
    @jakarta.persistence.Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private java.util.Date createdAt = new java.util.Date();

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public enum UserRole {
        USER,
        ADMIN
    }

    // Getter dan Setter
    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFavorite_tags() {
        return favorite_tags;
    }    public void setFavorite_tags(String favorite_tags) {
        this.favorite_tags = favorite_tags;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }
}