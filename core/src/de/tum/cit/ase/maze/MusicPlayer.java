package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
/**
 * Specifically used for playing sounds and musics.
 */
public class MusicPlayer {
    /**
     * chop sound
     */
    public Music chop;
    /**
     * hits sound
     */
    public Music hit,hitE;
    /**
     * sounds when gets some good stuffs
     */
    public Music complete;
    /**
     * sounds if win
     */
    public Music win;
    /**
     * sounds if lose
     */
    public Music death;

    public MusicPlayer() {
        this.chop =  Gdx.audio.newMusic(Gdx.files.internal("chop.wav"));
        chop.setLooping(false);
        this.hit =Gdx.audio.newMusic(Gdx.files.internal("hit0.mp3"));
        hit.setLooping(false);
        this.hitE =Gdx.audio.newMusic(Gdx.files.internal("hit2.mp3"));
        hit.setLooping(false);
        this.complete = Gdx.audio.newMusic(Gdx.files.internal("completetask.mp3"));
        complete.setLooping(false);
        this.win = Gdx.audio.newMusic(Gdx.files.internal("win.ogg"));
        win.setLooping(false);
        this.death = Gdx.audio.newMusic(Gdx.files.internal("death.ogg"));
        death.setLooping(false);
    }
}
