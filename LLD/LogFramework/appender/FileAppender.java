package LLD.LogFramework.appender;

import LLD.LogFramework.entities.LogEvent;
import LLD.LogFramework.formatter.LogFormatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileAppender implements LogAppender,AutoCloseable{

    public final LogFormatter formatter;
    public final BufferedWriter writer;

    public FileAppender(String fileName,LogFormatter formatter) throws IOException {
        this.formatter = formatter;

        this.writer= Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.APPEND);
    }

    @Override
    public synchronized void append(LogEvent logEvent) {
        try{
            String formattedLog = formatter.format(logEvent);
            writer.write(formattedLog);
            writer.newLine();
            writer.flush();

        }
        catch(IOException ioe){
            throw new UncheckedIOException(ioe);

        }
    }

    @Override
    public synchronized void close() throws IOException {
        writer.close();
    }



}
