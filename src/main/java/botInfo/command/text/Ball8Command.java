package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Ball8Command extends Command {
    private final List<String> reponse= Arrays.asList(
            "Essaye plus tard",
            "Essaye encore",
            "Pas d'avis",
            "C'est ton destin",
            "Le sort en est jeté",
            "Une chance sur deux",
            "Repose ta question",
            "D'après moi oui",
            "C'est certain",
            "Oui absolument",
            "Tu peux compter dessus",
            "Sans aucun doute",
            "Très probable",
            "Oui",
            "C'est bien parti",
            "C'est non",
            "Peu probable",
            "Faut pas rêver",
            "N'y compte pas",
            "Impossible"
    );
    public Ball8Command(){
        this.name="ball";
        this.aliases=new String[]{"8ball","ball8"};
        this.help="Répond à une question de façon random";
        this.botPermissions=new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly=false;
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] com=commandEvent.getArgs().split("\\s+");
        if (com.length!=0) {
            Random rand = new Random();
            int value = rand.nextInt(reponse.size());
            commandEvent.reply(reponse.get(value));
        }else{
            commandEvent.reply("Il faudrait me poser une question..");
        }
    }
}
