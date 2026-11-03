package LLD.LogFramework.appender;

import LLD.LogFramework.entities.LogEvent;
import LLD.LogFramework.formatter.LogFormatter;

public class ConsoleAppender  implements LogAppender{
    public final LogFormatter logFormatter;


    public ConsoleAppender(LogFormatter logFormatter){
        this.logFormatter = logFormatter;
    }
    @Override
    public void append(LogEvent event)
    {
        String formattedMessage=logFormatter.format(event);
        System.out.println(formattedMessage);

    }
}
