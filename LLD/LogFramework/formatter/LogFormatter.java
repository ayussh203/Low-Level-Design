package LLD.LogFramework.formatter;

import LLD.LogFramework.entities.LogEvent;

public interface LogFormatter {
    String format(LogEvent logEvent);
}
