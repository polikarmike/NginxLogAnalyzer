package edu.java.reader;

import edu.java.type.LogRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LogParser {
    private static final String LOG_PATTERN =
        "^(\\S+) (\\S+) (\\S+) \\[([^\\]]+)\\] \"([^\"]+)\" (\\d+) (\\d+) \"([^\"]+)\" \"([^\"]+)\"";
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    private static final int GROUP_REMOTE_ADDR = 1;
    private static final int GROUP_REMOTE_USER = 2;
    private static final int GROUP_TIME_LOCAL = 4;
    private static final int GROUP_REQUEST = 5;
    private static final int GROUP_STATUS = 6;
    private static final int GROUP_BODY_BYTES_SENT = 7;
    private static final int GROUP_HTTP_REFERER = 8;
    private static final int GROUP_HTTP_USER_AGENT = 9;

    private LogParser() {

    }

    public static Stream<LogRecord> parseLogFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);
            String fileName = path.getFileName().toString();

            return lines.stream()
                .map(logLine -> parseLogLine(logLine, fileName))
                .filter(Objects::nonNull);
        } catch (IOException e) {
            return Stream.empty();
        }
    }

    public static LogRecord parseLogLine(String logLine, String fileName) {
        Pattern pattern = Pattern.compile(LOG_PATTERN);
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.matches()) {
            String remoteAddr = matcher.group(GROUP_REMOTE_ADDR);
            String remoteUser = matcher.group(GROUP_REMOTE_USER);
            String timeLocalStr = matcher.group(GROUP_TIME_LOCAL);
            String request = matcher.group(GROUP_REQUEST);
            int status = Integer.parseInt(matcher.group(GROUP_STATUS));
            long bodyBytesSent = Long.parseLong(matcher.group(GROUP_BODY_BYTES_SENT));
            String httpReferer = matcher.group(GROUP_HTTP_REFERER);
            String httpUserAgent = matcher.group(GROUP_HTTP_USER_AGENT);


            OffsetDateTime timeLocal = OffsetDateTime.parse(timeLocalStr, DATE_TIME_FORMATTER);

            return new LogRecord(
                remoteAddr,
                remoteUser,
                timeLocal,
                request,
                status,
                bodyBytesSent,
                httpReferer,
                httpUserAgent,
                fileName
            );
        } else {
            return null;
        }
    }
}
