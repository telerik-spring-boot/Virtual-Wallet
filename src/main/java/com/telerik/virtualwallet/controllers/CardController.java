package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.CardMapper;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.services.card.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;

    public CardController(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }


    @GetMapping("/{cardId}")
    @PreAuthorize("hasRole('ADMIN') OR " +
            "@cardSecurityService.isUserCardHolder(#cardId, authentication.name)")
    public ResponseEntity<CardDisplayDTO> getCard(@PathVariable int cardId) {

        Card card = cardService.getCardById(cardId);

        return ResponseEntity.ok(cardMapper.cardToCardDisplayDTO(card));

    }

}
