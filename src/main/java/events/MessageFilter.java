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

public class MessageFilter extends ListenerAdapter {
    public static boolean isActive = true;
    StringBuilder regexPattern = new StringBuilder();

    public MessageFilter() {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("excluded_words.json"));
            JSONArray excludedWords = (JSONArray) jsonObject.get("words");
            JSONArray excludedSentences = (JSONArray) jsonObject.get("sentences");
            for (Object words : excludedWords) {
                regexPattern.append(words).append("|");
            }
            for (Object sentences : excludedSentences) {
                regexPattern.append(sentences).append("|");
            }
        } catch (IOException | ParseException e) {
            System.out.println("Exception occurred.");
            e.printStackTrace();
        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (isActive) {
            String message = event.getMessage().getContentRaw();
            Pattern pattern = Pattern.compile(regexPattern.substring(0, regexPattern.length() - 1), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);

            if (matcher.find())
                event.getMessage().delete().complete();
        }
    }

    public static String getStatus() {
        if (isActive) return "On"; else return "Off";
    }
}
