package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.gameObjects.*;

import java.util.Map;

/**
 * shows when a game is finished, 
 */
public class CheckoutPage implements Screen {

    private final Stage stage;

    private MazeRunnerGame game;

    public CheckoutPage(MazeRunnerGame game,float timeUsed) {
        this.game = game;
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage
        Label win = new Label("You Win!",game.getSkin());
        if(game.player.hp<=0){
            win.setText("You died!");
        }

        Label time = new Label("Time Used: "+timeUsed+" s",game.getSkin());
        win.setAlignment(Align.center);
        time.setAlignment(Align.center);


        TextButton nextGameButton = new TextButton("Next level", game.getSkin());
        nextGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                camera.zoom = 2.2f;
                FileHandle file = Gdx.files.local("maps/level-1.properties");
                if(game.level=='1'){file = Gdx.files.local("maps/level-3.properties");}
                if(game.level=='2'){file = Gdx.files.local("maps/level-3.properties");}
                if(game.level=='3'){file = Gdx.files.local("maps/level-4.properties");}
                if(game.level=='4'){file = Gdx.files.local("maps/level-5.properties");}
                if(game.level=='5'){file = Gdx.files.local("maps/level-1.properties");}

                game.mapData = file.readString();
                game.reset();
                game.formGameElements();
                game.goToGame();
            }
        });

        table.add(win).center().height(200).width(200).row();
        table.add(time).center().height(200).width(200).row();
        if(game.player.hp>0) {
            table.add(nextGameButton);
        }
        else{
            Label label = new Label("Press Esc Back to Menu",game.getSkin());
            label.setAlignment(Align.center);
            table.add(label);

        }
        nextGameButton.align(Align.center);



    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            game.goToMenu();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage

        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
