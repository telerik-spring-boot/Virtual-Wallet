package com.telerik.virtualwallet.services.card;

import com.telerik.virtualwallet.models.Card;

import java.util.List;

public interface CardService {

    List<Card> getAllCards();

    List<Card> getCardsByUsername(String username);

    Card getFirstCardCreatedByUsername(String username);

    Card getCardById(int id);

    void addCard(String username, Card card);

    void updateCard(Card card);

    void deleteCard(int cardId);
}
