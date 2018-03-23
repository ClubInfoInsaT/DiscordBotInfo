package botInfo;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    public final AudioPlayerSendHandler sendHandler;

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public GuildMusicManager(AudioPlayerManager manager){
        audioPlayer=manager.createPlayer();
        scheduler=new TrackScheduler(audioPlayer);
        sendHandler=new AudioPlayerSendHandler(audioPlayer);
        audioPlayer.addListener(scheduler);
    }
}
