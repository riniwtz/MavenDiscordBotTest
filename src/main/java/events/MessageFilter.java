package events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;

public class MessageFilter extends ListenerAdapter {
    private String regexWord;

    public MessageFilter() {







//        JSONParser parser = new JSONParser();
//
//        try {
//            reader = new FileReader("D:/Documents/Coding/MavenDiscordBotTest/src/main/java/resources/prohibitedwords.json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            Object obj = parser.parse(new FileReader(String.valueOf(reader)));
//            JSONObject jsonObject = (JSONObject) obj;
//
//            JSONArray words = (JSONArray) jsonObject.get("word");
//            Iterator<String> iterator = words.iterator();
//
//            while (iterator.hasNext()) {
//                System.out.println("Words: " + iterator.next());
//            }
//        } catch (ParseException | IOException e) {
//            System.out.println("Exception occurred...");
//            e.printStackTrace();
//        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();

//        Pattern pattern = Pattern.compile(regexWord);
//        Matcher matcher = pattern.matcher(message);
    }
}
