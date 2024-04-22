package de.tum.cit.ase.maze.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * describe a exit's physical body and functions.
 */
public class Exit  extends Rectangle {
    Texture texture; public Sprite sprite;


    public Exit(float x, float y) {
        super(x, y, 100, 100);
        texture =  new Texture(Gdx.files.internal("things.png"));
        sprite = new Sprite(texture,0,0,16,16);
    }

    public Exit(float x, float y, float width, float height) {
        super(x, y, width, height);
        texture =  new Texture(Gdx.files.internal("things.png"));
        sprite = new Sprite(texture,0,0,16,16);
    }
}
