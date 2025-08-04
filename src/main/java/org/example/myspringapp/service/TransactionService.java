package org.example.myspringapp.service;


import org.example.myspringapp.model.Account;
import org.example.myspringapp.model.Transaction;
import org.example.myspringapp.repository.AccountRepository;
import org.example.myspringapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }



    public Transaction withdrawal(String cardNumber, Integer amount) {
        if (amount == 0 || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));



        if(account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType("WITHDRAWAL");
        transaction.setCardNumber(cardNumber);
        transaction.setAmount(amount);
        transaction.setReceipientAccountNumber(null);
        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }
    public Transaction deposit(String cardNumber, Integer amount) {
        if (amount == 0 || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));



        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType("DEPOSIT");
        transaction.setCardNumber(cardNumber);
        transaction.setAmount(amount);
        transaction.setReceipientAccountNumber(null);
        transaction.setAccount(account);

        return transactionRepository.save(transaction);

    }

     public Transaction externalTransfer(Transaction transaction,Integer amount,String receipientAccountNumber,Account account) {
        if(amount == 0 || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
         Account senderAccount = accountRepository.findByAccountNumber(account.getAccountNumber())
                 .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if(senderAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        accountRepository.save(senderAccount);

         Transaction newTransaction = new Transaction();
         newTransaction.setType("TRANSFER");
         newTransaction.setCardNumber(senderAccount.getCardNumber());
         newTransaction.setReceipientAccountNumber(receipientAccountNumber);
         newTransaction.setAmount(amount);
         newTransaction.setAccount(senderAccount);

         return transactionRepository.save(newTransaction);


     }




}
