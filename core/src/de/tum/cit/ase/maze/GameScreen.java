package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.ase.maze.gameObjects.*;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;


    private boolean cameraLocked = false;
    private boolean needReset = true;
    private final BitmapFont font;

    public float sinusInput = 0f;
    public float sinusInput2 = 0f;
    public float sinusInput3 = 0f;

    private boolean keyMusicPlayed = false;

    private Vector2 mapCenter;

    private Rectangle mainVision = new Rectangle();

    public float timeRecorder = 0;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;
        camera.position.x = 0;
        camera.position.y = 0;
        camera.update();
        mapCenter= calculateMapCenter(game);


        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
    }


    /**
     * game rendering with camera control logics
     */    @Override
    public void render(float delta) {
        //adjusting camera
        if(needReset){
            camera.position.x=mapCenter.x;
            camera.position.y=mapCenter.y;
            camera.zoom = 2.2f;
            camera.update();
            needReset = false;

        }

        mainVision.setWidth(800*0.8f*camera.zoom);
        mainVision.setHeight(800*0.8f*camera.zoom);
        mainVision.setCenter(camera.position.x,camera.position.y);

        float deltaX = (game.player.x+game.player.width/2)-(mainVision.x+mainVision.width/2);
        float deltaY = (game.player.y+game.player.height/2)-(mainVision.y+mainVision.height/2);
        if(!game.player.overlaps(mainVision)){
            camera.position.x+=deltaX*0.01f;
            camera.position.y+=deltaY*0.01f;
            camera.update();
        }

        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            camera.zoom = 0.75f;camera.update();
            game.getSpriteBatch().setProjectionMatrix(camera.combined);
            game.goToMenu();
        }

        //more camera controls and adjusting
        if(Gdx.input.isKeyPressed(Input.Keys.F1)) {camera.zoom += 0.04f;camera.update();}
        if(Gdx.input.isKeyPressed(Input.Keys.F2)&&camera.zoom>0.5) {camera.zoom -= 0.02f;camera.update();}
        if(Gdx.input.isKeyPressed(Input.Keys.F3)) {
            camera.position.x = calculateMapCenter(game).x;
            camera.position.y = calculateMapCenter(game).y;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {camera.position.y += 20;}
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {camera.position.y -= 20;}
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {camera.position.x -= 20;}
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {camera.position.x += 20;}
        boolean cameraLockChanged = false;
        if(Gdx.input.isKeyJustPressed(Input.Keys.Y)&&!cameraLocked) {cameraLocked = true;cameraLockChanged = true;}
        if(Gdx.input.isKeyJustPressed(Input.Keys.Y)&&cameraLocked&&!cameraLockChanged) {cameraLocked = false;}
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)||cameraLocked) {
            camera.position.x = game.player.x;
            camera.position.y = game.player.y;
        }
        camera.update();



        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        game.getSpriteBatch().setProjectionMatrix(camera.combined);



        //call acts and moves;
        game.player.stunnedTimeLeft -= delta;
        if(game.player.stunnedTimeLeft<=0) {
            game.player.playerAct(game,this);//actively part
        }
        for(Enemy enemy:game.enemies){
            enemy.stunnedTimeLeft -= delta;
            boolean moveAble = true;
            if(enemy.stunnedTimeLeft>0) {
                moveAble = false;
            }
            enemy.enemyAct(game,moveAble,delta);
        }

        for (Wall wall: game.walls){
         wall.repulsing(game.player);
         for(Enemy enemy: game.enemies){
             wall.repulsing(enemy);
         }
        }
        for (Wall wall: game.walls){
           wall.repulsing(game.player);
            for(Enemy enemy: game.enemies){
                wall.repulsing(enemy);
            }
        }

        //something dirty
        for(Enemy enemy: game.enemies){
            for(int i =0;i<game.enemies.size;i++){
                if(game.enemies.get(i)!=enemy&&game.enemies.get(i).overlaps(enemy)){
                    enemy.x -= enemy.velocity.x;
                    enemy.y -= enemy.velocity.y;
                }
            }
        }

        for(Trap trap:game.traps){
            trap.repulsing(game.player);
        }


        //starts drawing
        game.getSpriteBatch().begin();

        wallsRendering(game.walls);
//        for(WalkableSpace walkableSpace: game.walkableSpaces){
//            Texture texture =  new Texture(Gdx.files.internal("things.png"));
//            game.getSpriteBatch().draw(new Sprite(texture),walkableSpace.x,walkableSpace.y,walkableSpace.width,walkableSpace.height);
//        }
        trapsRendering(game.traps);
        exitsRendering(game.exits);
        hudRendering();

        game.getSpriteBatch().draw(game.entryPoint.getSprite(), game.entryPoint.x,game.entryPoint.y,
                game.entryPoint.width,game.entryPoint.height);

        if(!game.player.hasKey) {
            game.getSpriteBatch().draw(game.key.getSprite(), game.key.x, game.key.y,
                    game.key.width, game.key.height);
        }

        if(game.player.hasKey) {
            Texture texture =  new Texture(Gdx.files.internal("things.png"));
            Sprite newSprite = new Sprite(texture,16*5,16*4,16,16);
            game.getSpriteBatch().draw(newSprite, game.key.x, game.key.y,
                    game.key.width, game.key.height);
        }

        if(!game.player.isAttacking) {
            drawPlayerAnimation(delta);
        }
        else{drawPlayerAttacksAnimation(delta);}

        if(game.enemies.size<8) {
            enemiesRendering(game.enemies, delta);
        }
        else {enemiesRendering(game.enemies);}

        for(Exit exit:game.exits) {
            if (game.player.hasKey && game.player.overlaps(exit)){
                ScreenUtils.clear(0,0,0,1);
                camera.update();game.gameEnd = true;
                game.musicPlayer.win.play();
                game.goToCheck(timeRecorder);
            }
        }
        if(game.player.hp<=0){
            game.musicPlayer.death.play();
            ScreenUtils.clear(0,0,0,1);
            camera.update();game.gameEnd = true;
            game.goToCheck(timeRecorder);
        }

        game.getSpriteBatch().end(); // Important to call this after drawing everything

        timeRecorder += delta;

        game.cheatingCode();


    }
    /**
     * call to draw player moving animation.
     */
    private void drawPlayerAnimation(float delta){

        TextureRegion stillPic ;
        stillPic = AnimationsLoader.getCharacterAnimation(game.player.moveType).getKeyFrame(sinusInput, true);



        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            sinusInput += delta;
            game.player.moveType = AnimationType.UP;
            game.getSpriteBatch().draw(
                    AnimationsLoader.getCharacterAnimation(game.player.moveType).getKeyFrame(sinusInput, true),                    game.player.x,
                    game.player.y-20,
                    64,
                    128
            );


        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            sinusInput += delta;
            game.player.moveType = AnimationType.LEFT;
            game.getSpriteBatch().draw(
                    AnimationsLoader.getCharacterAnimation(game.player.moveType).getKeyFrame(sinusInput, true),
                    game.player.x,
                    game.player.y-10,
                    64,
                    128
            );

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            sinusInput += delta;
            game.player.moveType = AnimationType.DOWN;
            game.getSpriteBatch().draw(
                    AnimationsLoader.getCharacterAnimation(game.player.moveType).getKeyFrame(sinusInput, true),                    game.player.x,
                    game.player.y-10,
                    64,
                    128
            );

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            sinusInput += delta;
            game.player.moveType = AnimationType.RIGHT;
            game.getSpriteBatch().draw(
                    AnimationsLoader.getCharacterAnimation(game.player.moveType).getKeyFrame(sinusInput, true),                    game.player.x,
                    game.player.y-10,
                    64,
                    128
            );


        }

        game.getSpriteBatch().draw(
                stillPic,
                game.player.x,
                game.player.y-10,
                64,
                128
        );

    }
    /**
     * call to draw player attacking animation.
     */
    private void drawPlayerAttacksAnimation(float delta){

        if(Gdx.input.isKeyPressed(Input.Keys.J)) {
            sinusInput3 += delta;
            if(game.player.moveType == AnimationType.DOWN) {
                game.player.attacksType = AnimationType.DoAtt;
            }
            if(game.player.moveType == AnimationType.UP) {
                game.player.attacksType = AnimationType.UpAtt;
            }
            if(game.player.moveType == AnimationType.LEFT) {
                game.player.attacksType = AnimationType.LeAtt;
            }
            if(game.player.moveType == AnimationType.RIGHT) {
                game.player.attacksType = AnimationType.RiAtt;
            }
            game.getSpriteBatch().draw(
                    AnimationsLoader.getCharacterAnimation(game.player.attacksType).getKeyFrame(sinusInput3, true), game.player.x,
                    game.player.y - 20,
                    128,
                    128
            );

        }


    }

    /**
     * call to draw walls
     */
    private void wallsRendering(Array<Wall> walls){
        for(Wall wall: walls){
            if(!wall.isInivisble) {
                game.getSpriteBatch().draw(wall.sprite, wall.x, wall.y, wall.width, wall.height);
            }
        }

    }
    /**
     * call to draw traps
     */
    private void trapsRendering(Array<Trap> traps){
        for(Trap trap: traps){
            game.getSpriteBatch().draw(trap.sprite,trap.x-30,trap.y-30,trap.width+60,trap.height+60);
        }

    }
    /**
     * call to draw exits
     */
    private void exitsRendering(Array<Exit> exits){
        for(Exit exit: exits){
            if(game.player.hasKey){
                exit.sprite = new Sprite(exit.sprite.getTexture(),0,16,16,16);
                for(Wall wall: game.walls){
                    if(wall.overlaps(exit)){
                        wall.setCenter(mapCenter.x+100000,mapCenter.y+100000);
                    }
                }
            }
            game.getSpriteBatch().draw(exit.sprite,exit.x,exit.y,exit.width,exit.height);
        }


    }
    /**
     * call to draw enemies
     */
    private void enemiesRendering(Array<Enemy> enemies){// old vision
        for(Enemy enemy: enemies){
            game.getSpriteBatch().draw(
                    AnimationsLoader.getEnemySimpleAnimation().getKeyFrame(enemy.seekingTime, true),
                    enemy.x,
                    enemy.y,
                    64,
                    128
            );
        }
    }
    /**
     * call to draw enemies with simpler animation
     */
    private void enemiesRendering(Array<Enemy> enemies,float delta){
        for(Enemy enemy: enemies){
            enemyRendering(enemy,delta);
        }
    }
    /**
     * call to draw one single enemy
     */
    private void enemyRendering(Enemy enemy,float delta){
        TextureRegion stillPic = AnimationsLoader.getEnemyAnimation(AnimationType.DOWN).getKeyFrame(sinusInput2, true);

        if(enemy.moveType == AnimationType.UP){stillPic = AnimationsLoader.getEnemyAnimation(enemy.moveType).getKeyFrame(sinusInput2, true);}
        if(enemy.moveType == AnimationType.LEFT){stillPic = AnimationsLoader.getEnemyAnimation(enemy.moveType).getKeyFrame(sinusInput2, true);}
        if(enemy.moveType == AnimationType.DOWN){stillPic = AnimationsLoader.getEnemyAnimation(enemy.moveType).getKeyFrame(sinusInput2, true);}
        if(enemy.moveType == AnimationType.RIGHT){stillPic = AnimationsLoader.getEnemyAnimation(enemy.moveType).getKeyFrame(sinusInput2, true);}


        if(enemy.velocity.y>0) {
            sinusInput2 += delta*0.5f;
            enemy.moveType = AnimationType.UP;
            game.getSpriteBatch().draw(
                    AnimationsLoader.getEnemyAnimation(enemy.moveType).getKeyFrame(sinusInput2, true),
                    enemy.x,
                    enemy.y-20,
                    64,
                    64
            );


        }
        else if(enemy.velocity.x<0) {
            sinusInput2 += delta*0.5f;
            enemy.moveType = AnimationType.LEFT;
            game.getSpriteBatch().draw(
                    AnimationsLoader.getEnemyAnimation(enemy.moveType).getKeyFrame(sinusInput2, true),
                    enemy.x,
                    enemy.y-10,
                    64,
                    64
            );

        }
        else if(enemy.velocity.y<0) {
            sinusInput2 += delta*0.5f;
            enemy.moveType = AnimationType.DOWN;
            game.getSpriteBatch().draw(
                    AnimationsLoader.getEnemyAnimation(enemy.moveType).getKeyFrame(sinusInput2, true),
                    enemy.x,
                    enemy.y-10,
                    64,
                    64
            );

        }
        else if(enemy.velocity.x>0) {
            sinusInput2 += delta*0.5f;
            enemy.moveType = AnimationType.RIGHT;
            game.getSpriteBatch().draw(
                    AnimationsLoader.getEnemyAnimation(enemy.moveType).getKeyFrame(sinusInput2, true),
                    enemy.x,
                    enemy.y-10,
                    64,
                    64
            );

        }

        game.getSpriteBatch().draw(
                stillPic,
                enemy.x,
                enemy.y-10,
                64,
                64
        );

    }


    /**
    * used for draw the HUD;
    */
    private void hudRendering(){
        Vector3 topLeft = new Vector3(0,0,0);
        Vector3 botLeft = new Vector3(0,camera.viewportHeight,0);
        camera.unproject(topLeft);camera.unproject(botLeft);
        BitmapFont hUDFont = game.getSkin().getFont("font");
        float base = 1.0f;
        float zoomFactor = 1.0f + (camera.zoom - 1.0f);

        hUDFont.getData().setScale(zoomFactor*base);
        if(game.player.hp>20000){hUDFont.draw(game.getSpriteBatch(), "Hp: "+"???",topLeft.x,topLeft.y);}
        else {
            hUDFont.draw(game.getSpriteBatch(), "Hp: " + game.player.hp, topLeft.x, topLeft.y);
        }

        if(game.player.hasKey) {
            hUDFont.draw(game.getSpriteBatch(), "Door Opened!", topLeft.x + 150 * zoomFactor, topLeft.y);
        }
        if(!game.player.hasKey) {
            hUDFont.draw(game.getSpriteBatch(), "Door not opened yet", topLeft.x + 150 * zoomFactor, topLeft.y);
        }
            hUDFont.draw(game.getSpriteBatch(), "Time used:"+(int)timeRecorder, topLeft.x + 550 * zoomFactor, topLeft.y);
    }

    /**
     * calculate map center
     */
    public Vector2 calculateMapCenter(MazeRunnerGame game){

        float bot=game.walls.get(0).y;
        float left=game.walls.get(0).x;
        float top=bot;
        float right=left;

        for(Wall wall:game.walls){
            if(wall.y+wall.height>top){
                top = wall.y+wall.height;
            }
            if(wall.y<bot){
                bot = wall.y;
            }
            if(wall.x+wall.width>right){
                right = wall.x+wall.width;
            }
            if(wall.x<left){
                left = wall.x;
            }
        }
        if(game.entryPoint.y+game.entryPoint.height==top){
            Wall edge = new Wall(game.entryPoint.x,top+100,100,100);
            edge.isInivisble = true;
            game.walls.add(edge);
        }
        if(game.entryPoint.y==bot){
            Wall edge = new Wall(game.entryPoint.x,bot-100,100,100);
            edge.isInivisble = true;
            game.walls.add(edge);
        }
        if(game.entryPoint.x+game.entryPoint.width==right){
            Wall edge = new Wall(right+100,game.entryPoint.y,100,100);
            edge.isInivisble = true;
            game.walls.add(edge);
        }
        if(game.entryPoint.x==left){
            Wall edge = new Wall(left-100,game.entryPoint.y,100,100);
            edge.isInivisble = true;
            game.walls.add(edge);
        }

        return new Vector2((left+right)/2,(top+bot)/2);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }


}
