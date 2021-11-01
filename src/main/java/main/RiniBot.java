package main;

import events.CommandList;
import events.MessageFilter;
import events.TestEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RiniBot {

    public static char prefix = '-';

    public static void main(String[] args) throws Exception {
        InputStream directory = RiniBot.class.getResourceAsStream("/src/main/java/main/text.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(directory));
        System.out.println(bufferedReader.readLine());


        JDA jda = JDABuilder.createDefault("").build();
        jda.addEventListener(new CommandList());
    }
}
