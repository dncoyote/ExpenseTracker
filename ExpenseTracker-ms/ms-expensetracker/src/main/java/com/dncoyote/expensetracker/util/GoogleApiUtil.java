package com.dncoyote.expensetracker.util;

import com.dncoyote.expensetracker.DTO.MonthlyStatementRequestDto;
import com.dncoyote.expensetracker.common.ExpenseTrackerException;
import com.dncoyote.expensetracker.model.MonthlyStatement;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GoogleApiUtil {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleApiUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public List<MonthlyStatement> getDataFromGoogleSheet(MonthlyStatementRequestDto reqDto)
            throws IOException, GeneralSecurityException, NumberFormatException, ParseException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // final String spreadsheetId = SPREADSHEET_ID;
        final String spreadSheetId = "1iY4Q0DLl-UofnPU_936Rz_lhef_egjhskfI0uH5nvgs";

        StringBuilder reqStringBuilder = new StringBuilder();
        reqStringBuilder.append(reqDto.getMonth());
        reqStringBuilder.append("_");
        reqStringBuilder.append(reqDto.getYear());
        reqStringBuilder.append("!B10:F");
        // reqStringBuilder.append(TEMPLATE_RANGE);

        final String range = reqStringBuilder.toString();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = null;
        List<MonthlyStatement> expenses = new ArrayList<>();
        try {
            response = service.spreadsheets().values()
                    .get(spreadSheetId, range)
                    .execute();
        } catch (IOException e) {
            // Log the error message or rethrow the exception for further analysis
            e.printStackTrace();
            // Handle the exception as needed
            throw e;
        }
        log.info("Fetching data from : " + spreadSheetId + range);
        List<List<Object>> values = response.getValues();
        if (response == null) {
            throw new ExpenseTrackerException("Google Sheets API returned null response.");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
            Map<Object, Object> storeDataFromGoogleSheet = new HashMap<>();

            if (values == null || values.isEmpty()) {
                System.out.println("No data found.");
            } else {
                System.out.println("Type, Amount");
                int i = 0;
                for (List row : values) {
                    // Print columns A and E, which correspond to indices 0 and 4.
                    System.out.printf("%s, %s, %s, %s, %s\n", ++i, row.get(0), row.get(1), row.get(2), row.get(3),
                            row.get(4));
                    // storeDataFromGoogleSheet.put(row.get(0), row.get(1));
                    expenses.add(new MonthlyStatement(row.get(0).toString(), row.get(1).toString(),
                            Double.parseDouble(row.get(2).toString()),
                            row.get(3).toString(), dateFormat.parse(row.get(4).toString())));
                }
            }
        }

        return expenses;
    }
}