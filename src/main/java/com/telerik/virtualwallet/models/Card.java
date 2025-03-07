package com.telerik.virtualwallet.models;


import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private int id;

    @Column(name="card_number")
    private String number;

    @Column(name="card_holder")
    private String holder;

    @Column(name="expiry_month")
    private String expiryMonth;

    @Column(name="expiry_year")
    private String expiryYear;

    @Column(name="card_cvv")
    private String cvv;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Card() {
    }

    public Card(int id, String number, String holder, String expiryMonth, String expiryYear, String cvv, User user) {
        this.id = id;
        this.number = number;
        this.holder = holder;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }
}
