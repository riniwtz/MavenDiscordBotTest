package main;

import events.*;
import events.BotBananaFilter;
import logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class RiniBot {

    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault("").build();
        // events
        jda.addEventListener(new BaseCommand());
        jda.addEventListener(new BotCommandList());
        jda.addEventListener(new BotMessageFilter());
        jda.addEventListener(new BotPrefix());
        // logger
        jda.addEventListener(new Logger());
        // banana
        jda.addEventListener(new BotBananaFilter());
    }
}
