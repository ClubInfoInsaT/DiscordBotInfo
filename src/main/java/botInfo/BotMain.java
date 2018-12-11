package botInfo;

import botInfo.command.meme.xkcd.XkcdCommand;
import botInfo.command.meteo.MeteoCommand;
import botInfo.command.text.*;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class BotMain{
    public static void main(String[] args) throws LoginException {
        //Token
        String token="MzkxMzMwODgxNDM3OTU4MTQ1.DZT7Jg.LDtRqICdhEe54OWiXQXc9H26ZA8";

        String ownerId="228970552545509376";
        // waiter
        EventWaiter waiter=new EventWaiter();

        CommandClientBuilder client=new CommandClientBuilder();

        client.setOwnerId(ownerId);
        client.setPrefix("/");

        client.addCommands(
                new RollCommand(),
                new MeteoCommand(),
                new DadJokeCommand(),
                new FlipCoinCommand(),
                new Ball8Command(),
                new XkcdCommand(),
                new LMGTFY(),
                new IsaacCommand(),
                new RemindCommand(),
                new SendNudesCommand(),
                new LoremIpsumCommand()
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