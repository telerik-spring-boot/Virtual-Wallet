package com.telerik.virtualwallet.controllers;

import com.telerik.virtualwallet.helpers.CardMapper;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.dtos.card.CardCreateDTO;
import com.telerik.virtualwallet.models.dtos.card.CardDisplayDTO;
import com.telerik.virtualwallet.services.card.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    public ResponseEntity<CardDisplayDTO> addCardToUserByUsername(@RequestBody CardCreateDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Card cardToAdd = cardMapper.createDtoToCard(dto);
        cardService.addCard(auth.getName(), cardToAdd);

        return ResponseEntity.ok(cardMapper.cardToCardDisplayDTO(cardService.getCardById(cardToAdd.getId())));

    }

    @PutMapping("/{cardId}")
    @PreAuthorize("@cardSecurityService.isUserCardHolder(#cardId, authentication.name)")
    public ResponseEntity<CardDisplayDTO> updateCardFromUser(@PathVariable int cardId,
                                                             @RequestBody CardCreateDTO dto) {

        Card card = cardMapper.createDtoToCard(cardId, dto);

        cardService.updateCard(card);

        return ResponseEntity.ok(cardMapper.cardToCardDisplayDTO(cardService.getCardById(cardId)));
    }

    @DeleteMapping("/{cardId}")
    @PreAuthorize("@cardSecurityService.isUserCardHolder(#cardId, authentication.name) ")
    public ResponseEntity<String> removeCardFromUser(@PathVariable int cardId) {

        cardService.deleteCard(cardId);

        return ResponseEntity.ok(String.format("Card with id %d successfully removed.", cardId));
    }

}
