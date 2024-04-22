package de.tum.cit.ase.maze.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.ase.maze.AnimationType;
import de.tum.cit.ase.maze.GameScreen;
import de.tum.cit.ase.maze.MazeRunnerGame;

/**
 * describe a player's physical body and function
 */
public class Player extends Repusible {

    //public Vector2 velocity; //** unexpected overriding, dont forget the input only adjust the not shadowed one!
    // public int repulsedTimes;//only for the walls somehow its fine to overriding :?
    public boolean hasKey;
    public boolean isEmpowered = false;
    public boolean keyMusPlayed = false;
    public int hp;
    public float stunnedTimeLeft;



    public AnimationType moveType = AnimationType.DOWN;
    public AnimationType attacksType = AnimationType.DoAtt;

    public boolean justHurt,isAttacking;






    public Player(float x, float y, float width, float height) {
        super(x, y, width, height);
        velocity = new Vector2(0,0);
        hasKey = false;
        repulsedTimes = 0;//only for the walls
        hp = 15;
        justHurt = false;
        isAttacking = false;
        stunnedTimeLeft=0;


    }



    /**
     * player acting
     */
    public void playerAct(MazeRunnerGame game,GameScreen screen){
        if(!justHurt){
            playerMove(game);
        }
        if(isEmpowered) {
            playerAttacks(game);
        }
        playerOnEvents(game);
        resets();

    }
    /**
     * simulating an acceleration process.
     */
    public void playerMove(MazeRunnerGame game){
        //input will simulate a short acceleration;
        velocity = new Vector2(0,0);//reset
        repulsedTimes = 0;


        if (Gdx.input.isKeyPressed(Input.Keys.W)){velocity.y+=12;moveType = AnimationType.UP;}
        if (Gdx.input.isKeyPressed(Input.Keys.S)){velocity.y-=12;moveType = AnimationType.DOWN;}
        if (Gdx.input.isKeyPressed(Input.Keys.A)){velocity.x-=12;moveType = AnimationType.LEFT;}
        if (Gdx.input.isKeyPressed(Input.Keys.D)){velocity.x+=12;moveType = AnimationType.RIGHT;}
        if(velocity.dst(0.0f,0.0f)>12){
            // keep the velocity barely changed in case of simultaneously pressing;
            velocity.x *= 0.7f;
            velocity.y *= 0.7f;
        }
        System.out.println("v="+velocity);
        System.out.println("vx="+velocity.x);
        System.out.println("vy="+velocity.y);
        // since the unit is 1 delta, directly move the position;
        x += velocity.x;y += velocity.y;
    }

    public void playerPassivelyAct(){
        x += velocity.x;y += velocity.y;
    }
    /**
     * player meet something nice
     */
    public void playerOnEvents(MazeRunnerGame game){
        if(game.key.overlaps(this)){
            if(!keyMusPlayed){
                game.musicPlayer.win.play();
                keyMusPlayed = true;
            }
            hasKey = true;
        }
        if(justHurt){

            game.musicPlayer.hit.play();
            hp -= 1;

        }
        if(game.player.isEmpowered){
            if(!keyMusPlayed){
                game.musicPlayer.win.play();
                keyMusPlayed = true;
            }
            hasKey = true;
        }
    }

    public void resets(){
        justHurt = false;
    }

    public void playerAttacks(MazeRunnerGame game){

        Rectangle attacksRange = new Rectangle(x,y,0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            game.musicPlayer.chop.play();
            isAttacking = true;
            if(attacksType == AnimationType.UpAtt){
                attacksRange = new Rectangle(x,y+30,64,64);
            }
            if(attacksType == AnimationType.DoAtt){
                attacksRange = new Rectangle(x,y-30,64,64);
            }
            if(attacksType == AnimationType.LeAtt){
                attacksRange = new Rectangle(x-30,y,64,64);
            }
            if(attacksType == AnimationType.RiAtt){
                attacksRange = new Rectangle(x+30,y,64,64);
            }
            for(Enemy enemy:game.enemies){
                if(attacksRange.overlaps(enemy)){
                    game.musicPlayer.hitE.play();
                    enemy.x = 10000;
                    enemy.y = 10000;
                }
            }

        }
        else{isAttacking = false;}


    }




}
