package com.telerik.virtualwallet.repositories.card;

import com.telerik.virtualwallet.models.Card;

import java.util.List;

public interface CardRepository {

    List<Card> getAllCards();

    List<Card> getCardsByUserId(int userId);

    Card getCardById(int id);

    void addCard(Card card);

    void updateCard(Card card);

    void deleteCard(int cardId);


}
