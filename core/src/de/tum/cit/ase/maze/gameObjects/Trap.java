package de.tum.cit.ase.maze.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * describe a trap's physical body and functions.
 */
public class Trap extends Rectangle {
    Texture texture; public Sprite sprite;


    public Trap(float x, float y) {
        super(x, y, 100, 100);
        texture =  new Texture(Gdx.files.internal("things.png"));
        sprite = new Sprite(texture,0,0,16,16);
    }

    public Trap(float x, float y, float width, float height) {
        super(x, y, width, height);
        texture =  new Texture(Gdx.files.internal("objects.png"));
        sprite = new Sprite(texture,0,160,32,32);
    }

    /**
     * simulating Coulomb force but traps specific edition. failed to implement code reuse :(
     */
    public void repulsing(Player player){
        Vector2 positionDiff = new Vector2();
        if(this.overlaps(player)){
            float repulsingRate = 3;
            player.justHurt = true;
            Vector2 playerCenter = new Vector2();
            player.getCenter(playerCenter);
            Vector2 playerOriginalCenter = new Vector2(playerCenter.x-player.velocity.x,playerCenter.y-player.velocity.y);

            Vector2 wallCenter = new Vector2();
            this.getCenter(wallCenter);
            positionDiff = new Vector2(playerOriginalCenter.x-wallCenter.x,playerOriginalCenter.y-wallCenter.y);

            if (Math.abs(positionDiff.x) > Math.abs(positionDiff.y)) {
                //since all the walls are square, it shows the direction of the repulsion. Here it will be horizontal.
                player.x -= player.velocity.x*repulsingRate;
            }
            if (Math.abs(positionDiff.x) < Math.abs(positionDiff.y)) {
                player.y -= player.velocity.y*repulsingRate;
            }

            if(player.repulsedTimes==1){

                playerOriginalCenter = new Vector2(playerCenter.x,playerCenter.y);
                player.getCenter(playerCenter);
                Vector2 v3 = playerCenter.sub(playerOriginalCenter);

                if(v3.y!=0){
                    player.x += player.velocity.x*repulsingRate;
                    player.velocity.y=0*repulsingRate;
                }
                if(v3.x !=0) {
                    player.y += player.velocity.y*repulsingRate;
                    player.velocity.x=0*repulsingRate;
                }

            }

            if(player.repulsedTimes == 2){
                player.setCenter(playerCenter);//cancel
                player.x -= player.velocity.x*repulsingRate;
                player.y -= player.velocity.y*repulsingRate;
            }

            player.repulsedTimes += 1;
            player.stunnedTimeLeft =0.5f;

        }


    }


}
