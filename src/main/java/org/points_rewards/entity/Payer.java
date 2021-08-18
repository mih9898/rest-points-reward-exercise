package org.points_rewards.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Table(name = "payers")
@Entity(name = "Payer")
@NoArgsConstructor
@Data
public class Payer {
    @Id
    @Column
    private String name;

    @Column
    private int balance;

    public Payer(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }




}
