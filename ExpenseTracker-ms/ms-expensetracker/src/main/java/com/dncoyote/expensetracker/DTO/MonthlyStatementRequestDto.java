package com.dncoyote.expensetracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatementRequestDto {
    private String month;
    private String year;
    private String sortBy;
    private String sortOrder;
}