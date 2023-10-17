package com.dncoyote.expensetrackermysql.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dncoyote.expensetrackermysql.model.MonthlyStatement;

public interface ExpenseTrackerMysqlRepository extends JpaRepository<MonthlyStatement, Integer> {
    // @Query("SELECT m FROM MonthlyStatement m WHERE m.date BETWEEN :startDate AND
    // :endDate")
    List<MonthlyStatement> findByDateBetween(Date startDate, Date endDate);
}