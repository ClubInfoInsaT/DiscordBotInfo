package botInfo;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;


public class BotMain extends ListenerAdapter {

    private final AudioPlayerManager playerManager;
    private final Map<Long,GuildMusicManager> musicManagers;

    public static void main(String[] args){
        // Note: It is important to register your ReadyListener before building
        try {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken("MzkxMzMwODgxNDM3OTU4MTQ1.DZT7Jg.LDtRqICdhEe54OWiXQXc9H26ZA8")
                    .addEventListener(new BotMain())
                    .buildBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BotMain(){
        this.musicManagers=new HashMap<>();
        this.playerManager=new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }


    private void loadAndPlay(final TextChannel channel,final String trackURL){
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                channel.sendMessage("Ajout à la playlist de :"+audioTrack.getInfo().title).queue();
                play(channel.getGuild(),musicManager,audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                AudioTrack firstTrack=audioPlaylist.getSelectedTrack();

                if (firstTrack==null){
                    firstTrack=audioPlaylist.getTracks().get(0);
                }
                channel.sendMessage("Ajout de chanson").queue();
                play(channel.getGuild(),musicManager,firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Pas de chance, je l'ai pas :/").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("Ah, j'ai pas réussis à charger la musique :/").queue();
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track){
        connectToChannel(guild.getAudioManager());

        musicManager.scheduler.queue(track);
    }

    private void skipTrack(TextChannel channel){
        GuildMusicManager musicManager=getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("On next !").queue();
    }

    private void helpme(TextChannel channel){
        StringBuilder help=new StringBuilder();
        help.append("Voici la liste des commandes utiles: \n");
        help.append("- /help => voir ceci\n");
        help.append("- /play [URL_Youtube] => Charge la musique dans la file d'attente. Si le bot n'est pas là, il rejoint le channel bot_music\n");
        help.append("- /now => Affiche la musique actuelle\n");
        help.append("- /stop => Arrête la musique et le bot se déconnecte\n");
        help.append("- /list => Liste les musiques actuellement dans la file d'attente avec le temps de chacune\n");
        help.append("- /pause => Pause ou relance une musique sur pause\n");
        help.append("- /skip => Passe la chanson actuelle pour lancer la prochaine");
        help.append("- /volume ([10-100]) => Sans argument ça donne le volume actuel ( par défaut à 35). Avec argument change le volume pour mettre le volume passé en argument. Valeurs possibles comprises entre 10 et 100\n");
        help.append("Voilà voilà");
        channel.sendMessage(help.toString()).queue();
    }

    public void listAllsong(TextChannel channel){
        GuildMusicManager musicManager=getGuildAudioPlayer(channel.getGuild());
        Queue<AudioTrack> queue =musicManager.scheduler.queue;
        synchronized (queue){
            if (queue.isEmpty()){
                channel.sendMessage("La liste d'attente est vide");
            }
            {
                int trackCount = 0;
                long queueLength = 0;
                StringBuilder sb = new StringBuilder();
                sb.append("Liste actuelle: Entrée: ").append(queue.size()+1).append("\n");
                AudioTrack actual=musicManager.scheduler.player.getPlayingTrack();
                queueLength+=actual.getDuration()-actual.getPosition();
                sb.append("`[").append(getTimestamp(queueLength)).append("]` ");
                sb.append(actual.getInfo().title).append("\n");
                trackCount++;
                for (AudioTrack track : queue)
                {
                    queueLength += track.getDuration();
                    if (trackCount < 10)
                    {
                        sb.append("`[").append(getTimestamp(track.getDuration())).append("]` ");
                        sb.append(track.getInfo().title).append("\n");
                        trackCount++;
                    }
                }
                sb.append("\n").append("Total Durée de ma liste: ").append(getTimestamp(queueLength));

                channel.sendMessage(sb.toString()).queue();
            }
        }
    }

    private void volume(String[] command, TextChannel channel){
        GuildMusicManager musicManager=getGuildAudioPlayer(channel.getGuild());
        AudioPlayer player=musicManager.audioPlayer;
        if (command.length==2){
            try
            {
                int newVolume = Math.max(10, Math.min(100, Integer.parseInt(command[1])));
                int oldVolume = player.getVolume();
                player.setVolume(newVolume);
                channel.sendMessage("Le volume passe de  `" + oldVolume + "` à `" + newVolume + "`").queue();
            }
            catch (NumberFormatException e)
            {
                channel.sendMessage("`" + command[1] + "` ne fait pas partie de l'intervalle acceptée. (10 - 100)").queue();
            }
        }
        else{
            channel.sendMessage("Le volume actuel est à "+musicManager.getVolume()).queue();
        }
    }

    private void resetList(TextChannel channel){
        GuildMusicManager musicManager=getGuildAudioPlayer(channel.getGuild());
        musicManager.audioPlayer.stopTrack();
        musicManager.scheduler.reset();
        channel.sendMessage("On reset la playlist").queue();
    }

    public void stop(TextChannel channel){
        GuildMusicManager musicManager=getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();
        musicManager.audioPlayer.setPaused(false);
        Guild guild=channel.getGuild();
        guild.getAudioManager().setSendingHandler(null);
        guild.getAudioManager().closeAudioConnection();
        channel.sendMessage("Arrêt de la musique");
    }
    private void whatIsNow(TextChannel channel){
        GuildMusicManager musicManager=getGuildAudioPlayer(channel.getGuild());
        AudioTrack currentTrack = musicManager.audioPlayer.getPlayingTrack();
        if (currentTrack != null)
        {
            String title = currentTrack.getInfo().title;
            String position = getTimestamp(currentTrack.getPosition());
            String duration = getTimestamp(currentTrack.getDuration());

            String nowplaying = String.format("**En cours:** %s\n**Temps:** [%s / %s]",
                    title, position, duration);

            channel.sendMessage(nowplaying).queue();
        }
        else {
            channel.sendMessage("Euh, y'a rien qui est en cours là..").queue();
        }
    }


    private void restart(TextChannel channel){
        GuildMusicManager musicManager=getGuildAudioPlayer(channel.getGuild());
        AudioTrack track=musicManager.audioPlayer.getPlayingTrack();

        if (track==null){
            channel.sendMessage("Restart quoi ? J'ai rien en mémoire").queue();
        }

        if (track!=null){
            channel.sendMessage("Restart de"+track.getInfo().title).queue();
            musicManager.audioPlayer.playTrack(track.makeClone());
        }
    }

    private void pauseTrack(TextChannel channel){
        GuildMusicManager musicManager=getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.pause();
        if (musicManager.audioPlayer.isPaused()){
            channel.sendMessage("On pause..").queue();
        }
        else{
            channel.sendMessage("On reprend ! ").queue();
        }
    }

    private static void connectToChannel(AudioManager audioManager){
        if (!audioManager.isConnected() && ! audioManager.isAttemptingToConnect()){
            for (VoiceChannel voiceChannel:audioManager.getGuild().getVoiceChannels()){
                if (voiceChannel.getName().toLowerCase().equals("bot_music")){
                    audioManager.openAudioConnection(voiceChannel);
                    break;
                }
            }
        }
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild){
        long guildId=Long.parseLong(guild.getId());
        GuildMusicManager musicManager=musicManagers.get(guildId);

        if (musicManager == null){
            musicManager=new GuildMusicManager(playerManager);
            musicManagers.put(guildId,musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }


    public void msgText(Message msg, MessageChannel chan){
        User autheur=msg.getAuthor();
        String charac=msg.getContentDisplay();
        if (!autheur.isBot()){
            if (charac.toLowerCase().contains("ping")){
                chan.sendMessage("pong !").queue();
            }
            if (charac.toLowerCase().startsWith("Je suis".toLowerCase())){
                String quijesuis=charac.substring(8).split("[ ]")[0];
                //System.out.println(quijesuis);
                if (quijesuis.toLowerCase().equals("infobot")){
                    chan.sendMessage("Euh, c'est moi InfoBot..").queue();
                }
                else {
                    chan.sendMessage("Bonjour " + quijesuis + ", je suis le bot du club'Info !").queue();
                }
            }
            if (charac.toLowerCase().contains("test")){
                chan.sendMessage(autheur.getName()+" tests unitaires ?").queue();
            }
            if (charac.startsWith("!roll")){
                int param1;
                try {
                    param1=Integer.parseInt(charac.trim().split("\\s+")[1]);
                }
                catch (Exception e){
                    param1=6;
                }
                Random rand= new Random();
                if (param1>0) {
                    int roll = rand.nextInt(param1) + 1;
                    String sms="";
                    switch (roll) {
                        case 1:
                            sms = "damn, too bad";
                            break;
                        case 2:
                            sms = "c'est mieux que 1";
                            break;
                        default:
                            if (roll == param1) {
                                sms = "damn, t'es bon";
                            } else {
                                int param2 = param1 - 1;
                                if (roll == param2) {
                                    sms = "presque.. t'es deg hein";
                                } else {
                                    sms = "pas mal du tout";
                                }
                            }
                            break;
                    }
                    chan.sendMessage(roll + " " +sms+" "+autheur.getName()).queue();
                }
                else{
                    chan.sendMessage("T'es un marrant toi "+autheur.getName()).queue();
                }
            }
        }
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //These are provided with every event in JDA
        JDA jda = event.getJDA();                       //JDA, the core of the api.
        long responseNumber = event.getResponseNumber();//The amount of discord events that JDA has received since the last reconnect.

        //Event specific information
        //The user that sent the message
        User author = event.getAuthor();
        //The message that was received.
        Message message = event.getMessage();
        //This is the MessageChannel that the message was sent to.
        //  This could be a TextChannel, PrivateChannel, or Group!
        MessageChannel channel = event.getChannel();
        //System.out.println(author.getName());
        // ce qui est affiché
        String sms=message.getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {
            if (!event.getMessage().getContentRaw().startsWith("/")) {
                msgText(message, channel);
            }
            else{
                String[] command=event.getMessage().getContentRaw().split(" ",2);
                Guild guild = event.getGuild();

                if (guild != null){
                    if ("/play".equals(command[0]) && command.length==2){
                        loadAndPlay(event.getTextChannel(),command[1]);
                    }
                    else if ("/skip".equals(command[0])){
                        skipTrack(event.getTextChannel());
                    }
                    else if ("/pause".equals(command[0])){
                        pauseTrack(event.getTextChannel());
                    }
                    else if ("/reset".equals(command[0])){
                        resetList(event.getTextChannel());
                    }
                    else if ("/list".equals(command[0])){
                        listAllsong(event.getTextChannel());
                    }
                    else if ("/volume".equals(command[0])){
                        volume(command,event.getTextChannel());
                    }
                    else if ("/restart".equals(command[0])){
                        restart(event.getTextChannel());
                    }
                    else if ("/stop".equals(command[0])){
                        stop(event.getTextChannel());
                    }
                    else if ("/now".equals(command[0])){
                        whatIsNow(event.getTextChannel());
                    }
                    else if("/pplay".equals(command[0])){
                    }
                    else if ("/help".equals(command[0])){
                        helpme(event.getTextChannel());
                    }
                }
                super.onMessageReceived(event);
            }
        }
    }

    private static String getTimestamp(long milliseconds)
    {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);
    }

}

