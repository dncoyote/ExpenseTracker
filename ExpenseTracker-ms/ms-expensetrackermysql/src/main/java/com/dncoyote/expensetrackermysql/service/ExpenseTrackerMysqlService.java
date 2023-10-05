package com.dncoyote.expensetrackermysql.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dncoyote.expensetrackermysql.DTO.MonthlyStatementRequestDto;
import com.dncoyote.expensetrackermysql.model.MonthlyStatement;
import com.dncoyote.expensetrackermysql.repository.ExpenseTrackerMysqlRepository;
import com.dncoyote.expensetrackermysql.util.GoogleApiUtil;

@Service
public class ExpenseTrackerMysqlService {

    @Autowired
    private GoogleApiUtil googleApiUtil;
    @Autowired
    private ExpenseTrackerMysqlRepository repository;

    @Autowired
    private ExpenseTrackerMysqlRepository expenseTrackerMysqlRepository;

    public List<MonthlyStatement> saveAll(List<MonthlyStatement> statements) {
        return expenseTrackerMysqlRepository.saveAll(statements);
    }

    public List<MonthlyStatement> getMonthlyStatements() {
        return repository.findAll();
    }

    public List<MonthlyStatement> getStatements(MonthlyStatementRequestDto reqDto) {
        Date startDate = calculateStartDate(reqDto.getMonth(), reqDto.getYear());
        Date endDate = calculateEndDate(reqDto.getMonth(), reqDto.getYear());
        return repository.findByDateBetween(startDate, endDate);
    }

    private Date calculateStartDate(String month, String year) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(year), getMonthIndex(month), 1);
        return calendar.getTime();
    }

    private Date calculateEndDate(String month, String year) {
        Calendar calendar = new GregorianCalendar(Integer.parseInt(year), getMonthIndex(month), 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    private int getMonthIndex(String month) {
        // Convert month name to its corresponding index (0-based)
        String[] monthNames = new String[] { "January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December" };
        for (int i = 0; i < monthNames.length; i++) {
            if (monthNames[i].equalsIgnoreCase(month)) {
                return i;
            }
        }
        // Default to January if month name is not recognized
        return 0;
    }
}
