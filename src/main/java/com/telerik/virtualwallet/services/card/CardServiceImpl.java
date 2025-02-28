package com.telerik.virtualwallet.services.card;

import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.UnauthorizedOperationException;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.repositories.card.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static final String UNAUTHORIZED_MESSAGE = "This card is not associated with your account.";
    private static final String NO_CARDS_FOUND_MESSAGE = "No cards associated with user with id %d found.";
    private static final String NO_CARDS_MESSAGE = "No cards are found.";

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getAllCards() {

        List<Card> allCards = cardRepository.getAllCards();

        if (allCards.isEmpty()) {
            throw new EntityNotFoundException(NO_CARDS_MESSAGE);
        }

        return allCards;
    }

    @Override
    public List<Card> getCardsByUserId(User user, int userId) {

        if (user.getId() != userId) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MESSAGE);
        }

        List<Card> cards = cardRepository.getCardsByUserId(userId);

        if (cards.isEmpty()) {
            throw new EntityNotFoundException(String.format(NO_CARDS_FOUND_MESSAGE, userId));
        }

        return cards;
    }

    @Override
    public Card getCardById(User user, int id) {

        Card card = cardRepository.getCardById(id);

        if (card == null) {
            throw new EntityNotFoundException("Card", "id", id);
        }

        if (isUserNotCardHolder(user, card)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MESSAGE);
        }

        return card;

    }

    @Override
    public void addCard(User user, Card card) {

        if (isUserNotCardHolder(user, card)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MESSAGE);
        }

        cardRepository.addCard(card);

    }

    @Override
    public void updateCard(User user, Card card) {

        if (isUserNotCardHolder(user, card)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MESSAGE);
        }

        cardRepository.updateCard(card);

    }

    @Override
    public void deleteCard(User user, int id) {


        Card cardToDelete = cardRepository.getCardById(id);

        if (cardToDelete == null) {
            throw new EntityNotFoundException("Card", "id", id);
        }

        if (isUserNotCardHolder(user, cardToDelete)) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_MESSAGE);
        }

        cardRepository.deleteCard(id);

    }

    private static boolean isUserNotCardHolder(User user, Card card) {

        return card.getUser().getId() != user.getId();

    }

}
