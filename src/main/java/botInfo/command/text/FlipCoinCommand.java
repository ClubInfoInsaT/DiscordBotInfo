package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

import java.util.Random;

public class FlipCoinCommand extends Command {
    public FlipCoinCommand(){
        this.name="coin";
        this.help="Renvois soit pile ou face";
        this.botPermissions=new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly=false;
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        Random rand=new Random();
        String retour;
        int value=rand.nextInt(1);
        System.out.println(value);
        switch (value){
            case 0:
                retour="Pile";
                break;
            case 1:
                retour="Face";
                break;
            default:
                retour="Erreur..";
                break;
        }
        commandEvent.reply(retour);
    }
}
