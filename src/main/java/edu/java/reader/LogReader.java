package edu.java.reader;

import edu.java.type.LogRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LogReader {
    private LogReader() {

    }

    public static List<LogRecord> readLogs(String path, LocalDate fromDate, LocalDate toDate) {
        try {
            if (path.startsWith("http://") || path.startsWith("https://")) {
                URI uri = new URI(path);
                return readLogsFromUrl(uri, fromDate, toDate);
            } else {
                return readLogsFromLocalPath(path, fromDate, toDate);
            }
        } catch (URISyntaxException e) {
            return Collections.emptyList();
        }
    }

    private static List<LogRecord> readLogsFromLocalPath(String path, LocalDate fromDate, LocalDate toDate) {
        try {
            PathMatcher recursivePathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/" + path);

            return Files.walk(Paths.get("."), FileVisitOption.FOLLOW_LINKS)
                .filter(recursivePathMatcher::matches)
                .flatMap(LogParser::parseLogFile)
                .filter(logLine -> isWithinDateRange(logLine.getTimeLocal(), fromDate, toDate))
                .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static List<LogRecord> readLogsFromUrl(URI uri, LocalDate fromDate, LocalDate toDate) {
        try {
            List<String> lines = new BufferedReader(new InputStreamReader(uri.toURL().openStream()))
                .lines()
                .toList();

            return lines.stream()
                .map(logLine -> LogParser.parseLogLine(logLine, uri.toString()))
                .filter(Objects::nonNull)
                .filter(logLine -> isWithinDateRange(logLine.getTimeLocal(), fromDate, toDate))
                .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static boolean isWithinDateRange(OffsetDateTime date, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null && toDate == null) {
            return true;
        }

        return !date.toLocalDate().isBefore(fromDate) && !date.toLocalDate().isAfter(toDate);
    }
}
