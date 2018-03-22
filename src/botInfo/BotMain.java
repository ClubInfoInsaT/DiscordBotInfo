package botInfo;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;


public class BotMain extends ListenerAdapter {

    public void main(String[] args){
        // Note: It is important to register your ReadyListener before building
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken("p5CeWvew3xArKvf9ObHEyntDw1um5t9A")
                    .addEventListener(new BotMain())
                    .buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
