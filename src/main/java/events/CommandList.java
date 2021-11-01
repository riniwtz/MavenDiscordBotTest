package events;

import main.RiniBot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandList extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        if (message.equals(RiniBot.prefix + "commands".toLowerCase())) {
            e.getChannel().sendMessage("Commands List:\n" +
                    RiniBot.prefix + "commands\n").queue();
        }
    }
}
