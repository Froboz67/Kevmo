package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    UserDao userDao;


    /*
    Method is used to display a list of users that the authenticatied

    Not sure I needed to write this method
     */
    @Override
    public Account getAccountNumberByUserId(int userId) {
        Account account = null;
        final String sql = "SELECT account_id FROM public.account WHERE user_id = ?";

        try {
            final SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        System.out.println("the account # " + account);
        return account;
    }
    @Override
    public Account getAccountListByUserId(int userId) {
        Account account = null;

        final String sql = "SELECT account_id, user_id, balance\n" +
                "\tFROM public.account\n" +
                "\tWHERE user_id = ?";
        try {
           final SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
           if (results.next()) {
               account = mapRowToAccount(results);
           }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        System.out.println(account);
        return account;
    }
    @Override
    public Account getAccountByUserId(int userId, int accountId) {
        Account account = null;

        final String sql = "SELECT account_id, user_id, balance\n" +
                "\tFROM public.account\n" +
                "\tWHERE user_id = ? AND account_id = ?";
        try {
            final SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, accountId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        System.out.println(account);
        return account;
    }
    /* method to get balance to be used in the transfer controller for the user that
    is sending money to another user    */
    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        final String sql = "SELECT balance FROM public.account WHERE user_id = ?";
        BigDecimal balance = null;
        try {
            balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("Error fetching balance for userId: " + userId, e);
        }
        System.out.println("Balance for user " + userId + ": " + balance);
        return balance;
    }

    Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }

}
