package com.telerik.virtualwallet.services.email;

public interface EmailService {

    void send(String receiver, String title, String content);

}
