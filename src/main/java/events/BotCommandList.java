package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import static events.BaseCommand.*;

public class BotCommandList extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        // Commands command
        if (message.equals(BotPrefix.prefix + "commands".toLowerCase())) {
            e.getChannel().sendMessage("Commands List:\n" +
                    BotPrefix.prefix + "commands\n").queue();
        }
    }
}
