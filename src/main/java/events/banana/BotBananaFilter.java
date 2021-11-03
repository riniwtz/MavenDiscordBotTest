package events.banana;

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

public class BotBananaFilter extends ListenerAdapter {
    private static boolean isActive = false;
    StringBuilder regexPattern = new StringBuilder();

    public BotBananaFilter() {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("excluded_names.json"));
            JSONArray excludedNames = (JSONArray) jsonObject.get("names");
            for (Object names : excludedNames) {
                regexPattern.append(names);
                regexPattern = new StringBuilder(regexPattern.toString().replaceAll(" ", "|"));
                regexPattern.append("|");
            }
        } catch (IOException | ParseException e) {
            System.out.println("Exception occurred.");
            e.printStackTrace();
        }
    }

    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        Pattern pattern = Pattern.compile(regexPattern.substring(0, regexPattern.length() - 1), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(message);
        if (isActive) {
            if (matcher.find() && !Objects.requireNonNull(event.getMember()).getUser().isBot())
                event.getMessage().delete().complete();
        }

        if (command.equals("$banana-filter")) {
            if (commandGroup.length == 1)
                sendMessage(event, "`$banana-filter <on|off|status>`", false);
            if (commandGroup.length == 2) {
                if (commandGroup[1].equals("status"))
                    sendMessage(event, "Banana Message Filter status: " + getStatus(), false);
                if (isActive) {
                    switch (commandGroup[1]) {
                        case "on" -> sendMessage(event, "Banana Message Filter is already enabled", false);
                        case "off" -> {
                            isActive = false;
                            sendMessage(event, "Banana Message Filter has been disabled by " + Objects.requireNonNull(event.getMember()).getUser().getName(), false);
                        }
                    }
                } else {
                    switch (commandGroup[1]) {
                        case "on" -> {
                            isActive = true;
                            event.getChannel().sendMessage("Banana Message Filter has been enabled by " + Objects.requireNonNull(event.getMember()).getUser().getName()).queue();
                        }
                        case "off" -> event.getChannel().sendMessage("Banana Message Filter is already disabled").queue();
                    }
                }
            }
        }
    }

    public static String getStatus() {
        if (isActive) return "On"; else return "Off";
    }
}
