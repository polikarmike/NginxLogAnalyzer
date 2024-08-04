package edu.java.statistics;

import edu.java.type.LogRecord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopResourcesStatistics extends AbstractLogStatistics {

    public TopResourcesStatistics(List<LogRecord> logRecords) {
        super(logRecords);
    }

    private Map<String, Long> getTopResources(int limit) {
        if (logRecords == null) {
            return new LinkedHashMap<>();
        }

        return logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::getRequestURI, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    @SuppressWarnings({"MultipleStringLiterals"})
    public String toMarkdown(int limit) {
        Map<String, Long> topResources = getTopResources(limit);

        StringBuilder resources = new StringBuilder();
        resources.append("#### Запрашиваемые ресурсы\n\n");
        resources.append("| Ресурс | Количество |\n");
        resources.append("|:-:|-:|\n");
        topResources.forEach((resource, count) -> resources.append("| `").append(resource).append("` | ").append(count)
            .append(" |\n"));
        resources.append("\n");
        return resources.toString();
    }

    @Override
    @SuppressWarnings({"MultipleStringLiterals"})
    public String toAsciiDoc(int limit) {
        Map<String, Long> topResources = getTopResources(limit);

        StringBuilder resources = new StringBuilder();
        resources.append("#### Запрашиваемые ресурсы\n\n");
        resources.append("|===\n^| Ресурс >| Количество\n");
        topResources.forEach((resource, count) -> resources.append("^| ").append(resource).append(" >| ").append(count)
            .append("\n"));
        resources.append("|===\n");
        resources.append("\n");
        return resources.toString();
    }
}
