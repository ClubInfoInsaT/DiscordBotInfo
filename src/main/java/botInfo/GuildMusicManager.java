package botInfo;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    public final AudioPlayerSendHandler sendHandler;
    public int DEFAULT_VOLUME=35;
    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public GuildMusicManager(AudioPlayerManager manager){
        audioPlayer=manager.createPlayer();
        audioPlayer.setVolume(DEFAULT_VOLUME);
        scheduler=new TrackScheduler(audioPlayer);
        sendHandler=new AudioPlayerSendHandler(audioPlayer);
        audioPlayer.addListener(scheduler);
    }

    public int getVolume() {
        return this.audioPlayer.getVolume();
    }

    public void setDefaultVolume(int defaultVolume) {
        this.audioPlayer.setVolume(defaultVolume);
    }
}
