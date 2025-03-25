package com.telerik.virtualwallet.models;


import jakarta.persistence.*;

@Entity
@Table(name = "referrals")
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "referrer_id")
    private User referrer;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "referee_id")
    private User referee;


    public Referral() {
    }

    public Referral(int id, User referrer, User referee) {
        this.id = id;
        this.referrer = referrer;
        this.referee = referee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getReferrer() {
        return referrer;
    }

    public void setReferrer(User referrer) {
        this.referrer = referrer;
    }

    public User getReferee() {
        return referee;
    }

    public void setReferee(User referee) {
        this.referee = referee;
    }


}
