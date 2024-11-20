package com.techelevator.tenmo.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    UserDao userDao;
    AccountDao accountDao;

    @Override
    public Transfer getTransfer(int userId, int transferId) {
        Transfer transfer = null;
        final String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount\n" +
                "\tFROM public.transfer\n" +
                "\tWHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("unable to connect to server or database", e);
        }
        return transfer;
    }

    @Override
    public Transfer createTransfer(Transfer transfer, int userId, int transferAccountFrom,
                                   int transferAccountTo) {
        System.out.println(transferAccountFrom + "   " + transferAccountTo);
        Transfer transfer1 = null;
        final String sql = "INSERT INTO public.transfer(\n" +
                "\ttransfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "\tVALUES (?, ?, ?, ?, ?)\n" +
                "\tRETURNING transfer_id;";
        try {
           Integer transferId = jdbcTemplate.queryForObject(sql, int.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(),
                   transfer.getAccountTo(), transfer.getAmount());
           if (transferId != null) {
               transfer1 = getTransfer(userId, transferId);

           } else {
               System.out.println("Transfer Id was returned null");
           }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        System.out.println("here is the transfer " + transfer1);
        return transfer1;
    }

    Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

}
