package com.telerik.virtualwallet.services.security;

import com.telerik.virtualwallet.repositories.card.CardRepository;
import org.springframework.stereotype.Service;

@Service
public class CardSecurityService {

    private final CardRepository cardRepository;

    public CardSecurityService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public boolean isUserCardHolder(int cardId, String username) {

        return cardRepository.isUserCardHolder(username, cardId);

    }
}
