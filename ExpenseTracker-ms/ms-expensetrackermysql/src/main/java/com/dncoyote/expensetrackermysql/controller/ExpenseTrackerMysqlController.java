package com.dncoyote.expensetrackermysql.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dncoyote.expensetrackermysql.DTO.MonthlyStatementRequestDto;
import com.dncoyote.expensetrackermysql.DTO.MonthlyStatementResponseDto;
import com.dncoyote.expensetrackermysql.common.ExpenseTrackerException;
import com.dncoyote.expensetrackermysql.common.ExpenseTrackerConstants;
import com.dncoyote.expensetrackermysql.common.Response;
import com.dncoyote.expensetrackermysql.model.MonthlyStatement;
import com.dncoyote.expensetrackermysql.service.ExpenseCalculatorService;
import com.dncoyote.expensetrackermysql.service.ExpenseTrackerMysqlService;
import com.dncoyote.expensetrackermysql.service.GoogleApiService;

import static com.dncoyote.expensetrackermysql.common.ApiEndpoints.API_PREFIX;
import static com.dncoyote.expensetrackermysql.common.ApiEndpoints.MONTHLY_STATEMENT;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping(API_PREFIX)
public class ExpenseTrackerMysqlController {

    @Autowired
    private GoogleApiService googleApiService;

    @Autowired
    private ExpenseTrackerMysqlService expenseTrackerMysqlService;

    @GetMapping("/products")
    public List<MonthlyStatement> findAllProducts() {
        return expenseTrackerMysqlService.getMonthlyStatements();
    }

    @GetMapping(MONTHLY_STATEMENT)
    public ResponseEntity<Response<List<MonthlyStatementResponseDto>>> getMonthlyStatementFromGoogleSheet(
            @ModelAttribute MonthlyStatementRequestDto reqDto)
            throws GeneralSecurityException, IOException, NumberFormatException, ParseException {
        try {
            List<MonthlyStatement> expenses = expenseTrackerMysqlService.getStatements(reqDto);
            List<MonthlyStatementResponseDto> responseList = new ArrayList<>();
            if (expenses == null || expenses.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new Response<>(ExpenseTrackerConstants.ERROR_MESSAGE, null, 400));
            } else {
                MonthlyStatementResponseDto response = new MonthlyStatementResponseDto(expenses,
                        ExpenseCalculatorService.calculateDebitSum(expenses),
                        ExpenseCalculatorService.calculateCreditSum(expenses),
                        ExpenseCalculatorService.calculateBalance(expenses));
                responseList.add(response);
                responseList.isEmpty();
                return ResponseEntity
                        .ok(new Response<List<MonthlyStatementResponseDto>>(ExpenseTrackerConstants.SUCCESS_MESSAGE,
                                responseList, 0));
            }
            // if (!(expenses == null || expenses.isEmpty()) && null != reqDto.getSortBy()
            // && null != reqDto.getSortOrder()) {
            // expenses = googleApiService.sortMonthlyStatement(expenses,
            // reqDto.getSortBy(), reqDto.getSortOrder());
            // }
            // List<MonthlyStatementResponseDto> responseList = new ArrayList<>();
            // MonthlyStatementResponseDto response = new
            // MonthlyStatementResponseDto(expenses,
            // ExpenseCalculatorService.calculateDebitSum(expenses),
            // ExpenseCalculatorService.calculateCreditSum(expenses),
            // ExpenseCalculatorService.calculateBalance(expenses));
            // responseList.add(response);
            // responseList.isEmpty();
            // return ResponseEntity
            // .ok(new
            // Response<List<MonthlyStatementResponseDto>>(ExpenseTrackerConstants.SUCCESS_MESSAGE,
            // responseList, 0));
        } catch (ExpenseTrackerException e) {
            // Handle custom application-specific exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>("Error: " + "NO data", null, 400));
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.badRequest()
                    .body(new Response<>(ExpenseTrackerConstants.ERROR_MESSAGE, null, 400));
        }
    }

    @GetMapping("/check")
    public String check() {
        return "hello";
    }

}