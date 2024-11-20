package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferDao transferDao;

    @PostMapping("/transfer/{userId}/{accountFromId}/{accountToId}")
    public ResponseEntity<String> createTransfer(@PathVariable int userId, @PathVariable int accountFromId, @PathVariable int accountToId,
                                                 @RequestBody Transfer transfer, Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());

        System.out.println(accountFromId + "  " + accountToId);

        // tests for authenticated user
        if (user.getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthenticated user");
        }
        // tests for accounts to be different
        if (accountFromId == accountToId) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user can't transfer money to themselves");
        }
        // gets the current balance of the sending account
        BigDecimal currentBalance = accountDao.getBalanceByUserId(userId);

        System.out.println("amount to transfer " + transfer.getAmount());

        // tests to make sure there is enough money to make the transfer
        if (transfer.getAmount().compareTo(currentBalance) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user doesn't have enough tenmo bucks");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(transferDao.createTransfer(transfer, userId, accountFromId, accountToId)));
    }

}
