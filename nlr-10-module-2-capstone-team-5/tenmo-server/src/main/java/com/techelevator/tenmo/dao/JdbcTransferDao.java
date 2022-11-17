package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate dao){
        this.jdbcTemplate = dao;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * " +
                " FROM transfer";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while(results.next()){
            Transfer transfer = transferMapper(results);
            transfers.add(transfer);
        }
        return transfers;

    }

    //this will get the transfer type by id from our database
    @Override
    public Transfer getTransferTypeById(int id) {
        Transfer transfer = new Transfer();
        String sql = " SELECT transfer_type_id " +
                     " FROM transfer ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        if (result.next()) {
            transfer = transferMapper(result);
        } else {
            return null;
        }
            return transfer;
    }

    @Override
    public Transfer createTransfer(Transfer newTransfer) {
        Transfer transfer = new Transfer();
        String sql = " INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                " VALUES (2, 2, ?, ?, ?) " ;
        jdbcTemplate.update(sql, newTransfer.getTransferTypeId(),
                newTransfer.getTransferStatusId(),
                newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());
        transferAddToBalance(newTransfer.getAccountTo(), newTransfer.getAmount());
        transferSubtractFromBalance(newTransfer.getAccountTo(), newTransfer.getAmount());
        return newTransfer;
        }

    private void transferAddToBalance(int id, double amount) {
            String sql = " UPDATE account SET balance = balance + ? WHERE user_id = ?";
            jdbcTemplate.update(sql, amount, id);
        }

    private void transferSubtractFromBalance(int id, double amount) {
            String sql = " UPDATE account SET balance = balance - ? WHERE user_id = ?";
            jdbcTemplate.update(sql, amount, id);
        }

    private Transfer transferMapper(SqlRowSet result){
            Transfer transfer = new Transfer();
            transfer.setAccountFrom(result.getInt("account_id"));
            transfer.setTransferTypeId(result.getInt("transfer_type_id"));
            transfer.setAccountFrom(result.getInt("account_from"));
            transfer.setAccountTo(result.getInt("account_to"));
            transfer.setAmount(result.getDouble("amount"));
            return transfer;
    }

}
