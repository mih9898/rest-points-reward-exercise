package org.points_rewards.controller;

import org.points_rewards.entity.Payer;
import org.points_rewards.entity.Transaction;
import org.points_rewards.entity.TransactionDTO;
import org.points_rewards.repository.GenericDao;
import org.points_rewards.service.GenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;

@RestController
@RequestMapping("/rest")
public class MyRestController {
    private final GenericDao genericDao;
    private final GenService genService;


    @Autowired
    public MyRestController(GenericDao genericDao, GenService genService) {
        this.genericDao = genericDao;
        this.genService = genService;
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

    @GetMapping("/spends")
    public List<Map<String, Object>> getSpends(@RequestParam(name = "points") int points) {
        String hquery = "select tr from Transaction tr where tr.isCounted=false order by date asc";
        List<Transaction> orderedTransactions = genericDao.getListBasedOnCustomHquery(hquery);
        return genService.processSpendPointsForTransactions(points, orderedTransactions);
    }



    @PostMapping("/transaction")
    public Transaction newTransaction(@RequestBody TransactionDTO transactionDTO) {
        System.out.println("transactionDTO:" + transactionDTO);

        Payer payer = genService.createOrGetPayer(transactionDTO);
        Transaction transaction = new Transaction(transactionDTO, payer);
        int balanceIsGreaterThanZero = updatePayerBalance(payer,transaction.getPoints());
        if (balanceIsGreaterThanZero < 0) {
            System.err.println("/transaction.newTransaction: balanceIsLessThanZero");
            return null;
        }

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

    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        Date parsedDate = dateFormat.parse("2020-11-02T14:00:00Z");
//        Timestamp timestamp = new Timestamp(parsedDate.getTime());

        Transaction transaction = new Transaction();
        transaction.setDate("2020-11-02T14:00:00Z");

//        System.out.println(timestamp);
    }
}

