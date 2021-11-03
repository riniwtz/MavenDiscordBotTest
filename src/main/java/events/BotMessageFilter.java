package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
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

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        Pattern pattern = Pattern.compile(regexPattern.substring(0, regexPattern.length() - 1), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(message);
        if (isActive) {
            if (matcher.find() && !Objects.requireNonNull(event.getMember()).getUser().isBot()) {
                event.getMessage().delete().complete();
                System.out.println(event.getMember().getUser().getName() + ": [" + matcher.toString().substring(matcher.toString().indexOf("lastmatch=")));
            }
        }

        if (command.equals(BotPrefix.prefix + "filter")) {
            if (commandGroup.length == 1)
                sendMessage(event, "`" + BotPrefix.prefix + "help filter" + "`", false);
            if (commandGroup.length == 2) {
                if (commandGroup[1].equals("status"))
                    sendMessage(event, "Message Filter status: " + getStatus(), false);
                if (isActive) {
                    switch (commandGroup[1]) {
                        case "on" -> sendMessage(event, "Message Filter is already enabled", false);
                        case "off" -> {
                            isActive = false;
                            sendMessage(event, "Message Filter has been disabled by " + Objects.requireNonNull(event.getMember()).getUser().getName(), false);
                        }
                    }
                } else {
                    switch (commandGroup[1]) {
                        case "on" -> {
                            isActive = true;
                            event.getChannel().sendMessage("Message Filter has been enabled by " + Objects.requireNonNull(event.getMember()).getUser().getName()).queue();
                        }
                        case "off" -> event.getChannel().sendMessage("Message Filter is already disabled").queue();
                    }
                }
            }
        }
    }

    public static String getStatus() {
        if (isActive) return "On"; else return "Off";
    }
}
