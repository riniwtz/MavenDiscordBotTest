package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static events.BaseCommand.*;

public class BotMessageFilter extends ListenerAdapter {
    private static boolean isActive = true;
    private Matcher matcher;
    private final StringBuilder regexPattern = new StringBuilder();

    public BotMessageFilter() {
        try {
            JSONParser jsonParser = new JSONParser();
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(BotMessageFilter.class.getResourceAsStream("/excluded_words.json"))));
            String text;
            StringBuilder excludedWordsJSONObject = new StringBuilder();
            while ((text = reader.readLine()) != null) {
                excludedWordsJSONObject.append(text);
            }
            JSONObject jsonObject = (JSONObject) jsonParser.parse(excludedWordsJSONObject.toString());
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
        searchFilter();
        if (isActive) {
            if (matcher.find() && !Objects.requireNonNull(event.getMember()).getUser().isBot())
                event.getMessage().delete().complete();
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

    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
        searchFilter();
        if (isActive) {
            if (matcher.find() && !Objects.requireNonNull(event.getMember()).getUser().isBot())
                event.getMessage().delete().complete();
        }
    }

    private void searchFilter() {
        Pattern pattern = Pattern.compile(regexPattern.substring(0, regexPattern.length() - 1), Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(message);
    }

    private static String getStatus() {
        if (isActive) return "On"; else return "Off";
    }
}
