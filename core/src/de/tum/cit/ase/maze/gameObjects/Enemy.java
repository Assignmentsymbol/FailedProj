package de.tum.cit.ase.maze.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.AnimationType;
import de.tum.cit.ase.maze.MazeRunnerGame;
import de.tum.cit.ase.maze.NodeRecorder;
import de.tum.cit.ase.maze.Utils2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Describe an enemy's body and functions
 */
public class Enemy extends Repusible {
    private Texture texture;
    private Sprite sprite;
    float ax = 0;
    float ay = 0;

   // public Vector2 velocity;
    /**
     * record how many times it was repulsed
     */
    //public int repulsedTimes;

    public float stunnedTimeLeft;
    public float seekingTime = 1;

    public AnimationType moveType = AnimationType.DOWN;

    float right, up;//


    public Enemy(float x, float y, float width, float height) {
        super(x, y, width, height);
        texture = new Texture(Gdx.files.internal("basictiles.png"));
        sprite = new Sprite(texture, 36, 36, 16, 16);
        right = x + width;
        up = y + height;
        stunnedTimeLeft = 0;
        velocity = new Vector2(0,0);

        repulsedTimes = 0;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void create() {
        texture = new Texture(Gdx.files.internal("things.png"));
        sprite = new Sprite(texture);
        this.sprite.setPosition(100, 200);
    }
    /**
     * enemy acting
     */
    public void enemyAct(MazeRunnerGame game,boolean moveAble,float delta) {
        Vector2 enemyPosition = new Vector2(x, y);
        Vector2 playerPosition = new Vector2(game.player.x, game.player.y);
        float distance = playerPosition.dst(enemyPosition);
//        if (distance > 550) {
//            enemySeek(game,delta);
//        } else if(moveAble) {
//            enemyChase(game);
//        }
        enemyChaseDijk(game);
        System.out.println("tracing========================x "+this.x);
        if(this.overlaps(game.player)){
            stunnedTimeLeft = 1f;
            game.player.stunnedTimeLeft = 0.5f;
            Vector2 positionDiff = new Vector2();
            Player player = game.player;
                player.justHurt = true;
                Vector2 playerCenter = new Vector2();
                player.getCenter(playerCenter);
                Vector2 playerOriginalCenter = new Vector2(playerCenter.x-player.velocity.x,playerCenter.y-player.velocity.y);

                Vector2 center = new Vector2();
                this.getCenter(center);
                positionDiff = new Vector2(playerOriginalCenter.x-center.x,playerOriginalCenter.y-center.y);

                player.velocity.x += positionDiff.x*0.5f;
                player.velocity.y += positionDiff.y*0.5f;
                this.x -= positionDiff.x*0.4f;
                this.y -= positionDiff.y*0.4f;
                for(Wall wall:game.walls){
                    if(this.overlaps(wall)){
                        this.x += positionDiff.x*0.4f;
                        this.y += positionDiff.y*0.4f;
                    }
                }

                player.stunnedTimeLeft =0.5f;
                game.player.playerPassivelyAct();

        }


    }
    /**
     * enemy seeking, triggered if player isn't nearby
     */
    public void enemySeek(MazeRunnerGame game, float delta) {
        seekingTime += delta;

        velocity = new Vector2(0,0);//reset
        repulsedTimes = 0;
        velocity.x += (float) (Math.sin(seekingTime) * 5*Math.random());
        velocity.y += (float) (Math.cos(seekingTime) * 5*Math.random());

        x += velocity.x; y += velocity.y;


    }

    /**
     * enemy chase player, triggered if player is nearby
     */
    public void enemyChase(MazeRunnerGame game) {
        Player player = game.player;

        velocity = new Vector2(0,0);//reset
        repulsedTimes = 0;
        boolean overlaps = false;


        if (x - player.x > 0) {
            velocity.x = -3;

        }
        if (this.x - player.x < 0) {
            velocity.x = 3;

        }


        if (this.y - player.y > 0) {
            velocity.y = -3;

        }
        if (this.y - player.y < 0) {
            velocity.y = 3;

        }
        for(Wall wall:game.walls){
            Rectangle nextBody = new Rectangle(x+velocity.x,y+velocity.y,30,30);

            if (nextBody.overlaps(wall)){
                overlaps = true;
            }
        }
        if(overlaps){
            double random = Math.random()-0.5;
            float randomD = (float) (random/Math.abs(random));
            float vx = (float) (velocity.y*randomD);
            float vy = (float) (velocity.x);

            velocity.x = vx;
            velocity.y = vy;
        }
        x += velocity.x;y += velocity.y;

    }

    public Vector2 dijkstraVectorCalculate(MazeRunnerGame game){
        //step 0: get current block
        WalkableSpace sourse = new WalkableSpace();
        WalkableSpace playerBlock = new WalkableSpace();
        Array<WalkableSpace> map = game.walkableSpaces;
        //this node
        for(WalkableSpace walkableSpace:game.walkableSpaces){
            if(this.overlaps(walkableSpace)){
                sourse = walkableSpace;
                System.out.println("!!!OverlappedEnemy!!!"+sourse.getClass());
            }
        }
        //player node
        for(WalkableSpace walkableSpace:game.walkableSpaces){
            if(game.player.overlaps(walkableSpace)){
                playerBlock = walkableSpace;
                System.out.println("!!!OverlappedPlayer!!!");
            }
        }
        int initialSize = map.size;
        Map<WalkableSpace,Float> zielMap = new HashMap<>();
        Map<WalkableSpace,Float> pseudoQueue = new HashMap<>();
        Map<WalkableSpace,Boolean> marks = new HashMap<>();
        Map<WalkableSpace,WalkableSpace> prev = new HashMap<>();
        WalkableSpace workin = new WalkableSpace();
        //initializing
        for(WalkableSpace walkableSpace:game.walkableSpaces){
            marks.put(walkableSpace,false);
        }
        for(WalkableSpace walkableSpace:game.walkableSpaces){
            zielMap.put(walkableSpace,999999f);
        }
        for(WalkableSpace walkableSpace:game.walkableSpaces){
            prev.put(walkableSpace,null);
        }
        workin = sourse;
        zielMap.put(workin,0f);

        pseudoQueue.put(sourse,0f);
        //for(int i =0;i<game.walkableSpaces.size;i++){}


        boolean looper = true;
        while(looper)
        {
            //weird size change,reset

            // bug
            workin = pseudoQueue.entrySet().stream().min(
                    new Comparator<Map.Entry<WalkableSpace, Float>>() {
                        @Override
                        public int compare(Map.Entry<WalkableSpace, Float> e1, Map.Entry<WalkableSpace, Float> e2) {
                            return Math.round(e1.getValue()-e2.getValue());
                        }
                    }
            ).get().getKey();
            //System.out.println("workin dist value: "+pseudoQueue.get(workin)+"and777"+workin.equals(sourse)+"ddd "+pseudoQueue.size());

            pseudoQueue.remove(workin);



            marks.put(workin,true);
//            System.out.println("while loopering********** map size is :"+map.size);

//            System.out.println(map.get(0).x+"%%%%%%%%%%%%%%%%"+map.get(72).x);

            int loopcounter1 = 0;int loopcounter2=0;
            for(int i =0;i<map.size;i++){

                if(!marks.get(map.get(i))){
//                    System.out.println("worked !shon%%%%%%%%%%%%");
                }
                //
                if(Utils2.twoAreAdj(workin,map.get(i))){
//                    System.out.println("layer1 passed-----------------");
                    loopcounter1+=1;
//                    System.out.println("loopcounter1 is now&&&&&&&&&&&&&&&&&"+loopcounter1);
                    if(!(marks.get(map.get(i)))) {
                        loopcounter2 +=1;
                        zielMap.put(map.get(i), zielMap.get(workin) + 100);

                        pseudoQueue.put(map.get(i), zielMap.get(workin) + 100);
                        marks.put(map.get(i), true);
                        prev.put(map.get(i), workin);



                    }
                }
            }
           // marks.put(workin,true);
             loopcounter1 = 0;loopcounter2=0;

            if(zielMap.values().stream().noneMatch(e->e>99999f)){
                System.out.println("looper ends endendendendendendendendendendendend");
                looper = false;
            }
            //bug there
//            System.out.println("size ends: "+pseudoQueue.size()+"  999999999999999");
        }

        return Utils2.reversIterate(prev,sourse,playerBlock);







        //sourse: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm



    }



    public void enemyChaseDijk(MazeRunnerGame game){
        Vector2 velocity = dijkstraVectorCalculate(game);
        velocity.x *= 1f; velocity.y *= 1f;

        x += velocity.x;y += velocity.y;
        System.out.println("chasing!!!!!!!!   "+velocity+"    !!!!");
    }



    float D2(Vector3 vector,Vector3 anotherV){
        Vector2 v21 = new Vector2(vector.x,vector.y);
        Vector2 v22 = new Vector2(anotherV.x,anotherV.y);
        return v21.dst2(v22);
    }

    Array<Vector3> getAdjNodes(Array<Vector3> map,Vector3 point){
        Array<Vector3> returns = new Array<>();
        for(Vector3 node:map){
            if(D2(node,point)==10000){
                returns.add(node);
            }
        }
        return returns;
    }



}
