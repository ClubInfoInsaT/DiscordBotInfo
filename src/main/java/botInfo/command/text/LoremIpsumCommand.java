package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

public class LoremIpsumCommand extends Command {
    private String[] base={"Integer ac mauris nec ante euismod porttitor vel sed dolor. Pellentesque fermentum, enim consectetur pretium efficitur, lacus ex mattis lectus, sit amet consequat erat elit eget lacus. Integer malesuada ultrices aliquam. Nulla commodo in est non tristique. Integer tincidunt ultrices congue. Nunc arcu est, condimentum nec mauris vitae, tempor condimentum sem. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur ullamcorper ultrices pulvinar. Nam consectetur metus eget nunc consectetur efficitur. In id convallis mauris. Aliquam lacinia varius vehicula. Proin ultrices quam ac molestie accumsan. Duis vulputate dui eu rhoncus viverra. Aliquam erat volutpat.",
    "Mauris nulla leo, hendrerit vel semper sed, pellentesque eget nisl. Donec finibus scelerisque ex, non viverra libero posuere vel. Donec euismod nisl molestie nunc egestas pellentesque. Vestibulum egestas augue a orci scelerisque, et consequat est lobortis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris mauris metus, iaculis eget magna sed, egestas vulputate nisl. Vestibulum posuere sit amet elit vel porttitor. Cras eu consequat nibh. Aenean non tortor leo. Phasellus a tristique nisi, ut ullamcorper leo.",
    "Fusce convallis, felis a aliquam interdum, massa lorem pellentesque orci, sit amet facilisis lacus enim nec sapien. In hac habitasse platea dictumst. Vestibulum consectetur purus sed est efficitur pretium. Morbi nisi mauris, euismod eget fermentum nec, rutrum sit amet felis. Donec vestibulum, erat in aliquam condimentum, felis turpis sagittis velit, eget imperdiet mi enim sed lorem. Proin quis ipsum ex. Suspendisse potenti. Aenean eleifend, diam ac luctus finibus, ligula erat hendrerit urna, at placerat massa tortor eget nunc. Mauris auctor ante nulla. Aliquam vel augue a sapien efficitur scelerisque ac ut metus. Quisque interdum ornare purus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec vitae ligula imperdiet, efficitur lacus scelerisque, varius velit.",
    "Suspendisse ornare aliquam lectus vitae fringilla. Aliquam volutpat, massa id laoreet egestas, odio orci elementum dui, ac ultrices libero urna vitae neque. Aliquam placerat nisl neque, nec volutpat tellus eleifend bibendum. Praesent varius pellentesque elementum. Mauris tempor risus nec iaculis sodales. Quisque vulputate elit nulla, at gravida est luctus eu. Praesent fringilla eros eu accumsan pellentesque. Ut in lectus imperdiet tellus aliquet fermentum nec eget ex. In dictum eu metus vel cursus. Nam rutrum, ante vitae blandit vehicula, dui magna lacinia massa, ac aliquet enim velit sed nisl."};
    public LoremIpsumCommand(){
        this.name="lorem";
        this.help="/lorem [N INT]. Renvois N paragraphe de lorem ipsum. Si pas d'argument renvois 1.";
        this.botPermissions=new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly=false;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        StringBuilder retour=new StringBuilder();
        String[] arg=commandEvent.getArgs().split("\\s+");
        if (arg.length==0){
            retour.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean dapibus nisi eu porttitor vulputate. Morbi ut mauris gravida, aliquam nisl id, sollicitudin lorem. Duis finibus enim justo, sed ultrices velit blandit nec. Nulla vehicula mattis orci id dictum. Cras arcu ligula, rhoncus sed sapien eget, posuere feugiat urna." +
                    " Vestibulum eu dui non leo varius suscipit vitae id odio. Vivamus ut vehicula purus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer laoreet, augue nec condimentum condimentum, purus enim egestas dolor, id tristique ipsum felis ut elit. Maecenas turpis felis, tristique at arcu nec, facilisis efficitur neque. " +
                    "Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec ac sagittis arcu, sit amet aliquet felis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse rhoncus quam non justo luctus facilisis sit amet nec orci. ");
        }else{
            retour.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean dapibus nisi eu porttitor vulputate. Morbi ut mauris gravida, aliquam nisl id, sollicitudin lorem. Duis finibus enim justo, sed ultrices velit blandit nec. Nulla vehicula mattis orci id dictum. Cras arcu ligula, rhoncus sed sapien eget, posuere feugiat urna." +
                    " Vestibulum eu dui non leo varius suscipit vitae id odio. Vivamus ut vehicula purus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer laoreet, augue nec condimentum condimentum, purus enim egestas dolor, id tristique ipsum felis ut elit. Maecenas turpis felis, tristique at arcu nec, facilisis efficitur neque. " +
                    "Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec ac sagittis arcu, sit amet aliquet felis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse rhoncus quam non justo luctus facilisis sit amet nec orci. ");
            for (int i=0;i<Integer.parseInt(arg[0]);i++){
                retour.append(base[i % base.length]);
            }
        }
        commandEvent.reply(retour.toString());
    }
}
