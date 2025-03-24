package com.telerik.virtualwallet.services.card;

import com.telerik.virtualwallet.exceptions.DuplicateEntityException;
import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.ExpiredCardException;
import com.telerik.virtualwallet.models.Card;
import com.telerik.virtualwallet.models.User;
import com.telerik.virtualwallet.repositories.card.CardRepository;
import com.telerik.virtualwallet.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private static final String NO_CARDS_MESSAGE = "No cards are found.";
    private static final String EXPIRED_CARD_ERROR_MESSAGE = "Please insert an expiry date in the future.";

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

        return cardRepository.getCardsByUsername(username);
    }

    @Override
    public Card getFirstCardCreatedByUsername(String username) {

        return cardRepository.getFirstCardCreatedByUsername(username);
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

        validateCardExpiryDate(card);

        if (cardRepository.isCardAlreadyAssignedToUser(username, card)) {
            throw new DuplicateEntityException("This card is already saved to your account");
        }

        User user = userRepository.getByUsername(username);

        card.setUser(user);

        cardRepository.addCard(card);

    }

    @Override
    public void updateCard(Card card) {

        validateCardExpiryDate(card);

        if (cardRepository.isCardAlreadyAssignedToUser(card.getUser().getUsername(), card)) {
            throw new DuplicateEntityException("This card is already saved to your account");
        }

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

    private static void validateCardExpiryDate(Card card) {
        int cardYear = Integer.parseInt(card.getExpiryYear());
        int currentYear = LocalDate.now().getYear() % 100;
        if (cardYear < currentYear) {
            throw new ExpiredCardException(EXPIRED_CARD_ERROR_MESSAGE);
        }
        if (cardYear == currentYear) {
            int cardMonth = Integer.parseInt(card.getExpiryMonth());
            int currentMonth = LocalDate.now().getMonthValue();
            if (cardMonth <= currentMonth) {
                throw new ExpiredCardException(EXPIRED_CARD_ERROR_MESSAGE);
            }
        }
    }

}
