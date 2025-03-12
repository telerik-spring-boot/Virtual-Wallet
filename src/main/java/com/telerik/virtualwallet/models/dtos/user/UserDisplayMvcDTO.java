package com.telerik.virtualwallet.models.dtos.user;

import java.time.LocalDateTime;

public class UserDisplayMvcDTO extends UserDisplayDTO{

    private String fullName;

    private boolean isVerified;

    private LocalDateTime createdAt;

    private LocalDateTime lastOnline;

    private boolean isAdmin;


    public UserDisplayMvcDTO(){}

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
