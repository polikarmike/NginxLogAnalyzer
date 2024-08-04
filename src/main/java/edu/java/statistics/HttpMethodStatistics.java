package edu.java.statistics;

import edu.java.type.LogRecord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpMethodStatistics extends AbstractLogStatistics {

    public HttpMethodStatistics(List<LogRecord> logRecords) {
        super(logRecords);
    }

    private Map<String, Long> getMethodsCount() {
        if (logRecords == null) {
            return new LinkedHashMap<>();
        }

        return logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::getRequestMethod, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    @SuppressWarnings({"MultipleStringLiterals"})
    public String toMarkdown(int limit) {
        Map<String, Long> topResources = getMethodsCount();

        StringBuilder methods = new StringBuilder();
        methods.append("#### Методы запроса\n\n");
        methods.append("| Метод | Количество |\n");
        methods.append("|:-:|-:|\n");
        topResources.forEach((method, count) -> methods.append("| ").append(method).append(" | ").append(count)
            .append(" |\n"));
        methods.append("\n");
        return methods.toString();
    }

    @Override
    @SuppressWarnings({"MultipleStringLiterals"})
    public String toAsciiDoc(int limit) {
        Map<String, Long> topResources = getMethodsCount();

        StringBuilder methods = new StringBuilder();
        methods.append("#### Методы запроса\n\n");
        methods.append("|===\n^| Метод >| Количество\n");
        topResources.forEach((method, count) -> methods.append("^| ").append(method).append(" >| ").append(count)
            .append("\n"));
        methods.append("|===\n");
        methods.append("\n");
        return methods.toString();
    }
}

