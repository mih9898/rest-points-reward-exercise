package org.points_rewards.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.points_rewards.entity.Payer;
import org.points_rewards.entity.Transaction;
import org.points_rewards.entity.TransactionDTO;
import org.points_rewards.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GenService {

    private GenericDao genericDao;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public GenService(GenericDao genericDao) {
        this.genericDao = genericDao;
    }


    public List<Map<String, Object>> processSpendPointsForTransactions(int points,  List<Transaction> orderedTransactions) {
        List<Transaction> spends = new ArrayList<>();

        if (orderedTransactions.isEmpty() || points == 0) {
            return null;
        }

        // spending points from unchecked transactions
        for (Transaction transaction : orderedTransactions){
            Payer payer = transaction.getPayerObj();
            int pointsToSpend = payer.getBalance();
            int afterSpendBalance = pointsToSpend - points;
            points = points - pointsToSpend;


            // update balance, transaction's flag, and forming final spend-transaction
            if (afterSpendBalance <= 0) {
                if (payer.getBalance() > 0) {
                    spends.add(new Transaction(payer));
                }
                payer.setBalance(0);
                transaction.setPayer(payer);
                transaction.setCounted(true); // no points to spend in future. can set ignore flag
            } else {
                spends.add(new Transaction(payer, afterSpendBalance));
                payer.setBalance(afterSpendBalance);
                transaction.setPayer(payer);
            }
            // no more points
            if (points <= 0) {
                break;
            }
        }
        //update ignore-flag(s) for transactions+balance
        for (Transaction updatedTransaction : orderedTransactions) {
            genericDao.saveOrUpdate(updatedTransaction);
        }

        return convertTransactionsToMapList(spends);
    }

    //generates formatted map list to display json wit custom fields
    private List<Map<String, Object>> convertTransactionsToMapList(List<Transaction> spends) {
        if (spends.isEmpty()) {
            return null;
        }
        List<Map<String, Object>> spendTransactionsMaps = new ArrayList<>();
        for (Transaction transaction : spends) {
            Map<String, Object> spendTransactionsMap = new HashMap<>();
            spendTransactionsMap.put("payer", transaction.getPayer());
            spendTransactionsMap.put("points", transaction.getPoints());
            spendTransactionsMaps.add(spendTransactionsMap);
        }
        return spendTransactionsMaps;
    }

    public Payer createOrGetPayer(TransactionDTO transactionDTO) {
        String payerName = transactionDTO.getPayer();
        Payer payer =  genericDao.getFirstEntryBasedOnAnotherTableColumnProperty("name", payerName, Payer.class);
        if (payer == null) {
            payer = new Payer(payerName);
            genericDao.save(payer);
        }
        return payer;
    }
}
