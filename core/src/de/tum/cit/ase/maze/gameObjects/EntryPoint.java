package de.tum.cit.ase.maze.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Describes the Body and relative texture of EntryPoint
 */
public class EntryPoint extends Rectangle {
    private Texture texture; private Sprite sprite;


    public Sprite getSprite(){
        return sprite;
    }


    public EntryPoint(float x, float y) {
        super(x, y, 100, 100);
        texture =  new Texture(Gdx.files.internal("basictiles.png"));
        sprite = new Sprite(texture,16,16,16,16);
    }
    public EntryPoint() {
        super();
        texture =  new Texture(Gdx.files.internal("basictiles.png"));
        sprite = new Sprite(texture,16,16,20,20);
    }
}
