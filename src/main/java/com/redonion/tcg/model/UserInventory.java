package com.redonion.tcg.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_inventory")
public class UserInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "id_kartu")
    private Long idKartu;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "acquired_date")
    private LocalDateTime acquiredDate = LocalDateTime.now();    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_kartu", referencedColumnName = "id_kartu", insertable = false, updatable = false)
    private Card card;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Long getIdKartu() {
        return idKartu;
    }

    public void setIdKartu(Long idKartu) {
        this.idKartu = idKartu;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAcquiredDate() {
        return acquiredDate;
    }

    public void setAcquiredDate(LocalDateTime acquiredDate) {
        this.acquiredDate = acquiredDate;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
