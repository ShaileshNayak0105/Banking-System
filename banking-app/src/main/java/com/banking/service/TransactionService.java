package com.banking.service;

import com.banking.dto.TransactionSearchRequest;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.entity.TransactionType;
import com.banking.repository.TransactionRepository;
import com.opencsv.CSVWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final CurrentUserService currentUserService;
    private final TransactionRepository transactionRepository;

    public TransactionService(CurrentUserService currentUserService, TransactionRepository transactionRepository) {
        this.currentUserService = currentUserService;
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> history() {
        Account account = currentUserService.account();
        return transactionRepository.findTop50BySenderAccountOrReceiverAccountOrderByCreatedAtDesc(
                account.getAccountNumber(),
                account.getAccountNumber()
        );
    }

    public List<Transaction> miniStatement() {
        Account account = currentUserService.account();
        return transactionRepository.findTop10BySenderAccountOrReceiverAccountOrderByCreatedAtDesc(
                account.getAccountNumber(),
                account.getAccountNumber()
        );
    }

    public List<Transaction> search(TransactionSearchRequest request) {
        Account account = currentUserService.account();
        ZoneId zone = ZoneId.systemDefault();
        Instant start = request.startDate() == null
                ? null
                : request.startDate().atStartOfDay(zone).toInstant();
        Instant end = request.endDate() == null
                ? null
                : request.endDate().atTime(LocalTime.MAX).atZone(zone).toInstant();

        return transactionRepository.search(
                account.getAccountNumber(),
                start,
                end,
                request.type(),
                request.minAmount(),
                request.maxAmount()
        );
    }

    public byte[] exportPdf() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Transaction Statement"));
            Table table = new Table(5);
            addHeader(table, "Date");
            addHeader(table, "Type");
            addHeader(table, "Amount");
            addHeader(table, "Description");
            addHeader(table, "Balance");

            statementRows().forEach(row -> {
                table.addCell(String.valueOf(row.transaction().getCreatedAt()));
                table.addCell(row.transaction().getType().name());
                table.addCell(row.transaction().getAmount().toString());
                table.addCell(row.transaction().getDescription());
                table.addCell(row.balance().toPlainString());
            });

            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (RuntimeException ex) {
            throw new IllegalStateException("Unable to generate PDF statement", ex);
        }
    }

    public String exportCsv() {
        try {
            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);
            csvWriter.writeNext(new String[]{"Date", "Type", "Amount", "Description", "Balance"});

            for (StatementRow row : statementRows()) {
                csvWriter.writeNext(new String[]{
                        String.valueOf(row.transaction().getCreatedAt()),
                        row.transaction().getType().name(),
                        row.transaction().getAmount().toString(),
                        row.transaction().getDescription(),
                        row.balance().toPlainString()
                });
            }

            csvWriter.close();
            return stringWriter.toString();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to generate CSV statement", ex);
        }
    }

    private void addHeader(Table table, String text) {
        table.addHeaderCell(new Cell().add(new Paragraph(text)));
    }

    private List<StatementRow> statementRows() {
        Account account = currentUserService.account();
        BigDecimal balance = account.getBalance();
        List<StatementRow> rows = new ArrayList<>();

        for (Transaction transaction : history()) {
            rows.add(new StatementRow(transaction, balance));
            balance = transaction.getType() == TransactionType.DEBIT
                    ? balance.add(transaction.getAmount())
                    : balance.subtract(transaction.getAmount());
        }

        return rows;
    }

    private record StatementRow(Transaction transaction, BigDecimal balance) {
    }
}
