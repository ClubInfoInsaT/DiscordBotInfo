package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

public class LMGTFY extends Command {
    private String URL_BASE="http://lmgtfy.com/?q=";
    public LMGTFY(){
        this.name="letmeg";
        this.help="/letmeg|tgoogle [Query]. Renvoit un lien vers l'animation de Let me Google that for you";
        this.aliases=new String[]{"tgoogle"};
        this.botPermissions=new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly=false;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] command=commandEvent.getArgs().split("\\s+");
        if (command.length==0){
            commandEvent.reply("Mhum, il me faudrait un paramètre");
        }else{
            String query="";
            for (int i=0;i<command.length;i++){
                if (i==command.length-1){
                    query+=command[i];
                }else{
                    query+=command[i]+"+";
                }
            }
            commandEvent.reply(URL_BASE+query);
        }
    }
}
