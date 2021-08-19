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
//        List<Transaction> transactions = genericDao.getOldestTransactionsThatWereNotCounted();
        List<Transaction> spends = new ArrayList<>();
        for (Transaction transaction : orderedTransactions){
            Payer payer = transaction.getPayerObj();
            int transPoints = transaction.getPoints();
            points = points - transPoints;

            if (points >= 0) { //trans.points.balance is zero
                Transaction spend = new Transaction(payer, -transPoints);
                payer.setBalance(payer.getBalance() - transPoints);
                System.out.println("payer:" + payer);
                transaction.setCounted(true);
                // merge spends if they have the same payer
                if (spends.contains(spend)) {
                    for (Transaction existedSpendWithTheSamePayer : spends) {
                        if (existedSpendWithTheSamePayer.getPayer().equals(spend.getPayer())) {
                            existedSpendWithTheSamePayer.addPoints(spend.getPoints());
                        }
                        break;
                    }
                    continue;
                }
                spends.add(spend);

            } else { // TODO: trans.points.balance have smt + add own trans.balance to be able to reuse
                int diff = transPoints + points;
                spends.add(new Transaction(payer, -diff));
                payer.setBalance(payer.getBalance() -  diff);
            }

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
            System.out.println("createPay:" + payer);

//            genericDao.saveObject(payer);
        }
        return payer;
    }
}
