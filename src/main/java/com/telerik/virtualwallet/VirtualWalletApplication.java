package com.telerik.virtualwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VirtualWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualWalletApplication.class, args);
    }

}
