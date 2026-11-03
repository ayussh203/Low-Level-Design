package LLD.LogFramework.appender;

import LLD.LogFramework.entities.LogEvent;

public interface LogAppender {

    void append(LogEvent logEvent);
}
