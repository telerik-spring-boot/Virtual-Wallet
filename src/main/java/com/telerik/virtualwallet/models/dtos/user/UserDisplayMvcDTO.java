package com.telerik.virtualwallet.models.dtos.user;

public class UserDisplayMvcDTO extends UserDisplayDTO{

    private String fullName;
    private boolean isVerified;

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
}
