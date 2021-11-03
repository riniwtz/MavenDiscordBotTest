package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static events.BaseCommand.*;

public class BotPrefix extends ListenerAdapter {
    public static char prefix = '-';
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        // prefix
        if (command.equals(prefix + "prefix")) {
            if (commandGroup.length == 1)
                sendMessage(event, "The bot prefix is: `" + prefix + "`", false);
            if (commandGroup.length == 2) {
                String newPrefix = commandGroup[1];
                if (checkValidPrefixLength(event, newPrefix) && checkValidNewPrefix(event, newPrefix)) {
                    setPrefix(newPrefix.charAt(0));
                    sendMessage(event, "RiniBot new prefix is: `" + prefix + "`", false);
                }
            }
        }
    }

    private void setPrefix(char prefix) {
        BotPrefix.prefix = prefix;
    }

    private boolean checkValidNewPrefix(GuildMessageReceivedEvent event, String newPrefix) {
        // if new prefix is the same as current prefix
        if (newPrefix.charAt(0) == prefix)
            sendMessage(event, prefix + " has already been set", false);
        else if (hasNumber(newPrefix))
            sendMessage(event, "[Prefix must not contain any numerical values]", false);
        else
            return true;
        return false;
    }

    private boolean checkValidPrefixLength(GuildMessageReceivedEvent event, String newPrefix) {
        // Character Length
        if (newPrefix.length() == 1) return true;
        sendMessage(event, "[Single prefix character only]", false);
        return false;
    }

    private boolean hasNumber(String text) {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
}
