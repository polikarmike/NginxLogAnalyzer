package edu.java.reporter;

import edu.java.statistics.*;
import edu.java.type.LogRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LogReport {
    private static final int DEFAULT_LIMIT = 3;
    private List<LogRecord> logRecords;

    public LogReport(List<LogRecord> logRecords) {

        this.logRecords = logRecords;
    }

    private void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toMarkdown() {
        return buildReport(new MarkdownReportBuilder());
    }

    public String toAdoc() {
        return buildReport(new AdocReportBuilder());
    }

    private String buildReport(ReportBuilder reportBuilder) {
        StringBuilder report = new StringBuilder();
        GeneralInfoStatistics generalInfo = new GeneralInfoStatistics(logRecords);
        TopResourcesStatistics topResources = new TopResourcesStatistics(logRecords);
        TopResponseCodesStatistics responseCodes = new TopResponseCodesStatistics(logRecords);
        HttpMethodStatistics methodCounts = new HttpMethodStatistics(logRecords);
        TopRemoteAddrStatistics remoteAddresses = new TopRemoteAddrStatistics(logRecords);

        reportBuilder.appendSection(report, "Общая информация", generalInfo, 0);
        reportBuilder.appendSection(report, "Запрашиваемые ресурсы", topResources, DEFAULT_LIMIT);
        reportBuilder.appendSection(report, "Коды ответа", responseCodes, DEFAULT_LIMIT);
        reportBuilder.appendSection(report, "Методы запроса", methodCounts, 0);
        reportBuilder.appendSection(report, "IP-адресы клиентов", remoteAddresses, DEFAULT_LIMIT);
        return report.toString();
    }

    public void writeMarkdownToFile(String filePath) {
        writeToFile(filePath, toMarkdown());
    }

    public void writeAdocToFile(String filePath) {
        writeToFile(filePath, toAdoc());
    }

}


