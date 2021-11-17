package events.banana;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static events.BaseCommand.*;

public class BotBananaFilter extends ListenerAdapter {
    private static boolean isActive = true;
    StringBuilder regexPattern = new StringBuilder();

    public BotBananaFilter() {
        try {
            JSONParser jsonParser = new JSONParser();
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(BotBananaFilter.class.getResourceAsStream("/excluded_names.json"))));
            String text;
            StringBuilder excludedNamesJSONObject = new StringBuilder();
            while ((text = reader.readLine()) != null)
                excludedNamesJSONObject.append(text);

            JSONObject jsonObject = (JSONObject) jsonParser.parse(excludedNamesJSONObject.toString());
            JSONArray excludedNames = (JSONArray) jsonObject.get("names");
            JSONArray excludedNicknames = (JSONArray) jsonObject.get("nicknames");
            for (Object names : excludedNames)
                regexPattern.append(parseNameToRegex(names.toString())).append("|");
            for (Object nicknames : excludedNicknames)
                regexPattern.append(nicknames).append("|");
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
                event.getChannel().sendMessage("`$banana-filter <on|off|status>`").queue();
            if (commandGroup.length == 2) {
                if (commandGroup[1].equals("status"))
                    event.getChannel().sendMessage("Banana Message Filter status: ").queue();
                if (isActive) {
                    switch (commandGroup[1]) {
                        case "on" -> event.getChannel().sendMessage("Banana Message Filter is already enabled").queue();
                        case "off" -> {
                            isActive = false;
                            event.getChannel().sendMessage("Banana Message Filter has been disabled by " + Objects.requireNonNull(event.getMember()).getUser().getName()).queue();
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

    private String parseNameToRegex(String name) {
        return name.replace('.', '|').replace('_', ' ');
    }

    private static String getStatus() {
        if (isActive) return "On"; else return "Off";
    }
}
