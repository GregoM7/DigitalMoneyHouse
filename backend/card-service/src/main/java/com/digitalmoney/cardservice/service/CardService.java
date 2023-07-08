package com.digitalmoney.cardservice.service;

import com.digitalmoney.cardservice.exception.BadRequestException;
import com.digitalmoney.cardservice.exception.ResourceNotFoundException;
import com.digitalmoney.cardservice.repository.feign.IAccountFeignRepository;
import com.digitalmoney.cardservice.service.interfaces.ICardService;
import com.digitalmoney.cardservice.entity.Account;
import com.digitalmoney.cardservice.entity.Card;
import com.digitalmoney.cardservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService implements ICardService {
    private CardRepository cardRepository;
    private IAccountFeignRepository iAccountFeignRepository;

    @Autowired
    public CardService(CardRepository cardRepository, IAccountFeignRepository iAccountFeignRepository) {
        this.cardRepository = cardRepository;
        this.iAccountFeignRepository = iAccountFeignRepository;
    }

    @Override
    public Optional<List<Card>> getAllByAccountId(Integer id) throws BadRequestException, ResourceNotFoundException {

        List<Card> cards = cardRepository.getAllByAccountId(id);

        if (cards == null) {
            throw new ResourceNotFoundException("No se encontraton tarjetas en cuentas con este id");
        }

        return Optional.of(cards);
    }

    @Override
    public Card create(Card card) {
        return this.cardRepository.save(card);
    }

    @Override
    public void delete(Integer accountId, Integer id) throws ResourceNotFoundException {
        Account account = iAccountFeignRepository.getAccountById(accountId);
        Optional<Card> card = cardRepository.findById(id);
        if (account != null && card.isPresent()){
            cardRepository.deleteCardByAccountIdAndId(accountId, id);
            return;
        }
        throw new ResourceNotFoundException("No se encontraron tarjetas");
    }

    @Override
    public Optional<Account> getAccountById(Integer accountOriginId) {
        try {
            Account account = iAccountFeignRepository.getAccountById(accountOriginId);
            return Optional.of(account);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public Card getCard(Integer accountId, Integer id) throws ResourceNotFoundException {
        Card card = cardRepository.getCardByAccountIdAndId(accountId, id);
        if (card != null){
            return card;
        }
        throw new ResourceNotFoundException("id de card invalido, no existe o no corresponde a la account con id: "+accountId);
    }
}