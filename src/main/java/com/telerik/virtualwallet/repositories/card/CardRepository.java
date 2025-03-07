package com.telerik.virtualwallet.repositories.card;

import com.telerik.virtualwallet.models.Card;

import java.util.List;

public interface CardRepository {

    List<Card> getAllCards();

    List<Card> getCardsByUsername(String username);

    Card getCardById(int id);

    void addCard(Card card);

    void updateCard(Card card);

    void deleteCard(int cardId);

    boolean isUserCardHolder(String username, int cardId);


}
