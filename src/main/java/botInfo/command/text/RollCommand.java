package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

import java.util.Random;

public class RollCommand extends Command {

    public RollCommand(){
        this.name="roll";
        this.help="roll [INT]. If no arg then roll 6, else roll [1-ARG]";
        this.botPermissions=new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly=false;
    }
    @Override
    protected void execute(CommandEvent event) {
        int roll=6;
        try {
            String[] arg=event.getArgs().split("\\s+");
            if (Integer.parseInt(arg[0])>0){
                roll=Integer.parseInt(arg[0]);
            }
        }catch (Exception e){

        }
        Random rand=new Random();
        int ret=rand.nextInt(roll)+1;
        String sms="Voilà ton résultat ";
        event.reply(":game_die: "+sms+ret);
    }
}
