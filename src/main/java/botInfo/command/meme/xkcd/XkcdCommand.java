package botInfo.command.meme.xkcd;

import botInfo.command.Utils.XKCD;
import com.google.gson.Gson;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

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
