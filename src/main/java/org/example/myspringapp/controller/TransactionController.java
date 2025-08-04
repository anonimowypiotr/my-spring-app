package org.example.myspringapp.controller;


import org.example.myspringapp.model.Account;
import org.example.myspringapp.model.Transaction;
import org.example.myspringapp.repository.TransactionRepository;
import org.example.myspringapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    public TransactionService transactionService;

    @GetMapping("/withdraw")
    public String withdraw(@RequestParam String cardNumber, @RequestParam Integer amount) {
        transactionService.withdrawal(cardNumber, amount);
        return "Wypłata wykonana!";
    }

    @GetMapping("/deposit")
    public String deposit(@RequestParam String cardNumber, @RequestParam Integer amount) {
        transactionService.deposit(cardNumber, amount);
        return "Wpłata wykonana!";
    }

    @GetMapping("/external-transfer")
    public String externalTransfer(@RequestParam String senderAccountNumber, @RequestParam String receipientAccountNumber, @RequestParam Integer amount) {
        Account senderAccount = new Account();
        senderAccount.setAccountNumber(senderAccountNumber);
        Transaction transaction = new Transaction();
        transactionService.externalTransfer(transaction, amount, receipientAccountNumber, senderAccount);
        return "Przelew wykonany!";
    }


}


