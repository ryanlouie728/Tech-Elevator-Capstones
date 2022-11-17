package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getAllTransfers();

    Transfer getTransferTypeById(int id);

    Transfer createTransfer(Transfer newTransfer);




}
