package events.banana;

import auth.BotTokenID;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static events.BaseCommand.*;

public class BotBananaContribute extends ListenerAdapter {

    // System Auto
    private String contributionDate = "";

    // Arguments
    private String contributionTitle = "";
    private String contributionSubject = "";

    // Time
    Date date = new Date();
    SimpleDateFormat dateFolderFormat = new SimpleDateFormat("yyyy-MM-dd");
    String dateFolder = dateFolderFormat.format(date);

    // Folder Location
    private final String CONTRIBUTION_FOLDER_DIRECTORY = BotTokenID.botFolder + "/Contributions";
    private final String DATE_FOLDER_DIRECTORY = CONTRIBUTION_FOLDER_DIRECTORY + "/" + dateFolder;

    public BotBananaContribute() {
        makeFolder(CONTRIBUTION_FOLDER_DIRECTORY);
        makeFolder(DATE_FOLDER_DIRECTORY);
    }

    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (command.equals("$contribute")) {
            if (commandGroup.length > 0 && commandGroup.length < 3 || commandGroup.length > 3)
                sendMessage(event, "`$contribute <subject> <title>`", false);

            if (commandGroup.length == 3) {
                List<Message.Attachment> attachments = event.getMessage().getAttachments();
                if (verifyContribution(event, attachments)) {
                    contributionSubject = commandGroup[1];
                    contributionTitle = commandGroup[2];

                    // Date Folder Directory
                    String NAME_FOLDER_DIRECTORY = DATE_FOLDER_DIRECTORY + "/" + Objects.requireNonNull(event.getMember()).getUser().getName();
                    makeFolder(NAME_FOLDER_DIRECTORY);

                    // Info Folder Directory
                    String INFO_FOLDER_DIRECTORY = NAME_FOLDER_DIRECTORY + "/" + "[" + contributionSubject + "] - " + contributionTitle;
                    makeFolder(INFO_FOLDER_DIRECTORY);

                    sendMessage(event, "`Contribution submitted. Please wait for technical admin to verify. Message will be re-uploaded after being verified`", false);

                    if (attachments.isEmpty()) return; // no attachments on the message!
                    CompletableFuture<File> future = attachments.get(0).downloadToFile(INFO_FOLDER_DIRECTORY + "/" + attachments.get(0).getFileName());
                    future.exceptionally(error -> { // handle possible errors
                        error.printStackTrace();
                        return null;
                    });
                }
            }
        }
    }

    private void makeFolder(String directory) {
        File folder = new File(directory);
        if (folder.mkdir())
            System.out.println("Folder created " + getParseFolderName(directory));
    }

    private String getParseFolderName(String folderName) {
        return folderName.substring(folderName.indexOf('/') + 1);
    }

    private boolean verifyContribution(GuildMessageReceivedEvent event, List<Message.Attachment> attachments) {
        if (attachments.get(0).isVideo()) {
            sendMessage(event, "`Video is not supported to be a contribution`", false);
            return false;
        }

        if (attachments.get(0).isSpoiler()) {
            sendMessage(event, "`Spoiler is not supported to be a contribution`", false);
            return false;
        }
        return true;
    }
}
