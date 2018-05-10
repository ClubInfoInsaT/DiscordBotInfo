package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

public class SendNudesCommand extends Command {
    public SendNudesCommand(){
        this.name="sendnude";
        this.help="/sendnude(s) send Nudes.";
        this.aliases=new String[]{"sendnudes"};
        this.botPermissions=new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly=false;
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply("Nudes");
    }
}
