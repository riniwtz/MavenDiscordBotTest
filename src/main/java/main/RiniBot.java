package main;

import events.BotCommandList;
import events.BotMessageFilter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class RiniBot {
    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault("").build();
        jda.addEventListener(new BotCommandList());
        jda.addEventListener(new BotMessageFilter());
    }
}
