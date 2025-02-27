package com.telerik.virtualwallet.models;


import jakarta.persistence.*;

@Entity
@Table(name = "verifications")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="email_verified")
    private boolean emailVerified;

    @Column(name="pictures_verified")
    private boolean picturesVerified;

    public Verification() {};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPicturesVerified() {
        return picturesVerified;
    }

    public void setPicturesVerified(boolean picturesVerified) {
        this.picturesVerified = picturesVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
