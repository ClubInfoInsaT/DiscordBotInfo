package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.role.update.RoleUpdateMentionableEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import net.dv8tion.jda.core.requests.restaction.RoleAction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RemindCommand extends Command {
    protected static final SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
    public static final List<Association> listReminder=new LinkedList<Association>();
    protected Date date=null;
    public RemindCommand(){
        this.name="remind";
        this.aliases=new String[]{"rappel"};
        this.botPermissions=new Permission[]{Permission.MESSAGE_WRITE};
        this.help="/remind [DATE] [PORTEE] [MSG]. La date sous format JJ/MM/YYYY, la portée 1=tout le monde, 0=moi personnellement. MSG entreguillemets";
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] arg=commandEvent.getArgs().split("\\s+");
        if (arg[0].toLowerCase().equals("cancel")){
            int asupprimer=Integer.parseInt(arg[1]);
            removeTimer(commandEvent,asupprimer);
        }else if (arg[0].toLowerCase().equals("list")){
            listTimer(commandEvent);
        }else{
            addATimer(commandEvent);
        }
    }

    private void removeTimer(CommandEvent commandEvent, int index){
        RemindCommand.listReminder.remove(index);
        commandEvent.reply("Suppression du remind.");
    }

    private void listTimer(CommandEvent commandEvent){
        StringBuilder ret=new StringBuilder();
        ret.append("Liste des remind : taille ("+listReminder.size()+")\n");
        for (int i=0;i<listReminder.size();i++){
            ret.append(i+"."+"Propriétaire : "+listReminder.get(i).author.getName()+" "+listReminder.get(i).date.toString()+"\n");
        }
        commandEvent.reply(ret.toString());
    }

    private void addATimer(CommandEvent commandEvent){
        String message=commandEvent.getArgs().split("\"")[1];
        String[] arg=commandEvent.getArgs().split("\\s+");
        try {
            date=dateFormat.parse(arg[0]);
        } catch (ParseException e) {
            commandEvent.reply("Je n'arrive pas à parser la date.");
        }
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,-1);
        Date dateAvant=cal.getTime();
        Timer timer=new Timer();
        TimerTask todo=new TimerTask(){
            @Override
            public void run() {
                StringBuilder ret=new StringBuilder();
                if (Integer.parseInt(arg[1])==1){
                    Role everyone=commandEvent.getGuild().getRoles().get(commandEvent.getGuild().getRoles().size()-1);
                    //TODO: Trouver une façon de faire le tag @everyone
                    ret.append(everyone.getAsMention()+" ");
                    ret.append("Rappel :"+message);
                }else{
                    ret.append(commandEvent.getAuthor().getAsMention()+" ");
                    ret.append("Rappel : "+message);
                }
                commandEvent.reply(ret.toString());
                int index=findIndexByTimertask(this);
                if (index !=-1){
                    RemindCommand.listReminder.remove(index);
                }
                timer.cancel();
            }
        };
        commandEvent.reply("Ajout d'un timer. Size "+RemindCommand.listReminder.size());
        Association ass=new Association(commandEvent.getAuthor(),date,todo);
        RemindCommand.listReminder.add(ass);
        commandEvent.reply("Ajout terminé. Size "+RemindCommand.listReminder.size());
        timer.schedule(todo,dateAvant);
    }

    public int findIndexByTimertask(TimerTask timerTask){
        int retour=-1;
        for (int i=0;i<RemindCommand.listReminder.size();i++){
            Association aux=RemindCommand.listReminder.get(i);
            if (aux.todo.equals(timerTask)){
                retour=i;
            }
        }
        return retour;
    }

    class Association{
        User author;
        Date date;
        TimerTask todo;

        public Association(User author,Date date,TimerTask todo){
            this.author=author;
            this.date=date;
            this.todo=todo;
        }

    }
}
