package com.telerik.virtualwallet.services.card;

import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.User;

import java.util.List;

public interface CardService {

    List<Card> getAllCards();

    List<Card> getCardsByUserId(int userId);

    Card getCardById(int id);

    void addCard(String username, Card card);

    void updateCard(Card card);

    void deleteCard(int cardId);
}
