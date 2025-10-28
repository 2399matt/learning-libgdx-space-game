package io.github.tutorial.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.tutorial.Main;

public class DeathScreen implements Screen {

    private final Main game;
    private final Stage stage;
    private final TextureAtlas atlas;

    public DeathScreen(Main game, TextureAtlas atlas) {
        this.game = game;
        this.atlas = atlas;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        // atlas, skin, custom bitmapfont.
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        Table table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setFillParent(true);
        stage.addActor(table);
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.font, Color.WHITE);
        Label label = new Label("GAME", titleStyle);
        table.add(label).padBottom(40f).row();
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
        stage.act();
        stage.draw();
    }

    private void changeScreen() {
        game.setScreen(new GameScreen(game, atlas));
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
