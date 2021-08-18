package org.points_rewards.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@Data
public class TransactionDTO {
    private Timestamp date;
    private int points;
    private String payer;


    public void setDate(String date) {
        System.out.println("setDate:" + date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            this.date = null; //TODO: wrong date  validation in controller
            e.printStackTrace();
            return;
        }
        this.date = new Timestamp(parsedDate.getTime());
    }
}
