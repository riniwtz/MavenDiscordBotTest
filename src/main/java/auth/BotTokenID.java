package auth;

import java.io.*;

public class BotTokenID {
    private String tokenID = "";
    private static final String folderName = "RiniBot";
    public static String botFolder = System.getProperty("user.home") + "/Documents/" + folderName;
    public BotTokenID() {
        File directory = new File(botFolder);
        if (directory.mkdir()) {
            System.out.println("Folder created " + folderName);
            writeTokenFile();
        } else {
            System.out.println("Reading token...");
            readTokenFile();
        }
    }

    private void readTokenFile() {
        try {
            FileReader fileReader = new FileReader(botFolder + "/" + "token.txt");
            BufferedReader reader = new BufferedReader(fileReader);

            tokenID = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTokenFile() {
        try {
            FileWriter writer = new FileWriter(botFolder + "/" + "token.txt");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTokenID() {
        return tokenID;
    }
}
