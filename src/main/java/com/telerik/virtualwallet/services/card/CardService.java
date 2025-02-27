package com.telerik.virtualwallet.services.card;

import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.User;

import java.util.List;

public interface CardService {

    List<Card> getAllCards();

    List<Card> getCardsByUserId(User user, int userId);

    Card getCardById(User user, int id);

    void addCard(User user, Card card);

    void updateCard(User user, Card card);

    void deleteCard(User user, int id);
}
