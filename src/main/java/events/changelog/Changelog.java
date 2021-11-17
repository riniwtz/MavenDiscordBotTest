package events.changelog;

import events.BotPrefix;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static events.BaseCommand.*;

public class Changelog extends ListenerAdapter {
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (command.equals(BotPrefix.prefix + "changelog")) {
            event.getChannel().sendMessage("""
                    Changelog Update v1.0 Beta:
                    + Added new commands (-commands, -prefix, -filter)
                    + Added Logger""").queue();
        }
    }
}
