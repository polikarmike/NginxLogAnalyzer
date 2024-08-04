package edu.java.reporter;

import edu.java.statistics.AbstractLogStatistics;

public interface ReportBuilder {
    void appendSection(StringBuilder report, String sectionTitle, AbstractLogStatistics statistics, int limit);
}
