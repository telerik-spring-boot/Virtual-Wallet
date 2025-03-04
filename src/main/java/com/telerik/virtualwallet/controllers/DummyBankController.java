package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.models.dtos.wallet.DummyCardWithdrawDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/dummy-bank")
public class DummyBankController {

    @PostMapping("/transfer")
    public ResponseEntity<Boolean> transferFunds(@RequestBody DummyCardWithdrawDTO request) {

        boolean transferIsSuccessful = getRandomBooleanWith80PercentChance();
        return ResponseEntity.ok(transferIsSuccessful);

    }

    public static boolean getRandomBooleanWith80PercentChance() {

        Random random = new Random();
        return random.nextDouble() < 0.8;

    }
}
