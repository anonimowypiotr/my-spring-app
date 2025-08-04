package org.example.myspringapp.controller;


import org.example.myspringapp.model.Account;
import org.example.myspringapp.model.User;
import org.example.myspringapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phoneNumber,
            @RequestParam  LocalDate birthDate,
            @RequestParam String accountType,
            @RequestParam String pesel) {


        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setBirthDate(birthDate);
        user.setPesel(pesel);


        Account account = new Account();
        account.setType(accountType);


        userService.register(user, accountType);

        return ResponseEntity.ok("Użytkownik zarejestrowany pomyślnie");
    }
}
