package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.maze.gameObjects.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;


import java.util.Map;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;

    private MazeRunnerGame game;


    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        this.game = game;//
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Welcome to the Maze!", game.getSkin(), "title")).padBottom(80).row();

        // Create and add a button to go to the game screen
        TextButton newGameButton = new TextButton("New Game", game.getSkin());

        table.add(newGameButton).width(300).height(80).row();
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileHandle file = Gdx.files.local("maps/level-1.properties");
                game.mapData = file.readString();
                game.level= file.name().charAt(6);
                System.out.println(game.level);
                game.reset();
                game.formGameElements();
                game.gameStarted = true;
                game.goToGame();
            }
        });
        TextButton goToGameButton = new TextButton("Back To Game", game.getSkin());

        table.add(goToGameButton).width(300).height(80).row();

        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(game.gameStarted) {
                    game.goToGame();
                }
            }
        });
        TextButton fileChooserButton = new TextButton("Choose file", game.getSkin());
        table.add(fileChooserButton).width(300).height(80).row();

        fileChooserButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                NativeFileChooserConfiguration conf= new NativeFileChooserConfiguration();
                conf.directory = Gdx.files.local("maps");
                conf.title = "choose a file";
                NativeFileChooserCallback callback = new NativeFileChooserCallback() {
                    @Override
                    public void onFileChosen(FileHandle file) {

                        //split the inputs
                        game.mapData = file.readString();
                        game.level= file.name().charAt(6);
                        System.out.println("String inputted");
                        game.formGameElements();
                        game.gameEnd = false;

                        game.gameStarted = true;
                        game.goToGame();



                    }

                    @Override
                    public void onCancellation() {

                    }

                    @Override
                    public void onError(Exception exception) {

                    }
                };
                game.fileChooser.chooseFile(conf,callback);

            }
        });
        TextButton exitGameButton = new TextButton("Exit", game.getSkin());
        exitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        table.add(exitGameButton).width(300).height(80).row();

    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage

        stage.draw(); // Draw the stage
    }




    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.clear();
    }
}
