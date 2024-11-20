package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

   @Autowired
    private AccountDao accountDao;
   @Autowired
    private UserDao userDao;
    /*
    this method gets a list of accounts but I'm not sure it will be needed
     */
   @GetMapping("/account/{userId}")
    public ResponseEntity<String> getAccountListByUserId(@PathVariable int userId, Principal principal) {
       User user = userDao.getUserByUsername(principal.getName());

       if (user.getId() != userId) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthenticated user");
       }
       return ResponseEntity.status((HttpStatus.OK)).body(String.valueOf(accountDao.getAccountListByUserId(user.getId())));
   }
    /*
    this method gets an account object by the userId provided
     */
    @GetMapping("/account/{userId}/{accountId}")
    public ResponseEntity<String> getAccountByUserId(@PathVariable int userId, @PathVariable int accountId, Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());

        if (user.getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthenticated user");
        }
        return ResponseEntity.status((HttpStatus.OK)).body(String.valueOf(accountDao.getAccountByUserId(user.getId(), accountId)));
    }
}
