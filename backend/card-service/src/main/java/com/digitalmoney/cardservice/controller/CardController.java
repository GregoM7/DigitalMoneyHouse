package com.digitalmoney.cardservice.controller;
import com.digitalmoney.cardservice.entity.Account;
import com.digitalmoney.cardservice.entity.Card;
import com.digitalmoney.cardservice.exception.BadRequestException;
import com.digitalmoney.cardservice.exception.ResourceNotFoundException;
import com.digitalmoney.cardservice.service.interfaces.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/card")
public class CardController {

    private ICardService cardService;

    @Autowired
    public CardController(ICardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/accounts/{accountId}/cards")
    public ResponseEntity<List<Card>> getAllByAccountId(@PathVariable(name = "accountId") Integer accountId) throws BadRequestException, ResourceNotFoundException {

        Optional<List<Card>> optionalCards = cardService.getAllByAccountId(accountId);

        return optionalCards.map(cards -> ResponseEntity.ok().body(cards)).orElseGet(() -> (ResponseEntity<List<Card>>) ResponseEntity.notFound());

    }

    @PostMapping
    public ResponseEntity<Card> create(@RequestBody Card card){
        try {
            Optional<Account> account = cardService.getAccountById(card.getAccountId());
            if (account.isEmpty()) {
                return new ResponseEntity("No existe la cuenta", HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok().body(cardService.create(card));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/accounts/{accountId}/cards/{id}")
    public ResponseEntity delete(@PathVariable Integer accountId, @PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
            Optional<Account> account = cardService.getAccountById(accountId);

            if (account.isEmpty()) {
                return new ResponseEntity("No existe la cuenta", HttpStatus.NOT_FOUND);
            }

            cardService.delete(accountId, id);

            return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts/{accountId}/cards/{id}")
    public ResponseEntity<Card> getCard(@PathVariable Integer accountId, @PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        Optional<Account> account = cardService.getAccountById(accountId);

        if (account.isEmpty()) {
            return new ResponseEntity("No existe la cuenta", HttpStatus.NOT_FOUND);
        }
        cardService.getCard(accountId, id);

        return ResponseEntity.ok(cardService.getCard(accountId, id));
    }

}