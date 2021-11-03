package logger;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static events.BaseCommand.*;

public class Logger extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        Date date = new Date();

        SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss a");
        String logDate = logDateFormat.format(date);

        SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = fileNameFormat.format(date);

        writeToFile(fileName, readConsoleLine(event, logDate, "[" + logDate + "] " +
                "[" + event.getChannel().getName() + "] " +
                Objects.requireNonNull(event.getMember()).getUser().getAsTag() +
                ": " + message));
    }

    private String readConsoleLine(GuildMessageReceivedEvent event, String logDate, String lineContent) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // Output holder
        System.setOut(new PrintStream(byteArrayOutputStream));
        System.out.println(lineContent);
        System.out.flush();
        System.setOut(System.out);
        return byteArrayOutputStream.toString();
    }

    private void writeToFile(String fileName, String content) {
        try {
            FileWriter logWriter = new FileWriter(System.getProperty("user.dir") + "/log_" + fileName + ".txt", true);
            logWriter.write(content);
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
