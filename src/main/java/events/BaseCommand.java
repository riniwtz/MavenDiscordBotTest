package events;

import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseCommand extends ListenerAdapter {

    public static String message, command;
    public static String[] commandGroup;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        message = event.getMessage().getContentRaw();
        commandGroup = message.split("\\s+");
        command = commandGroup[0];
    }

    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        message = event.getMessage().getContentRaw();
    }

    public static void sendMessage(GenericGuildMessageEvent event, Object text, boolean isComplete) {
        if (!isComplete)
            event.getChannel().sendMessage(String.valueOf(text)).queue();
        else
            event.getChannel().sendMessage(String.valueOf(text)).complete();
    }

    public static boolean hasNumber(String text) {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static boolean isNumber(String text) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
