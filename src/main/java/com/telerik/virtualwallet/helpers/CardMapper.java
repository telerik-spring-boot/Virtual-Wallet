package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.dtos.card.CardCreateDTO;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardDisplayDTO cardToCardDisplayDTO(Card card) {

        CardDisplayDTO dto = new CardDisplayDTO();

        dto.setCardHolder(card.getHolder());
        dto.setExpiryMonth(card.getExpiryMonth());
        dto.setExpiryYear(card.getExpiryYear());
        dto.setCardNumber("********" + card.getNumber().substring(card.getNumber().length() - 4));

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
}
