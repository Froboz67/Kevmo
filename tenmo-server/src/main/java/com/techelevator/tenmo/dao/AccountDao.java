package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    /*
    Method is used to display a list of users that the authenticatied
     */
    Account getAccountNumberByUserId(int userId);

    Account getAccountListByUserId(int userId);

    Account getAccountByUserId(int userId, int accountId);


    BigDecimal getBalanceByUserId(int userId);
}
