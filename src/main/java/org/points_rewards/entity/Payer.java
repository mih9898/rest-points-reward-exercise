package org.points_rewards.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Table(name = "payers")
@Entity(name = "Payer")
@NoArgsConstructor
@Data
public class Payer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;


//    @Id
    @Column
    private String name;

    @Column
    private int balance;

    public Payer(String payerName) {
        this.name = payerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payer payer = (Payer) o;
        return id == payer.id && Objects.equals(name, payer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
