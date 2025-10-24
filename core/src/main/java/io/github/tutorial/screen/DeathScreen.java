package io.github.tutorial.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.tutorial.Main;

import java.awt.*;

public class DeathScreen implements Screen {

    private final Main game;

    private Stage stage;

    public DeathScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Table table = new Table();
        Table title = new Table();
        title.setFillParent(true);
        table.setFillParent(true);
        stage.addActor(table);
        Label label = new Label("GAME", skin);
        label.setColor(Color.RED);
        title.top().add(label).minHeight(400).minWidth(400).center();
        stage.addActor(title);
        TextButton button = new TextButton("Play Again", skin);
        TextButton qButton = new TextButton("Quit", skin);
        qButton.pad(10f);
        button.pad(10f);
        qButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeScreen();
            }
        });
        table.add(button).align(Align.center).minHeight(100).minWidth(200).row();
        table.add(qButton).padTop(10f).align(Align.center).minHeight(100).minWidth(200);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
//        game.viewport.apply();
//        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
//        game.batch.begin();
//        game.font.draw(game.batch, "GAME OVER!", 8, 9);
//        game.font.draw(game.batch, "Press enter to replay.", 8, 8);
//        game.font.draw(game.batch, "Press escape to exit.", 8, 7);
//        game.batch.end();
        stage.act();
        stage.draw();
    }

    private void changeScreen() {
        game.setScreen(new GameScreen(game));
    }



    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
