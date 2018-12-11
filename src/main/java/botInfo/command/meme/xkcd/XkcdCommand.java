package botInfo.command.meme.xkcd;

import botInfo.command.Utils.XKCD;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

public class XkcdCommand extends Command {

    public XkcdCommand(){
        this.name="xkcd";
        this.help="xkcd [INT]. Envoi la dernière xkcd ou celle du numéro de l'argument";
        this.botPermissions=new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly=false;
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        int numero;
        String url;
        try{
            String[] arg= commandEvent.getArgs().split("\\s+");
            if (Integer.parseInt(arg[0])>0){
                numero=Integer.parseInt(arg[0]);
            }else{
                numero=-1;
            }
        }catch (Exception e){
            numero=-1;
        }
        if (numero<0){
            url="https://xkcd.com/info.0.json";
        }else{
            url="https://xkcd.com/"+numero+"/info.0.json";
        }

        try{
            String ask;
            if (numero<0){
                ask=XKCD.getNewestURL();
            }else{
                ask=XKCD.getImageURL(numero);
            }
            EmbedBuilder msg=new EmbedBuilder();
            msg.setImage(ask);
            commandEvent.reply(msg.build());
        } catch (Exception ex) {
                ex.printStackTrace();
        }
    }

}
