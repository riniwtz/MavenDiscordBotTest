package events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

import static events.BaseCommand.*;

public class BotMassDelete extends ListenerAdapter {

    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (command.equals(BotPrefix.prefix + "massdelete")) {
            if (commandGroup.length == 1)
                event.getChannel().sendMessage("`massdelete <number>`").queue();

            if (commandGroup.length == 2) {
                int massNumber = 0;
                if (isNumber(commandGroup[1])) {
                    massNumber = Integer.parseInt(commandGroup[1]);
                    if (massNumber > 0 && massNumber <= 50) {
                        if (massNumber == 1)
                            massNumber = 2;
                        else
                            massNumber += 1;
                        List<Message> messages = event.getChannel().getHistory().retrievePast(massNumber).complete();
                        event.getTextChannel().deleteMessages(messages).queue();
                    } else {
                        event.getChannel().sendMessage("`Number of mass deletion should be greater than 0 and less than or equals to 50`").queue();
                    }
                } else {
                    event.getChannel().sendMessage("`Number only`").queue();
                }
            }
        }
    }
}
