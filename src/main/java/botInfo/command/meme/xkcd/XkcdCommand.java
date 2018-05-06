package botInfo.command.meme.xkcd;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import jdk.nashorn.internal.parser.JSONParser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

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
            url="http://xkcd.com/info.0.json";
        }else{
            url="http://xkcd.com/"+numero+"/info.0.json";
        }

        try{
            System.out.println(url);

            //JSONObject rep=readJsonFromUrl(url);
            System.out.println(readJsonFromUrl(url));
        }catch (MalformedURLException e){
            commandEvent.reply("Mauvaise URL.\nLog: "+e.getMessage());
        }catch (IOException e){
            commandEvent.reply("Erreur lors de l'ouverture de la page\nLog: "+e.getMessage());
        }catch (JSONException e){
            commandEvent.reply("Erreur de parse\nLog: "+e.getMessage());
        }
    }

    public static String readJsonFromUrl(String url) throws IOException {
        CloseableHttpClient client=HttpClientBuilder.create().build();
        URL obj=new URL(url);
        HttpURLConnection con= (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //JSONObject myResponse = new JSONObject(response.toString());
        return response.toString();
    }
}
