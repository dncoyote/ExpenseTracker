package com.dncoyote.expensetracker.service;

import java.util.List;

import com.dncoyote.expensetracker.model.MonthlyStatement;

public class ExpenseCalculatorService {
    public static double calculateDebitSum(List<MonthlyStatement> expenses) {
        return expenses.stream()
                .filter(expense -> "DEBIT".equals(expense.getType()))
                .mapToDouble(MonthlyStatement::getAmount)
                .sum();
    }

    public static double calculateCreditSum(List<MonthlyStatement> expenses) {
        return expenses.stream()
                .filter(expense -> "CREDIT".equals(expense.getType()))
                .mapToDouble(MonthlyStatement::getAmount)
                .sum();
    }

    public static double calculateBalance(List<MonthlyStatement> expenses) {
        double debitSum = calculateDebitSum(expenses);
        double creditSum = calculateCreditSum(expenses);
        return creditSum - debitSum;
    }
}