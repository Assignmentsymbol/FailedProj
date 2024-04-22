package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
/**
 * used for load and get animations
 */
public class AnimationsLoader {

    public static  Animation<TextureRegion> getCharacterAnimation(AnimationType type){
        Animation<TextureRegion> returns = null;

        if(type==AnimationType.UP){
            returns = getCharacterRegion(0,64);
        }
        if(type==AnimationType.DOWN){
            returns = getCharacterRegion(0,0);
        }
        if(type==AnimationType.LEFT){
            returns = getCharacterRegion(0,96);
        }
        if(type==AnimationType.RIGHT){
            returns = getCharacterRegion(0,32);
        }
        if(type==AnimationType.UpAtt){
            returns = getSelectedRegion(0,160,32,32,4);
        }
        if(type==AnimationType.DoAtt){
            returns = getSelectedRegion(0,128,32,32,4);
        }
        if(type==AnimationType.LeAtt){
            returns = getSelectedRegion(0,192+32,32,32,4);
        }
        if(type==AnimationType.RiAtt){
            returns = getSelectedRegion(0,192,32,32,4);
        }

        return  returns;

    }


    public static  Animation<TextureRegion> getEnemySimpleAnimation(){
        Texture walkSheet = new Texture(Gdx.files.internal("mobs.png"));

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;

        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, 64, frameWidth, frameHeight));
        }

        return new Animation<>(0.2f, walkFrames);
    }

    public static  Animation<TextureRegion> getEnemyAnimation(AnimationType type){
        Animation<TextureRegion> returns = null;
        if(type == AnimationType.UP){
            returns = getEnemyRegion(96,112);
        }
        if(type == AnimationType.DOWN){
            returns = getEnemyRegion(96,64);
        }
        if(type == AnimationType.LEFT){
            returns = getEnemyRegion(96,80);
        }
        if(type == AnimationType.RIGHT){
            returns = getEnemyRegion(96,96);
        }

        return returns;

    }

    private static Animation<TextureRegion> getEnemyRegion(int x, int y){
        Texture walkSheet = new Texture(Gdx.files.internal("mobs.png"));

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;

        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth+x, y, frameWidth, frameHeight));
        }

        return new Animation<>(0.9f, walkFrames);

    }

    private static Animation<TextureRegion> getCharacterRegion(int x,int y) {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth+ x, y, frameWidth, frameHeight));
        }

        return new Animation<>(0.1f, walkFrames);
    }

    private static Animation<TextureRegion> getSelectedRegion(int x,int y,int frameWidth,int frameHeight,int animationFrames) {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

//        int frameWidth = 16;
//        int frameHeight = 32;
//        int animationFrames = 4;

        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
        for (int col = 0; col < animationFrames; col++) {
            walkFrames.add(new TextureRegion(walkSheet, col * frameWidth+ x, y, frameWidth, frameHeight));
        }

        return new Animation<>(0.1f, walkFrames);
    }


}
