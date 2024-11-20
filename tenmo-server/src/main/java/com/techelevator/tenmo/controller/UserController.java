package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/usernames/{userId}")
    public ResponseEntity<List> getUsernames(@PathVariable int userId, Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());
        if (user.getId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonList("unauthenticated user"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userDao.getUsernames(userId, principal));
    }

}
