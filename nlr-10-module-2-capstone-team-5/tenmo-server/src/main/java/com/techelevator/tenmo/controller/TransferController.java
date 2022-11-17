package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/tenmo")
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController (TransferDao transferDao, UserDao userDao){
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping (path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(){
        List<Transfer> transfers = transferDao.getAllTransfers();
        return transfers;
    }

    @RequestMapping (path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferTypeById (@PathVariable int id){
        Transfer transfer = transferDao.getTransferTypeById(id);
        return transfer;
    }


    //call our createTransfer method from our JdbcTransferDao
    @RequestMapping (path = "/transfer", method = RequestMethod.POST)
        public Transfer createTransfer (@RequestBody Transfer transfer){
        return transferDao.createTransfer(transfer);
    }




}


