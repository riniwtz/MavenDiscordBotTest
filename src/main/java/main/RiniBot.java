package main;

import events.CommandList;
import events.MessageFilter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class RiniBot {
    public static char prefix = '-';
    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault("").build();
        jda.addEventListener(new CommandList());
        jda.addEventListener(new MessageFilter());
    }
}
