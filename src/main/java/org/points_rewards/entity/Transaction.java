package org.points_rewards.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.points_rewards.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Table(name = "transactions")
@Entity(name = "Transaction")
@NoArgsConstructor
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;

    @Column
    private Timestamp date;

    @Column
    private int points;

    @Column(name = "is_counted")
    @ToString.Exclude
    private boolean isCounted;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JoinColumn(name = "payer_id",
            foreignKey = @ForeignKey(name = "transactions_ibfk_1"))
    private Payer payer;

    public Transaction(TransactionDTO transactionDTO, Payer payer) {
        System.out.println("Trans constr payer:" + payer);
        this.date = transactionDTO.getDate();
        this.points = transactionDTO.getPoints();
        this.isCounted = false;
        this.payer = payer;
    }

    public Transaction(Payer payerObj) {
        this.payer = payerObj;
        this.points = -payerObj.getBalance();
    }

    public Transaction(Payer payer, int afterSpendBalance) {
        this.payer = payer;
        this.points = afterSpendBalance;
//        this.points = afterSpendBalance - payer.getBalance();
    }

    public void addPoints(int pointsToAdd) {
        this.points = points + pointsToAdd;
    }


    // override getter to display json(jackson) how in specs (/spends)
    public String getPayer() {
        return payer.getName();
    }

    public Payer getPayerObj() {
        return payer;
    }


    public void setDate(String date) {
        System.out.println("setDate:" + date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            this.date = null; //TODO: wrong date  validation in controller
            return;
        }
        this.date = new Timestamp(parsedDate.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id == that.id && Objects.equals(payer, that.payer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, payer);
    }
}
