package de.tum.cit.ase.maze;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.gameObjects.WalkableSpace;

import java.util.Map;
import java.util.PriorityQueue;

public class Utils2 {
    public static boolean twoAreAdj(WalkableSpace w1, WalkableSpace w2){
        if(w1.equals(w2)){
            System.out.println("same object false^$$$$$$$$$$$$$$$$");
            return false;}
        if(w1.overlaps(w2)){
            System.out.println("EnemyUtil adjTest: overlapped+++true+++++++++++++++");
            return true;
        }
        if(Math.abs(w1.x-w2.x)+Math.abs(w1.y-w2.y)<101){
            System.out.println("EnemyUtil adjTest: very close+++true+++++++++++++++");
            return true;
        }

        return false;
    }

    public void iteratorAdj(PriorityQueue<WalkableSpace> set, WalkableSpace walkableSpace){

    }

    public static Vector2 reversIterate(Map<WalkableSpace,WalkableSpace> prev, WalkableSpace source, WalkableSpace player){
        if(prev.isEmpty()&&!source.equals(player)){
            /// didnt work method
            throw new RuntimeException("reversIterate runs wrongly.");
        }
        WalkableSpace u =player;
        Array<WalkableSpace> path = new Array<>();

        while(u!=null){
            path.add(u);
            u=prev.get(u);
            System.out.println("prev of u "+prev.get(u));
        }


        Vector2 diff = new Vector2(
                path.get(path.size-2).x-source.x,path.get(path.size-2).y-source.y
        );

        System.out.println("Size: "+path.size+"prev size"+prev.size());
        System.out.println("diff^^^^^^^^^"+diff);
        System.out.println("playerBlock^^^^^^^^^"+player);

        System.out.println("v==========444================444=========  "+getDirection(diff));


        return getDirection(diff);


    }

    private static Vector2 getDirection(Vector2 vector2){
        if(vector2.x==0&&vector2.y==0){
            System.out.println("getDirection(0,0)");
            return new Vector2(0f,0f);
        }
        else if(vector2.x==0){
            System.out.println("getDirection(x=0)");
            return new Vector2(0f,Math.abs(vector2.y)/vector2.y);
        }
        else if(vector2.y==0){
            System.out.println("getDirection(y=0)");
            return new Vector2(Math.abs(vector2.x)/vector2.x,0);
        }
        System.out.println("XXXXXXXXXXXXXing!!##!!##!!##!!##!!##");
        return new Vector2(Math.abs(vector2.x)/vector2.x,Math.abs(vector2.y)/vector2.y);
    }





}
