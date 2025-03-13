package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.dtos.card.CardCreateDTO;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.services.card.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardMapper {

    private static final List<String> CARD_TYPES = List.of("Visa", "Mastercard", "Amex");

    private final CardService cardService;

    @Autowired
    public CardMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public CardDisplayDTO cardToCardDisplayDTO(Card card) {

        CardDisplayDTO dto = new CardDisplayDTO();

        dto.setId(card.getId());
        dto.setCardHolder(card.getHolder().toUpperCase());
        dto.setExpiryMonth(card.getExpiryMonth());
        dto.setExpiryYear(card.getExpiryYear());
        dto.setCardNumberFull(card.getNumber());
        dto.setCvv(card.getCvv());
        dto.setCardNumber("**** **** **** " + card.getNumber().substring(card.getNumber().length() - 4));
        System.out.println("test");
        int sumOfDigits = card.getNumber().chars().map(c -> c - '0').sum();

        dto.setType(CARD_TYPES.get(sumOfDigits % CARD_TYPES.size()));

        return dto;

    }

    public Card createDtoToCard(CardCreateDTO dto) {

        Card card = new Card();

        card.setNumber(dto.getCardNumber());
        card.setExpiryMonth(dto.getExpiryMonth());
        card.setExpiryYear(dto.getExpiryYear());
        card.setCvv(dto.getCvv());
        card.setHolder(dto.getCardHolderName());

        return card;

    }

    public Card createDtoToCard(int cardId, CardCreateDTO dto) {

        Card card = cardService.getCardById(cardId);

        if (dto.getCardHolderName() != null) {
            card.setHolder(dto.getCardHolderName());
        }

        if (dto.getCardNumber() != null) {
            card.setNumber(dto.getCardNumber());
        }

        if (dto.getExpiryMonth() != null) {
            card.setExpiryMonth(dto.getExpiryMonth());
        }

        if (dto.getExpiryYear() != null) {
            card.setExpiryYear(dto.getExpiryYear());
        }

        if (dto.getCvv() != null) {
            card.setCvv(dto.getCvv());
        }

        return card;
    }
}
