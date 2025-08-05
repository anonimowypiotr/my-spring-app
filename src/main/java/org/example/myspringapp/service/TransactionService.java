package org.example.myspringapp.service;


import org.example.myspringapp.dto.CashMachineDTO;
import org.example.myspringapp.dto.ExternalTransferDTO;
import org.example.myspringapp.model.Account;
import org.example.myspringapp.model.Transaction;
import org.example.myspringapp.repository.AccountRepository;
import org.example.myspringapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction withdrawal(CashMachineDTO cashMachineDTO) {
        if (cashMachineDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByCardNumber(cashMachineDTO.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));


        if (account.getBalance() < cashMachineDTO.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - cashMachineDTO.getAmount());
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType("WITHDRAWAL");
        transaction.setCardNumber(cashMachineDTO.getCardNumber());
        transaction.setAmount(cashMachineDTO.getAmount());
        transaction.setReceipientAccountNumber(null);
        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction deposit(CashMachineDTO cashMachineDTO) {
        if (cashMachineDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account account = accountRepository.findByCardNumber(cashMachineDTO.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));


        account.setBalance(account.getBalance() + cashMachineDTO.getAmount());
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setType("DEPOSIT");
        transaction.setCardNumber(cashMachineDTO.getCardNumber());
        transaction.setAmount(cashMachineDTO.getAmount());
        transaction.setReceipientAccountNumber(null);
        transaction.setAccount(account);

        return transactionRepository.save(transaction);

    }

    @Transactional
    public Transaction externalTransfer(ExternalTransferDTO externalTransferDTO) {
        if (externalTransferDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        Account senderAccount = accountRepository.findByAccountNumber(externalTransferDTO.getSenderAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (senderAccount.getBalance() < externalTransferDTO.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        senderAccount.setBalance(senderAccount.getBalance() - externalTransferDTO.getAmount());
        accountRepository.save(senderAccount);

        Transaction newTransaction = new Transaction();
        newTransaction.setType("TRANSFER");
        newTransaction.setCardNumber(null);
        newTransaction.setReceipientAccountNumber(externalTransferDTO.getRecipientAccountNumber());
        newTransaction.setAmount(externalTransferDTO.getAmount());
        newTransaction.setAccount(senderAccount);

        return transactionRepository.save(newTransaction);
    }
}
