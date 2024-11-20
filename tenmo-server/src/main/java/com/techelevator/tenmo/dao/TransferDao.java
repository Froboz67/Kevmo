package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface TransferDao {

    Transfer getTransfer(int userId, int transferId);

    Transfer createTransfer(Transfer transfer, int userId, int transferAccountFrom,
                            int transferAccountTo);
}
