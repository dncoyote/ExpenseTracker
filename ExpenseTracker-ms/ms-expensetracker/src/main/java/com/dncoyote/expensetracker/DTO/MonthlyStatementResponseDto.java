package com.dncoyote.expensetracker.DTO;

import java.util.List;

import com.dncoyote.expensetracker.model.MonthlyStatement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatementResponseDto {
    private List<MonthlyStatement> data;
    private double debitAmount;
    private double creditAmount;
    private double remainingAmount;
}