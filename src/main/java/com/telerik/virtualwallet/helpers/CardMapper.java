package com.telerik.virtualwallet.helpers;

import com.telerik.virtualwallet.models.Card;
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
}
