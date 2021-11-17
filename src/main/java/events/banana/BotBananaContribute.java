package events.banana;

import auth.BotTokenID;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static events.BaseCommand.*;

public class BotBananaContribute extends ListenerAdapter {
    private final Date date = new Date();
    private final SimpleDateFormat DATE_FOLDER_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd H:mm:ss a");

    private String contributionSubject;
    private String contributionAuthor;
    private final String[] SUBJECT_GROUP = {"math", "ap", "eng", "ict", "tle", "mapeh", "fil", "cl", "sci", "lip"};

    public BotBananaContribute() {
        makeFolder(getContributionFolderDirectory());
        makeFolder(getDateFolderDirectory());
    }

    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        List<Message.Attachment> attachments = event.getMessage().getAttachments();
        setContributionAuthor(Objects.requireNonNull(event.getMember()).getUser().getName());
        setContributionSubject(event.getChannel().getName());
        switch (command) {
            case "$contribute" -> {

                event.getMessage().delete().complete();
                if (verifyContribution(event, attachments)) {
                    makeFolder(getUserNameFolderDirectory());
                    makeFolder(getSubjectFolderDirectory());
                    event.getChannel().sendMessage("`[" + getLogDate() + "] " + getContributionAuthor() + " contributed " + attachments.get(0).getFileName() + "`").queue();
                    event.getChannel().sendMessage("`Contribution submitted. Please wait for technical admin to verify. Message will be re-uploaded after being verified`").queue();
                    // Checks for error
                    if (attachments.isEmpty()) return; // no attachments on the message!
                    CompletableFuture<File> future = attachments.get(0).downloadToFile(getSubjectFolderDirectory() + "/" + attachments.get(0).getFileName());
                    future.exceptionally(error -> { // handle possible errors
                        error.printStackTrace();
                        return null;
                    });
                }
            }

            case "$verify" -> {
                event.getMessage().delete().complete();
                if (verifyContribution(event, attachments)) {
                    final File FILE_ATTACHMENT = new File(getSubjectFolderDirectory() + "/" + attachments.get(0).getFileName());
                    try {
                        event.getChannel().sendFile(new File(String.valueOf(FILE_ATTACHMENT))).complete();
                        if (FILE_ATTACHMENT.renameTo(new File(getSubjectFolderDirectory() + "/verified_" + attachments.get(0).getFileName()))) System.out.println();
                    } catch (IllegalArgumentException e) {
                        event.getChannel().sendMessage("`Provided file doesn't exist or cannot be read!`").queue();
                    }
                }
            }
        }
    }

    private boolean verifyContribution(GuildMessageReceivedEvent event, List<Message.Attachment> attachments) {
        if (verifySubject()) {
            if (attachments.isEmpty()) {
                event.getChannel().sendMessage("`Attachment is Empty...`").queue();
                return false;
            }
            if (attachments.get(0).isVideo()) {
                event.getChannel().sendMessage("`Video is not supported to be a contribution`").queue();
                return false;
            }
            if (attachments.get(0).isSpoiler()) {
                event.getChannel().sendMessage("`Spoiler is not supported to be a contribution`").queue();
                return false;
            }
            return true;
        } else event.getChannel().sendMessage("`Please contribute in a correct subject text channel`").queue();
        return false;
    }

    private boolean verifySubject() {
        for (String s : SUBJECT_GROUP)
            if (contributionSubject.equals(s)) return true;
        return false;
    }

    private void makeFolder(String directory) {
        File folder = new File(directory);
        if (folder.mkdir()) System.out.println("Folder created " + directory.substring(directory.indexOf('/') + 1));
    }

    public String getContributionFolderDirectory() {
        return BotTokenID.getBotFolder() + "/Contributions";
    }
    public String getDateFolderDirectory() {
        return getContributionFolderDirectory() + getFolderDate();
    }
    public String getUserNameFolderDirectory() {
        return getDateFolderDirectory() + "/" + getContributionAuthor();
    }
    public String getSubjectFolderDirectory() {
        return getUserNameFolderDirectory() + "/" + getContributionSubject().toUpperCase().charAt(0) + getContributionSubject().toLowerCase().substring(1);
    }
    private String getFolderDate() {
        return "/" + DATE_FOLDER_FORMAT.format(date);
    }
    private String getLogDate() {
        return LOG_DATE_FORMAT.format(date);
    }
    public String getContributionAuthor() {
        return contributionAuthor;
    }
    public String getContributionSubject() {
        return contributionSubject.toUpperCase().charAt(0) + contributionSubject.toLowerCase().substring(1);
    }
    public void setContributionAuthor(String contributionAuthor) {
        this.contributionAuthor = contributionAuthor;
    }
    public void setContributionSubject(String contributionSubject) {
        this.contributionSubject = contributionSubject;
    }
}
