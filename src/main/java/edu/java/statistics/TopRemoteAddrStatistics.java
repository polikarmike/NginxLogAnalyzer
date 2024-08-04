package edu.java.statistics;

import edu.java.type.LogRecord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopRemoteAddrStatistics extends AbstractLogStatistics {
    public TopRemoteAddrStatistics(List<LogRecord> logRecords) {
        super(logRecords);
    }

    private Map<String, Long> getRemoteAddr(int topN) {
        if (logRecords == null) {
            return new LinkedHashMap<>();
        }

        return logRecords.stream()
            .collect(Collectors.groupingBy(LogRecord::getRemoteAddr, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(topN)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    @SuppressWarnings({"MultipleStringLiterals"})
    public String toMarkdown(int topN) {
        Map<String, Long> topResources = getRemoteAddr(topN);

        StringBuilder remoteAddresses = new StringBuilder();
        remoteAddresses.append("#### IP-адресы клиентов\n\n");
        remoteAddresses.append("| IP-Адрес | Количество |\n");
        remoteAddresses.append("|:-:|-:|\n");
        topResources.forEach((address, count) -> remoteAddresses.append("| ").append(address).append(" | ")
            .append(count).append(" |\n"));
        remoteAddresses.append("\n");
        return remoteAddresses.toString();
    }

    @Override
    @SuppressWarnings({"MultipleStringLiterals"})
    public String toAsciiDoc(int topN) {
        Map<String, Long> topResources = getRemoteAddr(topN);

        StringBuilder remoteAddresses = new StringBuilder();
        remoteAddresses.append("#### IP-адресы клиентов\n\n");
        remoteAddresses.append("|===\n^| IP-Адрес >| Количество\n");
        topResources.forEach((address, count) -> remoteAddresses.append("^| ").append(address).append(" >| ")
            .append(count).append("\n"));
        remoteAddresses.append("|===\n");
        remoteAddresses.append("\n");
        return remoteAddresses.toString();
    }
}
