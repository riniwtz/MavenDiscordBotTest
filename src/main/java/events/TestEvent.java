package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestEvent extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String messageSent = event.getMessage().getContentRaw();

        Pattern pattern = Pattern.compile("Rintaro|Kevin|Lawrence|Jacob", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(messageSent);

        if (matcher.find())
            event.getChannel().sendMessage("Sorry but this name is not allowed.").queue();

//        if (messageSent.equalsIgnoreCase("Hello"))
//            event.getChannel().sendMessage("Hi").queue();
    }
}
