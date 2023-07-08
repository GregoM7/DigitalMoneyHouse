package com.digitalmoney.cardservice.service.interfaces;

import com.digitalmoney.cardservice.exception.BadRequestException;
import com.digitalmoney.cardservice.exception.ResourceNotFoundException;
import com.digitalmoney.cardservice.entity.Account;
import com.digitalmoney.cardservice.entity.Card;

import java.util.List;
import java.util.Optional;

public interface ICardService {
    Optional<List<Card>> getAllByAccountId(Integer id) throws BadRequestException, ResourceNotFoundException;
    Card create(Card card);
    void delete(Integer accountId, Integer id) throws BadRequestException, ResourceNotFoundException;
    Optional<Account> getAccountById(Integer accountOriginId);

    Card getCard(Integer accountId, Integer id) throws ResourceNotFoundException;

}