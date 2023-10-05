package com.dncoyote.expensetracker.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatement {

    private String category;
    private String type;
    private double amount;
    private String description;
    private Date date;

}