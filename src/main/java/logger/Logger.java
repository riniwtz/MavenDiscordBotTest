package logger;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static events.BaseCommand.*;

public class Logger extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // Print some output: goes to your special stream
        System.out.println("[" + event.getMessage().getTimeCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss a")) + "] " + Objects.requireNonNull(event.getMember()).getUser().getName() + ": " + message);
        // Put things back
        System.out.flush();
        System.setOut(old);
        String log = baos.toString();

        try {
            FileWriter logWriter = new FileWriter(System.getProperty("user.dir") + "/log.txt", true);
            logWriter.write(log);
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
