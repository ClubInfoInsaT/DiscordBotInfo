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
            if (charac.equals("!ping")){
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
                }
                super.onMessageReceived(event);
            }
        }
    }
}
