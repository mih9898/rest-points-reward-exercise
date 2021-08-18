package org.points_rewards.controller;

import org.points_rewards.entity.Payer;
import org.points_rewards.entity.Transaction;
import org.points_rewards.entity.TransactionDTO;
import org.points_rewards.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class MyRestController {
    private final GenericDao genericDao;

    @Autowired
    public MyRestController(GenericDao genericDao) {
        this.genericDao = genericDao;
    }


    @GetMapping("/payers")
    public List<Payer> getPayers() {
        return genericDao.getAll(Payer.class);
    }

    //TODO: fix transaction's getter to display only needed fields
    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return genericDao.getAll(Transaction.class);
    }

    @PostMapping("/transaction")
    public Transaction newTransaction(@RequestBody TransactionDTO transactionDTO) {
        System.out.println("transactionDTO:" + transactionDTO);

        Payer payer = createOrGetPayer(transactionDTO);
        Transaction transaction = new Transaction(transactionDTO, payer);
        int balanceIsGreaterThanZero = updatePayerBalance(payer,transaction.getPoints());
        //TODO: return string err if balance < 0

        genericDao.save(transaction);
        System.out.println("transaction:" + transaction);

        return transaction;
    }

    private int updatePayerBalance(Payer payer, int points) {
        int finalResultDiff = payer.getBalance() - points;
        if (finalResultDiff < 0) {
            return 0;
        }
        payer.setBalance(finalResultDiff);
        genericDao.saveOrUpdate(payer);
        return 1;
    }

    private Payer createOrGetPayer(TransactionDTO transactionDTO) {
        String payerName = transactionDTO.getPayer();
        int balance = transactionDTO.getPoints();

        Payer payer =  genericDao.getFirstEntryBasedOnAnotherTableColumnProperty("name", payerName, Payer.class);
        if (payer == null) {
            payer = new Payer(payerName);
            genericDao.save(payer);
        }
        return payer;

    }


    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        Date parsedDate = dateFormat.parse("2020-11-02T14:00:00Z");
//        Timestamp timestamp = new Timestamp(parsedDate.getTime());

        Transaction transaction = new Transaction();
        transaction.setDate("2020-11-02T14:00:00Z");

//        System.out.println(timestamp);
    }
}

