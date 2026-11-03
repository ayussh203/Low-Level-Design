package LLD.LogFramework.formatter;

import LLD.LogFramework.entities.LogEvent;

public class PlainTextFormatter implements LogFormatter {
    @Override
    public String format(LogEvent logEvent) {
        return String.format("[%s] %s: %s", logEvent.getTimestamp(), logEvent.getLoggerName(), logEvent.getMessage());
    }
}
