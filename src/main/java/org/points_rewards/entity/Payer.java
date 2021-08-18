package org.points_rewards.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;


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

    public Payer(String payerName) {
        this.name = name;
    }

}
