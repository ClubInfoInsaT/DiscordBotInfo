package botInfo;


import botInfo.command.meme.xkcd.XkcdCommand;
import botInfo.command.meteo.MeteoCommand;
import botInfo.command.text.*;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class BotMain{
    public static void main(String[] args) throws LoginException {
        //Token
        String token="MzkxMzMwODgxNDM3OTU4MTQ1.DZT7Jg.LDtRqICdhEe54OWiXQXc9H26ZA8";

        String ownerId="228970552545509376";
        // waiter
        EventWaiter waiter=new EventWaiter();

        CommandClientBuilder client=new CommandClientBuilder();

        client.useDefaultGame();

        client.setOwnerId(ownerId);
        client.setPrefix("/");

        client.addCommands(new AboutCommand(Color.RED,
                "Le bot Discord du Club'Info",
                new String[]{"WOW","So much wow","Bamboozled"},
                new Permission[]{Permission.ADMINISTRATOR}),

                new RollCommand(),
                new PingCommand(),
                new MeteoCommand(),
                new DadJokeCommand(),
                new FlipCoinCommand(),
                new Ball8Command(),
                new XkcdCommand(),
                new LMGTFY(),
                new IsaacCommand(),
                new RemindCommand()
                );

        client.setEmojis("\uD83D\uDE03",
                "\uD83D\uDE2E",
                "\uD83D\uDE26");

        new JDABuilder(AccountType.BOT)
                .setToken(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setGame(Game.playing("World Of Warcraft"))
                .addEventListener(waiter)
                .addEventListener(client.build())

                .buildAsync();
    }
}