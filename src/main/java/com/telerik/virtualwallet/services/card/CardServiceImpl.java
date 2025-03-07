package com.telerik.virtualwallet.services.card;

import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.repositories.card.CardRepository;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static final String NO_CARDS_FOUND_MESSAGE = "No cards associated with %s found.";
    private static final String NO_CARDS_MESSAGE = "No cards are found.";

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
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
    public List<Card> getCardsByUsername(String username) {

        List<Card> cards = cardRepository.getCardsByUsername(username);

        if (cards.isEmpty()) {
            throw new EntityNotFoundException(String.format(NO_CARDS_FOUND_MESSAGE, username));
        }

        return cards;
    }

    @Override
    public Card getCardById(int id) {

        Card card = cardRepository.getCardById(id);

        if (card == null) {
            throw new EntityNotFoundException("Card", "id", id);
        }

        return card;

    }

    @Override
    public void addCard(String username, Card card) {

        User user = userRepository.getByUsername(username);

        card.setUser(user);

        cardRepository.addCard(card);

    }

    @Override
    public void updateCard(Card card) {

        cardRepository.updateCard(card);

    }

    @Override
    public void deleteCard(int id) {


        Card cardToDelete = cardRepository.getCardById(id);

        if (cardToDelete == null) {
            throw new EntityNotFoundException("Card", "id", id);
        }

        cardRepository.deleteCard(id);

    }

}
