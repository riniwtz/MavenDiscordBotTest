package events;

import main.RiniBot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotPrefix extends ListenerAdapter {
    public static char prefix = '-';
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        String[] messageArray = message.split(" ");
        if (message.contains(prefix + "prefix set")) {
            if (messageArray.length == 3) {
                BotPrefix.prefix = messageArray[2].charAt(0);
            }
        }
    }
}
