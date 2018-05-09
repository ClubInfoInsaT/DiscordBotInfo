package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.audio.factory.IAudioSendSystem;

public class IsaacCommand extends Command {
    public IsaacCommand(){
        this.name="isaac";
        this.botPermissions=new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly=false;
        this.help="Renvois le site : Isaac Stylesheet (Liste des items).";
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply("http://platinumgod.co.uk/");
    }
}
