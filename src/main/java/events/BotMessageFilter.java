package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static events.BaseCommand.*;

public class BotMessageFilter extends ListenerAdapter {
    private static boolean isActive = true;
    StringBuilder regexPattern = new StringBuilder();

    public BotMessageFilter() {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("excluded_words.json"));
            JSONArray excludedWords = (JSONArray) jsonObject.get("words");
            JSONArray excludedSentences = (JSONArray) jsonObject.get("sentences");
            for (Object words : excludedWords)
                regexPattern.append(words).append("|");
            for (Object sentences : excludedSentences)
                regexPattern.append(sentences).append("|");
        } catch (IOException | ParseException e) {
            System.out.println("Exception occurred.");
            e.printStackTrace();
        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        message = event.getMessage().getContentRaw();
        if (command.equals(BotPrefix.prefix + "filter")) {
            if (commandGroup.length == 1)
                sendMessage(event, "`" + BotPrefix.prefix + "help filter" + "`", false);
            if (commandGroup.length == 2) {
                if (commandGroup[1].equals("status")) {
                    sendMessage(event, "Message Filter status: " + BotMessageFilter.getStatus(), false);
                }
            }

        }





        if (message.equals(BotPrefix.prefix + "filter status"))
            event.getChannel().sendMessage("Message Filter status: " + BotMessageFilter.getStatus()).queue();

        if (isActive) {
            if (message.equals(BotPrefix.prefix + "filter on"))
                event.getChannel().sendMessage("Message Filter is already enabled").queue();

            if (message.equals(BotPrefix.prefix + "filter off")) {
                isActive = false;
                event.getChannel().sendMessage("Message Filter has been disabled by " + event.getMember().getUser().getName()).queue();
            }

            Pattern pattern = Pattern.compile(regexPattern.substring(0, regexPattern.length() - 1), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);

            if (matcher.find())
                event.getMessage().delete().complete();
        } else {
            if (message.equals(BotPrefix.prefix + "filter off"))
                event.getChannel().sendMessage("Message Filter is already disabled").queue();

            if (message.equals(BotPrefix.prefix + "filter on")) {
                isActive = true;
                event.getChannel().sendMessage("Message Filter has been enabled by " + event.getMember().getUser().getName()).queue();
            }
        }
    }

    public static String getStatus() {
        if (isActive) return "On"; else return "Off";
    }
}
