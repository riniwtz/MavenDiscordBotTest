package events.banana;

import auth.BotTokenID;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachmentOption;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static events.BaseCommand.*;

@SuppressWarnings("MultipleVariablesInDeclaration")
public class BotBananaContribute extends ListenerAdapter {

    Date date = new Date();
    SimpleDateFormat dateFolderFormat = new SimpleDateFormat("yyyy-MM-dd");
    String dateFolder = dateFolderFormat.format(date);
    SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss a");
    String logDate = logDateFormat.format(date);

    private final String CONTRIBUTION_FOLDER_DIRECTORY = BotTokenID.botFolder + "/Contributions";
    private final String DATE_FOLDER_DIRECTORY = CONTRIBUTION_FOLDER_DIRECTORY + "/" + dateFolder;
    private final String[] subjects = {
            "math", "ap", "eng", "ict", "tle", "mapeh", "fil", "cl", "sci", "lip"
    };

    private Path directoryVerification;
    private String contributionSubject, contributionAuthor, contributionDate, contributionTitle;
    private String NAME_FOLDER_DIRECTORY, INFO_FOLDER_DIRECTORY;

    public BotBananaContribute() {
        createFolder(CONTRIBUTION_FOLDER_DIRECTORY);
        createFolder(DATE_FOLDER_DIRECTORY);
    }


    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        List<Message.Attachment> attachments = event.getMessage().getAttachments();
        switch (command) {
            case "$contribute" -> {
                // Deletes user message
                event.getMessage().delete().complete();
                if (commandGroup.length == 1) sendMessage(event, "`$contribute <subject> <title>`", false);
                if (commandGroup.length == 2) sendMessage(event, "`" + Arrays.stream(subjects).toList() + "`", false);
                if (commandGroup.length >= 3) {
                    // Assignments
                    contributionAuthor = Objects.requireNonNull(event.getMember()).getUser().getName();
                    contributionSubject = commandGroup[1];
                    contributionDate = logDate;
                    contributionTitle = getContributionTitle();
                    NAME_FOLDER_DIRECTORY = DATE_FOLDER_DIRECTORY + "/" + contributionAuthor;
                    INFO_FOLDER_DIRECTORY = NAME_FOLDER_DIRECTORY + "/" + "[" + contributionSubject.toUpperCase() + "] - " + contributionTitle;
                    String fileName = attachments.get(0).getFileName();

                    // Verifies contribution
                    if (verifyContribution(event, attachments)) {
                        makeFolder(NAME_FOLDER_DIRECTORY);
                        makeFolder(INFO_FOLDER_DIRECTORY);
                        sendMessage(event, "`[" + contributionDate + "] " + contributionAuthor + " contributed " + fileName + "`", false);
                        sendMessage(event, "`Contribution submitted. Please wait for technical admin to verify. Message will be re-uploaded after being verified`", false);

                        // Checks for error
                        if (attachments.isEmpty()) return; // no attachments on the message!
                        CompletableFuture<File> future = attachments.get(0).downloadToFile(INFO_FOLDER_DIRECTORY + "/" + fileName);
                        future.exceptionally(error -> { // handle possible errors
                            error.printStackTrace();
                            return null;
                        });
                    }
                }
            }
            case "$verify" -> {

                event.getMessage().delete().complete();
                if (commandGroup.length == 1) sendMessage(event, "`$verify <filename>`", false);
                if (commandGroup.length == 2) {
                    String VERIFIED_FOLDER_DIRECTORY = DATE_FOLDER_DIRECTORY + "/Verified";
                    String fileName = commandGroup[1];
                    findAttachment(fileName);
                    sendAttachment(event);
                    makeFolder(VERIFIED_FOLDER_DIRECTORY);
                    moveLocalAttachment(NAME_FOLDER_DIRECTORY, VERIFIED_FOLDER_DIRECTORY);
                }
            }
        }
    }

    private void findAttachment(String fileName) {
        Path file = Paths.get("C:\\Users\\Tarog\\Documents\\RiniBot\\Contributions");
        try {
            SimpleFileVisitor<Path> pathVisitor = new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attribute) {
                    if (file.getFileName().toString().contains(fileName))
                        directoryVerification = file;
                    return FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path p, IOException e) {
                    System.err.println("error: " + e + p);
                    return FileVisitResult.CONTINUE;
                }
            };

            // Enum
            EnumSet<FileVisitOption> option = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
            Files.walkFileTree(file, option, 5, pathVisitor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAttachment(GuildMessageReceivedEvent event) {
        if (!(directoryVerification == null)) {
            sendMessage(event, "`Contribution verified. Contributed by: " + contributionAuthor + "`", false);
            event.getChannel().sendFile(directoryVerification.toFile(), AttachmentOption.SPOILER).queue();
        } else sendMessage(event, "`No contributions are available to verify`", false);
    }

    private void createFolder(File folder) {
        if (folder.mkdir()) System.out.println("Folder created: " + folder.toPath());
        else if (folder.exists()) System.out.println("Folder already available...");
        else System.out.println("Folder couldn't create..");
    }

    private void moveLocalAttachment(String directory, String newDirectory) {
        try {
            Files.move(new File(directory).toPath(), new File(newDirectory).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getContributionTitle() {
        Pattern pattern = Pattern.compile("\"[^\"]*\"$");
        StringBuilder contributionTitleBuilder = new StringBuilder();
        for (String c : commandGroup)
            contributionTitleBuilder.append(c).append(" ");
        Matcher matcher = pattern.matcher(contributionTitleBuilder.substring(0, contributionTitleBuilder.length() - 1));
        if (matcher.find())
            return matcher.group().substring(1, matcher.group().length() - 1);
        return "";
    }

    private String getParseFolderName(String folderName) {
        return folderName.substring(folderName.indexOf('/') + 1);
    }

    private boolean verifyContribution(GuildMessageReceivedEvent event, List<Message.Attachment> attachments) {
        if (getContributionTitle().isBlank()) {
            sendMessage(event, "`Please put title name inside \" \"`", false);
            return false;
        }
        if (attachments.isEmpty()) {
            sendMessage(event, "`Attachment is Empty...`", false);
            return false;
        }
        if (attachments.get(0).isVideo()) {
            sendMessage(event, "`Video is not supported to be a contribution`", false);
            return false;
        }
        if (attachments.get(0).isSpoiler()) {
            sendMessage(event, "`Spoiler is not supported to be a contribution`", false);
            return false;
        }
        for (String s : subjects) {
            if (contributionSubject.equals(s.toLowerCase()))
                return true;
            else {
                sendMessage(event, "`Subject argument should be " + Arrays.stream(subjects).toList() + "`", false);
                return false;
            }
        }
        return false;
    }
}
