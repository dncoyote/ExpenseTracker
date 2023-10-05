package com.dncoyote.expensetrackermysql;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ExpenseTrackerMysqlApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ExpenseTrackerMysqlApplication.class);
		builder.headless(false).run(args);
	}
}