package auth;

import java.io.*;

public class BotTokenID {
    private static final String BOT_FOLDER_NAME = "BananaBot";
    private static final String BOT_FOLDER = System.getProperty("user.home") + "/Documents/" + BOT_FOLDER_NAME;
    private String tokenID = "";

    public BotTokenID() {
        File directory = new File(BOT_FOLDER);
        if (directory.mkdir()) {
            System.out.println("Folder created " + BOT_FOLDER_NAME);
            writeTokenFile();
        } else {
            System.out.println("Reading token...");
            readTokenFile();
        }
    }

    private void writeTokenFile() {
        try {
            FileWriter writer = new FileWriter(BOT_FOLDER + "/" + "token.txt");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readTokenFile() {
        try {
            FileReader fileReader = new FileReader(BOT_FOLDER + "/" + "token.txt");
            BufferedReader reader = new BufferedReader(fileReader);
            tokenID = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTokenID() {
        return tokenID;
    }

    public static String getBotFolder() {
        return BOT_FOLDER;
    }
}
