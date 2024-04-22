package de.tum.cit.ase.maze.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * describe a key's physical body and functions.
 */
public class Key  extends Rectangle {
    private Texture texture; private Sprite sprite;


    public Sprite getSprite(){
        return sprite;
    }

    public Key(float x, float y) {
        super(x, y, 100, 100);
        texture =  new Texture(Gdx.files.internal("things.png"));
        sprite = new Sprite(texture,16*3,16*4,16,16);
    }
    public Key() {
        super();
        texture =  new Texture(Gdx.files.internal("things.png"));
        sprite = new Sprite(texture,16*3,16*4,16,16);
    }


}
