package botInfo;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    /**
    *
    *   @param player The audio player this scheduler uses
     * */
    public TrackScheduler(AudioPlayer player){
        this.player=player;
        this.queue=new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track){
        if (!player.startTrack(track,true)){
            queue.offer(track);
        }
    }

    public void nextTrack(){
        player.startTrack(queue.poll(),false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player,AudioTrack track,AudioTrackEndReason endReason){
        if (endReason.mayStartNext){
            nextTrack();
        }
    }
    public void pause(){
        player.setPaused(!player.isPaused());
    }
}