package auth;

import java.io.*;

public class BotTokenID {
    private String tokenID = "";
    private static String folderName = "RiniBot";
    public static String botFolder = System.getProperty("user.home") + "/Documents/" + folderName;
    public BotTokenID() {
        File directory = new File(botFolder);
        if (directory.mkdir()) {
            System.out.println("Folder created " + folderName);
            writeToFile("token.txt");
        } else {
            System.out.println("Reading token...");
            readTokenFile("token.txt");
        }
    }

    private void readTokenFile(String filename) {
        try {
            FileReader fileReader = new FileReader(botFolder + "/" + filename);
            BufferedReader reader = new BufferedReader(fileReader);

            tokenID = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(botFolder + "/" + filename);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTokenID() {
        return tokenID;
    }
}
