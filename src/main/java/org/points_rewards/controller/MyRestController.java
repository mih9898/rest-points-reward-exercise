package org.points_rewards.controller;

import org.points_rewards.entity.Payer;
import org.points_rewards.entity.Transaction;
import org.points_rewards.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public Transaction newTransaction(@RequestBody Transaction transaction) {

        return null;
    }


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date parsedDate = dateFormat.parse("2020-11-02T14:00:00Z");
        Timestamp timestamp = new Timestamp(parsedDate.getTime());

        System.out.println(timestamp);
    }
}

