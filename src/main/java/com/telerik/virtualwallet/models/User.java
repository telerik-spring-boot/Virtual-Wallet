package com.telerik.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name="full_name")
    private String fullName;

    @Column(name="username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name="is_blocked")
    private boolean isBlocked;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "users_wallets",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "wallet_id")
    )
    private Set<Wallet> wallets = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Verification verification;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, orphanRemoval = true)
    private List<Stock> stocks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL, orphanRemoval = true)
    private Set<Investment> investments;

    @OneToMany(mappedBy = "referrer", cascade = CascadeType.MERGE)
    private Set<Referral> referredPeople = new HashSet<>();

    @OneToOne(mappedBy = "referee", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Referral referredBy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="main_wallet_id", nullable=false)
    private Wallet mainWallet;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_online")
    private LocalDateTime lastOnline;

    public User() {
    }

    public User(int id, String fullName, String username, String email, String password, String phoneNumber, Set<Wallet> wallets,
                List<Role> roles, Verification verification, boolean isBlocked, LocalDateTime createdAt, LocalDateTime lastOnline,
                Set<Referral> referredPeople) {
        this.id = id;
        this.username = username;
        this.fullName=fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.wallets = wallets;
        this.roles = roles;
        this.verification = verification;
        this.isBlocked = isBlocked;
        this.createdAt  = createdAt;
        this.lastOnline = lastOnline;
        this.referredPeople = referredPeople;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Set<Investment> getInvestments() {
        return investments;
    }

    public void setInvestments(Set<Investment> investments) {
        this.investments = investments;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public void addStock(Stock stock){
        this.stocks.add(stock);
    }

    public void sellStock(Stock stock){
        this.stocks.remove(stock);
    }

    public void addInvestment(Investment investment){
        this.investments.add(investment);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Wallet getMainWallet() {
        return mainWallet;
    }

    public void setMainWallet(Wallet mainWallet) {
        this.mainWallet = mainWallet;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(LocalDateTime lastOnline) {
        this.lastOnline = lastOnline;
    }

    public Set<Referral> getReferredPeople() {
        return referredPeople;
    }

    public void setReferredPeople(Set<Referral> referredPeople) {
        this.referredPeople = referredPeople;
    }

    public void addReferredPerson(Referral referral){
        referredPeople.add(referral);
    }

    public Referral getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Referral referredBy) {
        this.referredBy = referredBy;
    }
}
