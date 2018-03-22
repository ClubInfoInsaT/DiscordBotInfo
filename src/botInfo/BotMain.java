package botInfo;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.lang.reflect.Executable;
import java.util.Random;


public class BotMain extends ListenerAdapter {

    public static void main(String[] args){
        // Note: It is important to register your ReadyListener before building
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken("MzkxMzMwODgxNDM3OTU4MTQ1.DZT7Jg.LDtRqICdhEe54OWiXQXc9H26ZA8")
                    .addEventListener(new BotMain())
                    .buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void msgText(Message msg, MessageChannel chan){
        User autheur=msg.getAuthor();
        String charac=msg.getContentDisplay();
        if (!autheur.isBot()){
            if (charac.equals("!ping")){
                chan.sendMessage("pong !").queue();
            }
            if (charac.toLowerCase().startsWith("Je suis".toLowerCase())){
                String quijesuis=charac.substring(8).split("[ ]")[0];
                //System.out.println(quijesuis);
                chan.sendMessage("Bonjour "+quijesuis+", je suis le bot du club").queue();
            }
            if (charac.toLowerCase().contains("test")){
                chan.sendMessage(autheur.getName()+"tests unitaires ?");
            }
            if (charac.startsWith("!roll")){
                int param1;
                try {
                    param1=Integer.parseInt(charac.trim().split("\\s+")[1]);
                }
                catch (Exception e){
                    param1=6;
                }
                Random rand= new Random();
                if (param1>0) {
                    int roll = rand.nextInt(param1) + 1;
                    String sms="";
                    switch (roll) {
                        case 1:
                            sms = "damn, too bad";
                            break;
                        case 2:
                            sms = "c'est mieux que 1";
                            break;
                        default:
                            if (roll == param1) {
                                sms = "damn, t'es bon";
                            } else {
                                int param2 = param1 - 1;
                                if (roll == param2) {
                                    sms = "presque.. t'es déception hein";
                                } else {
                                    sms = "pas mal du tout";
                                }
                            }
                            break;
                    }
                    chan.sendMessage(roll + " " +sms+" "+autheur.getName()).queue();
                }
                else{
                    chan.sendMessage("T'es un marrant toi "+autheur.getName()).queue();
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //These are provided with every event in JDA
        JDA jda = event.getJDA();                       //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

        //Event specific information
        //The user that sent the message
        User author = event.getAuthor();
        //The message that was received.
        Message message = event.getMessage();
        //This is the MessageChannel that the message was sent to.
        //  This could be a TextChannel, PrivateChannel, or Group!
        MessageChannel channel = event.getChannel();
        //System.out.println(author.getName());
        // ce qui est affiché
        String sms=message.getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {
            msgText(message, channel);
        }
    }
}
