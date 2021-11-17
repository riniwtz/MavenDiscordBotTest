package main;

import auth.BotTokenID;
import events.*;
import events.banana.BotBananaContribute;
import events.banana.BotBananaFilter;
import events.changelog.Changelog;
import logger.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BananaBot {
    public static void main(String[] args) throws Exception {
        BotTokenID botTokenID = new BotTokenID();
        JDA jda = JDABuilder.createDefault(botTokenID.getTokenID()).build();
        if (jda.getStatus().isInit()) System.out.println("Token is connected");

        // events
        jda.addEventListener(new BaseCommand());
        jda.addEventListener(new BotCommandList());
        jda.addEventListener(new BotMessageFilter());
        jda.addEventListener(new BotPrefix());
        jda.addEventListener(new BotMassDelete());
        // logger
        jda.addEventListener(new Logger());
        // banana
        jda.addEventListener(new BotBananaFilter());
        jda.addEventListener(new BotBananaContribute());
        // changelog
        jda.addEventListener(new Changelog());

        // Done:
        /*
         * + Logger
         * + Changelog
         * + Fixed messages
         * + Finish BotMessageFilter class development
         * + Finish BotPrefix class development
         * + Finish BaseCommand class development
         * + Finish BotCommandList class development
         * + Added Banana Commands
         */

        // TODO:
        /*
         * + Microsoft document modifier
         * + Contribute command
         * + Prevent admins and mods from being affected by word filter
         * + Fix update message event to prevent bypassers (COMPLETED)
         * + Learn role and server name or server exclusion method in JDA
         */
    }
}
