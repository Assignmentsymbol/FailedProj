package de.tum.cit.ase.maze.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * describe a wall's physical body and functions
 */
public class Wall extends Rectangle {

    Texture texture; public Sprite sprite;
    public boolean isInivisble = false;




    public Wall(float x, float y) {
        super(x, y, 100, 100);
        texture =  new Texture(Gdx.files.internal("basictiles.png"));
        sprite = new Sprite(texture,0,0,16,16);
    }

    public Wall(float x, float y, float width, float height) {
        super(x, y, width, height);
        texture =  new Texture(Gdx.files.internal("basictiles.png"));
        sprite = new Sprite(texture,0,0,16,16);

    }
    /**
     * simulating Coulomb force.
     */
//    public void repulsing(Player player){
//        Vector2 positionDiff = new Vector2();
//        if(this.overlaps(player)){
//            Vector2 playerCenter = new Vector2();
//            player.getCenter(playerCenter);
//            Vector2 playerOriginalCenter = new Vector2(playerCenter.x-player.velocity.x,playerCenter.y-player.velocity.y);
//
//            Vector2 wallCenter = new Vector2();
//            this.getCenter(wallCenter);
//            positionDiff = new Vector2(playerOriginalCenter.x-wallCenter.x,playerOriginalCenter.y-wallCenter.y);
//
//            if (Math.abs(positionDiff.x) > Math.abs(positionDiff.y)) {
//                //since all the walls are square, it shows the direction of the repulsion. Here it will be horizontal.
//                player.x -= player.velocity.x;
//            }
//            if (Math.abs(positionDiff.x) < Math.abs(positionDiff.y)) {
//                player.y -= player.velocity.y;
//            }
//
//            if(player.repulsedTimes==1){
//
//                playerOriginalCenter = new Vector2(playerCenter.x,playerCenter.y);
//                player.getCenter(playerCenter);
//                Vector2 v3 = playerCenter.sub(playerOriginalCenter);
//
//                if(v3.y!=0){
//                player.x += player.velocity.x;
//                    player.velocity.y=0;
//                }
//                if(v3.x !=0) {
//                    player.y += player.velocity.y;
//                    player.velocity.x=0;
//                }
//
//            }
//
//            if(player.repulsedTimes == 2){
//                player.setCenter(playerCenter);//cancel
//                player.x -= player.velocity.x;
//                player.y -= player.velocity.y;
//            }
//
//            player.repulsedTimes += 1;
//
//        }
//
//    }
    /**
     * simulating Coulomb force but enemies specific edition
     */
//    public void repulsing(Enemy enemy){
//            Vector2 positionDiff;
//            if(this.overlaps(enemy)){
//                Vector2 playerCenter = new Vector2();
//                enemy.getCenter(playerCenter);
//                Vector2 playerOriginalCenter = new Vector2(playerCenter.x-enemy.velocity.x,playerCenter.y-enemy.velocity.y);
//
//                Vector2 wallCenter = new Vector2();
//                this.getCenter(wallCenter);
//                positionDiff = new Vector2(playerOriginalCenter.x-wallCenter.x,playerOriginalCenter.y-wallCenter.y);
//
//                if (Math.abs(positionDiff.x) > Math.abs(positionDiff.y)) {
//                    //since all the walls are square, it shows the direction of the repulsion. Here it will be horizontal.
//                    enemy.x -= enemy.velocity.x;
//                }
//                if (Math.abs(positionDiff.x) < Math.abs(positionDiff.y)) {
//                    enemy.y -= enemy.velocity.y;
//                }
//
//                if(enemy.repulsedTimes==1){
//
//                    playerOriginalCenter = new Vector2(playerCenter.x,playerCenter.y);
//                    enemy.getCenter(playerCenter);
//                    Vector2 v3 = playerCenter.sub(playerOriginalCenter);
//
//                    if(v3.y!=0){
//                        enemy.x += enemy.velocity.x;
//                        enemy.velocity.y=0;
//                    }
//                    if(v3.x !=0) {
//                        enemy.y += enemy.velocity.y;
//                        enemy.velocity.x=0;
//                    }
//
//                }
//
//                if(enemy.repulsedTimes == 2){
//                    enemy.setCenter(playerCenter);//cancel
//                    enemy.x -= enemy.velocity.x;
//                    enemy.y -= enemy.velocity.y;
//                }
//
//                enemy.repulsedTimes += 1;
//
//            }
//
//
//}



    public void repulsing(Repusible player){


        Vector2 positionDiff = new Vector2();
        if(this.overlaps(player)){
            Vector2 playerCenter = new Vector2();
            player.getCenter(playerCenter);
            Vector2 playerOriginalCenter = new Vector2(playerCenter.x-player.velocity.x,playerCenter.y-player.velocity.y);

            Vector2 wallCenter = new Vector2();
            this.getCenter(wallCenter);
            positionDiff = new Vector2(playerOriginalCenter.x-wallCenter.x,playerOriginalCenter.y-wallCenter.y);

            if (Math.abs(positionDiff.x) > Math.abs(positionDiff.y)) {
                //since all the walls are square, it shows the direction of the repulsion. Here it will be horizontal.
                player.x -= player.velocity.x;
                System.out.println("type1"+player.velocity);
            }
            if (Math.abs(positionDiff.x) < Math.abs(positionDiff.y)) {
                player.y -= player.velocity.y;
                System.out.println("type2"+player.velocity);
            }

            if(player.repulsedTimes==1){

                playerOriginalCenter = new Vector2(playerCenter.x,playerCenter.y);
                player.getCenter(playerCenter);
                Vector2 v3 = playerCenter.sub(playerOriginalCenter);

                if(v3.y!=0){
                    player.x += player.velocity.x;
                    player.velocity.y=0;
                }
                if(v3.x !=0) {
                    player.y += player.velocity.y;
                    player.velocity.x=0;
                }

            }

            if(player.repulsedTimes == 2){

                player.setCenter(playerCenter);//cancel
                player.x -= player.velocity.x;
                player.y -= player.velocity.y;
            }

            player.repulsedTimes += 1;

        }

    }

}
