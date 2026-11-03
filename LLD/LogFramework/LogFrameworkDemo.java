package LLD.LogFramework;

import LLD.LogFramework.appender.ConsoleAppender;
import LLD.LogFramework.appender.FileAppender;
import LLD.LogFramework.appender.LogAppender;
import LLD.LogFramework.entities.LogLevel;
import LLD.LogFramework.formatter.LogFormatter;
import LLD.LogFramework.formatter.PlainTextFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static LLD.LogFramework.entities.LogLevel.DEBUG;
import static LLD.LogFramework.entities.LogLevel.INFO;

public class LogFrameworkDemo {

    public static void main(String[] args)
    {

        LogFormatter formatter=new PlainTextFormatter();

        LogAppender consoleAppender=new ConsoleAppender(formatter);

        try{
            FileAppender fileAppender=new FileAppender("application.log",formatter);
            List<LogAppender> appenders=new ArrayList<LogAppender>();
            appenders.add(consoleAppender);
            appenders.add(fileAppender);
            Logger paymentLogger=new Logger("Payment Service", INFO, appenders);
            Logger orderLogger=new Logger("Order Service", INFO, appenders);
            paymentLogger.log(INFO
            ,"Processed one payment");
            orderLogger.log(DEBUG,"Processed one order");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
