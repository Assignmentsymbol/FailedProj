package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.ase.maze.gameObjects.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    public String mapData;
    NativeFileChooser fileChooser;
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;


    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;
    /**
     * level of the game;
     */
    public char level = '0';

    // UI Skin
    private Skin skin;
    public boolean gameEnd = false;
    public boolean gameStarted = false;

     public Player player;
     public Array<Wall> walls;
     public Array<Enemy> enemies;
     public Array<Exit> exits;
     public Array<Trap> traps;
    public Array<WalkableSpace> walkableSpaces;
     public Key key;
     public EntryPoint entryPoint;


    public Music backgroundMusic;
    public MusicPlayer musicPlayer;

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
        this.fileChooser = fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin

        player = new Player(200,200,64,64);

        walls = new Array<>();entryPoint = new EntryPoint();
        enemies = new Array<>();key = new Key();
        traps = new Array<>();exits = new Array<>();
        walkableSpaces = new Array<>();

        // Play some background music
        // Background sound
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        musicPlayer = new MusicPlayer();

        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {

        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }
    public void goToCheck(float timeUsed) {

        this.setScreen(new CheckoutPage(this,timeUsed));
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }



    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        gameStarted = true;gameEnd = false;
        backgroundMusic.stop();

        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen

        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;

        }
    }


    /**
     * used for converts the properties inputs to a Map
     */
    public Map<String, String> convertStringToMap(String data) {
        Map<String, String> map = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(data, "\n");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split("=");
            map.put(keyValue[0], keyValue[1]);
        }

        return map;
    }

    private List<Integer> getBoundsOf2Coordinates(Map<String,String> map){
        String [] d3 = map.keySet().toArray(new String[0]);
        int[] xCoordinate = new int[d3.length];
        int[] yCoordinate = new int[d3.length];
        for(int i = 0;i< d3.length;i++){
            xCoordinate[i] = Integer.parseInt(d3[i].split(",")[0]) ;
            yCoordinate[i] = Integer.parseInt(d3[i].split(",")[1]) ;
        }
        int xMax = Arrays.stream(xCoordinate).max().orElse(-1);
        int xMin = Arrays.stream(xCoordinate).min().orElse(-1);
        int yMax = Arrays.stream(yCoordinate).max().orElse(-1);
        int yMin = Arrays.stream(yCoordinate).min().orElse(-1);

        return List.of(xMax,xMin,yMax,yMin);
    }



    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }
    /**
     * used for resets the game elements;
     */
    public void reset(){
        gameEnd = false;
        player.hp = 15;
        player.hasKey = false;
        player.isEmpowered = false;
        gameStarted = false;
        player.keyMusPlayed = false;

    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void formGameElements(){

        enemies.clear();walls.clear();
        Map<String,String> mapG = convertStringToMap(mapData);
        int xMax = getBoundsOf2Coordinates(mapG).get(0);
        int xMin = getBoundsOf2Coordinates(mapG).get(1);
        int yMax = getBoundsOf2Coordinates(mapG).get(2);
        int yMin = getBoundsOf2Coordinates(mapG).get(3);

        //form cover
        for(int i=xMin+1;i<xMax;i++){
            for(int j = yMin+1;j<yMax;j++){
                WalkableSpace walkableSpace = new WalkableSpace((float) i*100,(float)j*100,(float)100,(float)100);
//                System.out.println("1x:"+walkableSpace.x+"y"+walkableSpace.y);
                walkableSpaces.add(walkableSpace);
            }
        }

        //form walls
        for(Map.Entry<String,String> entry:mapG.entrySet()){
            String s1 = entry.getValue();
            String[] coordinate = entry.getKey().split(",",2);
            float x = 100*Float.parseFloat(coordinate[0]);float y = 100*Float.parseFloat(coordinate[1]);
            if(s1.contains("0")||s1.contains("2")) {
                Wall wall = new Wall(x, y, 100, 100);
                walls.add(wall);
//                System.out.println(wall.x+"y"+wall.y);
            }
        }

        //entrypoint and player
        for(Map.Entry<String,String> entry:mapG.entrySet()){
            String s1 = entry.getValue();
            String[] coordinate = entry.getKey().split(",",2);
            float x = 100*Float.parseFloat(coordinate[0]);float y = 100*Float.parseFloat(coordinate[1]);
            if(s1.contains("1")) {
                entryPoint = new EntryPoint(x,y);
                player.x = x+200;player.y = y+200;
            }
        }
        //key
        for(Map.Entry<String,String> entry:mapG.entrySet()){
            String s1 = entry.getValue();
            String[] coordinate = entry.getKey().split(",",2);
            float x = 100*Float.parseFloat(coordinate[0]);float y = 100*Float.parseFloat(coordinate[1]);
            if(s1.contains("5")) {
                key = new Key(x,y);
                Wall keyBody = new Wall(x, y);
                keyBody.isInivisble = true;
                walls.add(keyBody);
            }
        }
        //enemies
        for(Map.Entry<String,String> entry:mapG.entrySet()){
            String s1 = entry.getValue();
            String[] coordinate = entry.getKey().split(",",2);
            float x = 100*Float.parseFloat(coordinate[0]);float y = 100*Float.parseFloat(coordinate[1]);
            if(s1.contains("4")) {
                enemies.add(new Enemy(x,y,30,30));
            }
        }
        //traps
        for(Map.Entry<String,String> entry:mapG.entrySet()){
            String s1 = entry.getValue();
            String[] coordinate = entry.getKey().split(",",2);
            float x = 100*Float.parseFloat(coordinate[0]);float y = 100*Float.parseFloat(coordinate[1]);
            if(s1.contains("3")) {
                traps.add(new Trap(x,y,100,100));
            }
        }
        //exits
        for(Map.Entry<String,String> entry:mapG.entrySet()){
            String s1 = entry.getValue();
            String[] coordinate = entry.getKey().split(",",2);
            float x = 100*Float.parseFloat(coordinate[0]);float y = 100*Float.parseFloat(coordinate[1]);
            if(s1.contains("2")) {
                exits.add(new Exit(x,y,100,100));
            }
        }
        //remove non-walkable spaces
        System.out.println("------------------------------------------"+walkableSpaces.size);
        //issue zone
        int removeCounter = 0;
        int counter2 = 0;
        int wallLiterated_counter = 0;
        for(int i=0;i<walkableSpaces.size;i++){
            for(int j = 0;j<walls.size;j++){
                if(Math.abs(walkableSpaces.get(i).x-walls.get(j).x)<1&&Math.abs(walkableSpaces.get(i).y-walls.get(j).y)<1){
                    //unfixed bug: one element didnt get removed (at(13,10)).
                    boolean b1 = walkableSpaces.removeValue(walkableSpaces.get(i),true);
                    removeCounter +=1;
                    wallLiterated_counter +=1;
                    if(b1){counter2 +=1;}
                }
            }

        }
        System.out.println("rmct"+removeCounter+"ct2:"+counter2+"lited:"+wallLiterated_counter);
//        System.out.println("---77---77---77--77----77----------"+walkableSpaces.get(0).x);
        System.out.println("------------------------------------------"+walkableSpaces.size);
        System.out.println("------------------------------------------"+walls.size);

    }

    /**
     * used for gain power;
     */
    public void cheatingCode(){
        if(Gdx.input.isKeyPressed(Input.Keys.T)&&Gdx.input.isKeyPressed(Input.Keys.U)
        &&Gdx.input.isKeyPressed(Input.Keys.M)){
            player.hp += 10000;
            player.hasKey = true;
            player.isEmpowered = true;
        }
    }

}
