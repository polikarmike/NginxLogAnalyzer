package edu.java.statistics;

import edu.java.type.LogRecord;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralInfoStatistics extends AbstractLogStatistics {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public GeneralInfoStatistics(List<LogRecord> logRecords) {
        super(logRecords);
    }

    @Override
    @SuppressWarnings({"MultipleStringLiterals"})
    public String toMarkdown(int limit) {
        return "#### Общая информация\n\n"
            + "| Метрика | Значение |\n"
            + "|:-:|-:|\n"
            + "| Файл(-ы) | " + formatFileNames(getFileNames()) + " |\n"
            + "| Начальная дата | " + formatDate(getStartDate(), dateFormatter) + " |\n"
            + "| Конечная дата | " + formatDate(getEndDate(), dateFormatter) + " |\n"
            + "| Количество запросов | " + getRequestCount() + " |\n"
            + "| Средний размер ответа | " + formatAverageResponseSize(getAverageResponseSize()) + "b |\n\n";
    }

    @Override
    @SuppressWarnings({"MultipleStringLiterals"})
    public String toAsciiDoc(int limit) {
        return "#### Общая информация\n\n"
            + "|===\n"
            + "^| Метрика >| Значение\n"
            + "^| Файл(-ы) >| " + formatFileNames(getFileNames()) + "\n"
            + "^| Начальная дата >| " + formatDate(getStartDate(), dateFormatter) + "\n"
            + "^| Конечная дата >| " + formatDate(getEndDate(), dateFormatter) + "\n"
            + "^| Количество запросов >| " + getRequestCount() + "\n"
            + "^| Средний размер ответа >| " + formatAverageResponseSize(getAverageResponseSize()) + "b\n"
            + "|===\n\n";
    }

    private String formatDate(OffsetDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime != null ? dateTime.format(formatter) : "-";
    }

    private OffsetDateTime getStartDate() {
        return logRecords.isEmpty() ? null : logRecords.stream()
            .map(LogRecord::getTimeLocal)
            .min(OffsetDateTime::compareTo)
            .orElse(null);
    }

    private OffsetDateTime getEndDate() {
        return logRecords.isEmpty() ? null : logRecords.stream()
            .map(LogRecord::getTimeLocal)
            .max(OffsetDateTime::compareTo)
            .orElse(null);
    }

    private double getAverageResponseSize() {
        return logRecords.stream()
            .mapToLong(LogRecord::getBodyBytesSent)
            .average()
            .orElse(0.0);
    }

    private List<String> getFileNames() {
        List<String> fileNames = logRecords.stream()
            .map(LogRecord::getFileName)
            .distinct()
            .collect(Collectors.toList());

        return fileNames.isEmpty() ? List.of("-") : fileNames;
    }

    private String formatAverageResponseSize(double averageResponseSize) {
        return String.format("%.2f", getAverageResponseSize());
    }

    private String formatFileNames(List<String> fileNames) {
        return fileNames.stream()
            .map(name -> "`" + name + "`")
            .collect(Collectors.joining(", "));
    }

    private int getRequestCount() {
        return logRecords.size();
    }
}
