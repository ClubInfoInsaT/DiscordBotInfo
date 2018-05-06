package botInfo.command.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    public final AudioPlayerSendHandler sendHandler;
    public int DEFAULT_VOLUME=35;
    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public GuildMusicManager(AudioPlayerManager manager){
        player=manager.createPlayer();
        player.setVolume(DEFAULT_VOLUME);
        scheduler=new TrackScheduler(player);
        sendHandler=new AudioPlayerSendHandler(player);
        player.addListener(scheduler);
    }

    public int getVolume() {
        return this.player.getVolume();
    }

    public void setDefaultVolume(int defaultVolume) {
        this.player.setVolume(defaultVolume);
    }
}
