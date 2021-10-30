import events.TestEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Bot {

    public static void main(String[] args) throws Exception {
        JDA jda = JDABuilder.createDefault("OTAzOTMyMDkzODYyNjU4MDU5.YX0KXQ.mDSMDh8q9MC5iVAPZMY1qdT_mwE").build();
        jda.addEventListener(new TestEvent());
    }
}
