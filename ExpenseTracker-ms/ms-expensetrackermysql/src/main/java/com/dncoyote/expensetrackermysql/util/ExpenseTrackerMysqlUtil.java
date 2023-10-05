package com.dncoyote.expensetrackermysql.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dncoyote.expensetrackermysql.DTO.MonthlyStatementRequestDto;
import com.dncoyote.expensetrackermysql.model.MonthlyStatement;
import com.dncoyote.expensetrackermysql.service.ExpenseTrackerMysqlService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Component
public class ExpenseTrackerMysqlUtil implements ApplicationRunner {

    @Autowired
    GoogleApiUtil googleApiUtil;
    @Autowired
    ExpenseTrackerMysqlService expenseTrackerMysqlService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        try {
            truncate();
            // String[] years = new String[] { "2022", "2023" };
            // String[] months = new String[] { "January", "February", "March", "April",
            // "May", "June", "July",
            // "August",
            // "September", "October", "November", "December" };
            // for (String year : years) {
            // for (String month : months) {
            MonthlyStatementRequestDto reqDto = new MonthlyStatementRequestDto("october", "2023", null, null);
            List<MonthlyStatement> response = googleApiUtil.getDataFromGoogleSheet(reqDto);
            expenseTrackerMysqlService.saveAll(response);
            // }
            // }

        } catch (

        Exception e) {
            // Handle the exception gracefully, e.g., log it
            e.printStackTrace(); // You can replace this with your preferred logging mechanism
        }
    }

    private void truncate() {
        Query query = entityManager.createNativeQuery("SHOW TABLES");
        List<Object> tables = query.getResultList();

        for (Object table : tables) {
            String tableName = (String) table;
            if (!tableName.contains("seq")) {
                Query truncateQuery = entityManager.createNativeQuery("TRUNCATE TABLE " +
                        tableName);
                truncateQuery.executeUpdate();
            }

        }
    }
}