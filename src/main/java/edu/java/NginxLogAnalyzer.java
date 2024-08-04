package edu.java;

import edu.java.reader.LogReader;
import edu.java.reporter.LogReport;
import edu.java.type.LogRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class NginxLogAnalyzer {
    private static final String MARKDOWN_FORMAT = "markdown";
    private static final String ADOC_FORMAT = "adoc";
    private static final String USAGE_MESSAGE = "Формат: java -jar nginx-log-stats.jar --path <путь-к-логам> "
        + "[--from <начальная-дата>] [--to <конечная-дата>] [--format <markdown/adoc>]";
    private static final String NO_PATH_MESSAGE = "Путь к лог-файлу не указан.";
    private static final String UNSUPPORTED_FORMAT_MESSAGE = "Неподдерживаемый формат: ";

    private NginxLogAnalyzer() {

    }

    public static void analyze(String[] args) {
        if (args.length < 2) {
            printAndExit(USAGE_MESSAGE);
        }

        String path = getArgumentValue(args, "--path");
        if (path == null) {
            printAndExit(NO_PATH_MESSAGE);
        }

        LocalDate fromDate = getFromDate(args);
        LocalDate toDate = getToDate(args);
        String format = getArgumentValue(args, "--format", MARKDOWN_FORMAT);


        List<LogRecord> logRecords = LogReader.readLogs(path, fromDate, toDate);
        LogReport logReport = analyzeLogs(logRecords);

        switch (format) {
            case MARKDOWN_FORMAT:
                logReport.writeMarkdownToFile("src/main/resources/logs/markdown_report.md");
                break;
            case ADOC_FORMAT:
                logReport.writeAdocToFile("src/main/resources/logs/adoc_report.adoc");
                break;
            default:
                printAndExit(UNSUPPORTED_FORMAT_MESSAGE + format);
        }

    }

    private static LogReport analyzeLogs(List<LogRecord> logRecords) {
        return new LogReport(logRecords);
    }

    private static LocalDate getFromDate(String[] args) {
        String fromDate = getArgumentValue(args, "--from");
        return fromDate != null ? LocalDate.parse(fromDate) : LocalDate.MIN;
    }

    private static LocalDate getToDate(String[] args) {
        String toDate  = getArgumentValue(args, "--to");
        return toDate != null ? LocalDate.parse(toDate) : LocalDate.MAX;
    }

    @SuppressWarnings("RegexpSinglelineJava")
    private static void printAndExit(String message) {
        System.out.println(message);
        System.exit(1);
    }

    private static String getArgumentValue(String[] args, String argumentName) {
        return Arrays.stream(args)
            .filter(arg -> arg.equals(argumentName))
            .findAny()
            .map(i -> args[Arrays.asList(args).indexOf(i) + 1])
            .orElse(null);
    }

    private static String getArgumentValue(String[] args, String argumentName, String defaultValue) {
        return Arrays.stream(args)
            .filter(arg -> arg.equals(argumentName))
            .findAny()
            .map(i -> args[Arrays.asList(args).indexOf(i) + 1])
            .orElse(defaultValue);
    }
}
