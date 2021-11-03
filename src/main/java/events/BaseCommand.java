package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BaseCommand extends ListenerAdapter {

    public static String message, command;
    public static String[] commandGroup;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        message = event.getMessage().getContentRaw();
        commandGroup = message.split(" ");
        command = commandGroup[0];
    }

    public static void sendMessage(GuildMessageReceivedEvent event, String text, boolean isComplete) {
        if (!isComplete)
            event.getChannel().sendMessage(text).queue();
        else
            event.getChannel().sendMessage(text).complete();
    }
}
