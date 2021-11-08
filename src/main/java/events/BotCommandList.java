package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static events.BaseCommand.*;

public class BotCommandList extends ListenerAdapter {

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        // Commands command
        if (command.equals(BotPrefix.prefix + "commands") && !Objects.requireNonNull(event.getMember()).getUser().isBot()) {
            sendMessage(event, "**Commands List:**```\n" +
                    BotPrefix.prefix + "commands\n" +
                    BotPrefix.prefix + "prefix <character>\n" +
                    BotPrefix.prefix + "filter <on|off|status>\n" +
                    BotPrefix.prefix + "massdelete <number>```", false);
        }

        // Banana Commands
        if (command.equals("$banana-cmd")) {
            sendMessage(event, "**Banana Command List:**\n" +
                    "$banana-filter `<on|off|status>`", false);
        }
    }
}
