package com.dncoyote.expensetracker.controller;

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

import com.dncoyote.expensetracker.DTO.MonthlyStatementRequestDto;
import com.dncoyote.expensetracker.DTO.MonthlyStatementResponseDto;
import com.dncoyote.expensetracker.common.ExpenseTrackerException;
import com.dncoyote.expensetracker.common.ExpenseTrackerConstants;
import com.dncoyote.expensetracker.common.Response;
import com.dncoyote.expensetracker.model.MonthlyStatement;
import com.dncoyote.expensetracker.service.ExpenseCalculatorService;
import com.dncoyote.expensetracker.service.GoogleApiService;

import static com.dncoyote.expensetracker.common.ApiEndpoints.API_PREFIX;
import static com.dncoyote.expensetracker.common.ApiEndpoints.MONTHLY_STATEMENT;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping(API_PREFIX)
public class ExpenseTrackerController {

    @Autowired
    private GoogleApiService googleApiService;

    @GetMapping(MONTHLY_STATEMENT)
    public ResponseEntity<Response<List<MonthlyStatementResponseDto>>> getMonthlyStatementFromGoogleSheet(
            @ModelAttribute MonthlyStatementRequestDto reqDto)
            throws GeneralSecurityException, IOException, NumberFormatException, ParseException {
        try {
            List<MonthlyStatement> expenses = googleApiService.getMonthlyStatementFromGoogleSheet(reqDto);
            if (!(expenses == null || expenses.isEmpty()) && null != reqDto.getSortBy()
                    && null != reqDto.getSortOrder()) {
                expenses = googleApiService.sortMonthlyStatement(expenses, reqDto.getSortBy(), reqDto.getSortOrder());
            }
            List<MonthlyStatementResponseDto> responseList = new ArrayList<>();
            MonthlyStatementResponseDto response = new MonthlyStatementResponseDto(expenses,
                    ExpenseCalculatorService.calculateDebitSum(expenses),
                    ExpenseCalculatorService.calculateCreditSum(expenses),
                    ExpenseCalculatorService.calculateBalance(expenses));
            responseList.add(response);
            return ResponseEntity
                    .ok(new Response<List<MonthlyStatementResponseDto>>(ExpenseTrackerConstants.SUCCESS_MESSAGE,
                            responseList, 0));
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