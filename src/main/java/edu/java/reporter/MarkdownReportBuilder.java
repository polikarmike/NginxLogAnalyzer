package edu.java.reporter;

import edu.java.statistics.AbstractLogStatistics;

public class MarkdownReportBuilder implements ReportBuilder {
    @Override
    public void appendSection(StringBuilder report, String sectionTitle, AbstractLogStatistics statistics, int limit) {
        appendStatistics(report, statistics, limit);
    }

    private void appendStatistics(StringBuilder report, AbstractLogStatistics statistics, int limit) {
        report.append(statistics.toMarkdown(limit));
    }
}
