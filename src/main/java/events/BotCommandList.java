package events;

import main.RiniBot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommandList extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();

        // Commands command
        if (message.equals(BotPrefix.prefix + "commands".toLowerCase())) {
            e.getChannel().sendMessage("Commands List:\n" +
                    BotPrefix.prefix + "commands\n").queue();
        }
    }
}
