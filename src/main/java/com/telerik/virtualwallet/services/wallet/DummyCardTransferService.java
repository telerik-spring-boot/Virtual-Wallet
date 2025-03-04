package com.telerik.virtualwallet.services.wallet;

import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.dtos.wallet.DummyCardWithdrawDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class DummyCardTransferService {

    private final RestTemplate restTemplate;

    @Value("${dummy.bank.url}")
    private String dummyBankUrl;

    public DummyCardTransferService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean successfulCardTransfer(Card card, BigDecimal amount) {

        String url = dummyBankUrl + "/api/dummy-bank/transfer";
        DummyCardWithdrawDTO dummyCardWithdrawDTO = new DummyCardWithdrawDTO(card.getNumber(), amount);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, dummyCardWithdrawDTO, Boolean.class);
        return response.getBody() != null && response.getBody();

    }
}
