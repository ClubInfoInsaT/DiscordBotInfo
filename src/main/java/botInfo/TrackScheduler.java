package botInfo;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    public final AudioPlayer player;
    public LinkedList<AudioTrack> playlist;
    public AudioTrack lastplayed;
    public boolean repeat;

    /**
    *
    *   @param player The audio player this scheduler uses
     * */
    public TrackScheduler(AudioPlayer player){
        this.player=player;
        this.playlist=new LinkedList<AudioTrack>();
        this.repeat=false;
    }

    public void queue(AudioTrack track){
        if (!player.startTrack(track,true)){
            this.playlist.offer(track);
        }
    }

    public void nextTrack()
    {
        if (this.repeat==Boolean.TRUE) {
            lastplayed=playlist.poll();
            player.startTrack(lastplayed, false);
        }
        else{
            player.startTrack(this.lastplayed,false);
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player,AudioTrack track,AudioTrackEndReason endReason){
        if (endReason.mayStartNext){
            nextTrack();
        }
    }

    public void setRepeat(boolean rep){
        this.repeat=rep;
    }
    public void reset(){
        this.playlist=new LinkedList<AudioTrack>();
    }
    public void pause(){
        player.setPaused(!player.isPaused());
    }

}