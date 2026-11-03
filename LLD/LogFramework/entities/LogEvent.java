package LLD.LogFramework.entities;

import java.time.Instant;

public final class LogEvent {
    private final Instant timestamp;
    private final LogLevel logLevel;
    private final String message;
    private final String loggerName;


    public LogEvent(Instant timestamp, LogLevel logLevel, String message, String loggerName) {
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.message = message;
        this.loggerName = loggerName;
    }
    public Instant getTimestamp() {
        return timestamp;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }

    public String getLoggerName() {
        return loggerName;
    }
}
