package botInfo.command.text;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

import java.sql.*;

public class DadJokeCommand extends Command {

    Connection connection=null;
    Statement stmt=null;
    ResultSet res=null;

    public DadJokeCommand(){
        this.name="dad";
        this.help="Donne une dad Joke Random";
        this.botPermissions=new Permission[]{Permission.MESSAGE_WRITE};
        this.guildOnly=false;
    }
    @Override
    protected void execute(CommandEvent commandEvent) {
        try{
            String url="jdbc:mysql://localhost/discord?useSSL=false";
            String user="discord";
            String pass="1nf0rm4t1k";
            connection=DriverManager.getConnection(url,user,pass);
            stmt=connection.createStatement();
            res=stmt.executeQuery("SELECT text FROM DadJokes ORDER BY RAND() LIMIT 1");
            while (res.next()){
                String retour=res.getString("text");
                commandEvent.reply(retour);
            }
        }catch (SQLException e){
            commandEvent.reply("Veuillez vérifier la connexion à la base de donnée \nLog : "+e.getMessage());
        }
    }
}
