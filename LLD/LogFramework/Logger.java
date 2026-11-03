package LLD.LogFramework;

import LLD.LogFramework.appender.LogAppender;
import LLD.LogFramework.entities.LogEvent;
import LLD.LogFramework.entities.LogLevel;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Logger {

    private final String name;
    private volatile LogLevel minimumLevel;
    private final List<LogAppender> appenders;

    public Logger(String name,LogLevel minimumLevel,List<LogAppender> appenders) {
        this.name = Objects.requireNonNull(name);
        this.minimumLevel = Objects.requireNonNull(minimumLevel);
        this.appenders = List.copyOf(appenders);
    }
    public void setMinimumLevel(LogLevel minimumLevel) {
        this.minimumLevel = Objects.requireNonNull(minimumLevel);
    }

    public void log(LogLevel level,String message)
    {
        Objects.requireNonNull(level);
        Objects.requireNonNull(name);
        if(level.ordinal()<minimumLevel.ordinal())
        {
            return;
        }
        LogEvent event = new LogEvent(Instant.now(),level,name,message);

        for(LogAppender appender : appenders)
            {
            appender.append(event);
            }
    }

}
