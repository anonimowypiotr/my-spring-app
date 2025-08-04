package org.example.myspringapp.service;

import org.example.myspringapp.exception.UserNotFoundException;
import org.example.myspringapp.model.Account;
import org.example.myspringapp.repository.AccountRepository;
import org.example.myspringapp.repository.UserRepository;
import org.example.myspringapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserService {


    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private  AccountService accountService;
    private  PasswordEncoder passwordEncoder;




    @Autowired
    public UserService(UserRepository userRepository ,AccountService accountService,AccountRepository accountRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User getUser(Long id) {
     return  userRepository.findById(id)
             .orElseThrow(UserNotFoundException::new);

    }

    @Transactional
    public User register(User user, String accountType) {

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalArgumentException("User already exists");
        }
        if (userRepository.findByPesel(user.getPesel()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        if(user.getPesel() == null || user.getPesel().length() != 11) {
            throw new IllegalArgumentException("PESEL musi mieć dokładnie 11 cyfr");
        }

        if(user.getPassword().length()<6){
            throw new IllegalArgumentException("Password too short");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPesel(user.getPesel());
        User savedUser = userRepository.save(user);

        Account account = new Account();
        account.setUser(savedUser);
        account.setAccountNumber(accountService.generateAccountNumber());
        account.setBalance(0);
        account.setType(accountType);
        accountRepository.save(account);

        return savedUser;


    }

}
