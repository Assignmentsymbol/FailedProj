package de.tum.cit.ase.maze.gameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Repusible extends Rectangle {

    public Vector2 velocity;
    public int repulsedTimes; //only for the walls

    public Repusible(Vector2 velocity) {
        this.velocity = velocity;
        repulsedTimes = 0;
    }

    public Repusible(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.velocity = new Vector2(0,0);
        repulsedTimes = 0;
    }

    public Repusible(Rectangle rect) {
        super(rect);
        this.velocity = new Vector2(0,0);
        repulsedTimes = 0;
    }

    public boolean overlaps(Repusible r){
        return super.overlaps(r);

    }




}
