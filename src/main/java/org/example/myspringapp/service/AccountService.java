package org.example.myspringapp.service;

import org.example.myspringapp.model.Account;
import org.example.myspringapp.model.User;
import org.example.myspringapp.repository.AccountRepository;
import org.example.myspringapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository,UserRepository userRepository) {
        this.accountRepository = accountRepository;
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public Account createAccountForUser(String pesel, String type) {
        User user = userRepository.findByPesel(pesel)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setType(type);
        account.setBalance(0);
        account.setAccountNumber(generateAccountNumber());

        return accountRepository.save(account);
    }


    public String generateAccountNumber() {
        return "PL" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 26);
    }

}