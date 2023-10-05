package com.dncoyote.expensetrackermysql.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatement {

    @Id
    @GeneratedValue
    private Integer id;
    private String category;
    private String type;
    private double amount;
    private String description;
    private Date date;

}