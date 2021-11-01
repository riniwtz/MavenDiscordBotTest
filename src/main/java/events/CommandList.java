package events;

import main.RiniBot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class CommandList extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();

        // Commands command
        if (message.equals(RiniBot.prefix + "commands".toLowerCase())) {
            e.getChannel().sendMessage("Commands List:\n" +
                    RiniBot.prefix + "commands\n").queue();
        }

        // Filter command
        if (message.equals(RiniBot.prefix + "filter status"))
            e.getChannel().sendMessage("Message Filter status: " + MessageFilter.getStatus()).queue();

        if (MessageFilter.isActive) {
            if (message.equals(RiniBot.prefix + "filter on"))
                e.getChannel().sendMessage("Message Filter is already enabled").queue();

            if (message.equals(RiniBot.prefix + "filter off")) {
                MessageFilter.isActive = false;
                e.getChannel().sendMessage("Message Filter has been disabled by " + Objects.requireNonNull(e.getMember()).getUser().getName()).queue();
            }
        } else {
            if (message.equals(RiniBot.prefix + "filter off"))
                e.getChannel().sendMessage("Message Filter is already disabled").queue();

            if (message.equals(RiniBot.prefix + "filter on")) {
                MessageFilter.isActive = true;
                e.getChannel().sendMessage("Message Filter has been enabled by " + Objects.requireNonNull(e.getMember()).getUser().getName()).queue();
            }
        }
    }
}
