package com.dncoyote.expensetracker.common;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response<Data> {

    private Long timestamp = new Date().getTime();
    private String message;
    private Data data;
    private int statusCode;

    public Response(String message, Data data, int statusCode) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }
}