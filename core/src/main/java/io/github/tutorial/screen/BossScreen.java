package io.github.tutorial.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.tutorial.Main;
import io.github.tutorial.entity.Ship;
import io.github.tutorial.manager.BossEntityManager;

public class BossScreen implements Screen {

    public final Main game;

    private final TextureAtlas atlas;
    public float globalTimer;
    public BossEntityManager manager;
    private final TextureRegion background;
    private final TextureRegion lives;
    private final Stage stage;

    private final ProgressBar progressBar;

    private final Skin skin;

    public BossScreen(Main game, Ship ship, TextureAtlas atlas) {
        this.game = game;
        this.atlas = atlas;
        background = atlas.findRegion("background2");
        lives = atlas.findRegion("heart");
        globalTimer = 0f;
        manager = new BossEntityManager(ship, atlas);
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        progressBar = new ProgressBar(0, 100, 1, false, skin);
    }


    @Override
    public void show() {
        progressBar.setPosition((Gdx.graphics.getWidth() - 300f) / 2f, Gdx.graphics.getHeight() - 37f);
        progressBar.setSize(300f, 50f);
        stage.addActor(progressBar);
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    public void input() {
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            manager.getShip().moveUp(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            manager.getShip().moveDown(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            manager.getShip().moveLeft(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            manager.getShip().moveRight(delta);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            manager.createBullet();
        }
    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        float delta = Gdx.graphics.getDeltaTime();
        progressBar.setValue(manager.getBoss().getHealth());
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        short lives = (short) manager.getShip().getLives();
        game.batch.draw(background, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        manager.drawAll(game.batch);
        for (int i = 0; i < lives; i++) {
            game.batch.draw(this.lives, i * .5f, game.viewport.getWorldHeight() - 0.5f, 0.3f, 0.3f);
        }
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    public void logic() {
        if (!manager.getShip().isAlive()) {
            game.setScreen(new DeathScreen(game, atlas));
            this.dispose();
            return;
        }
        if (!manager.getBoss().isAlive()) {
            game.setScreen(new DeathScreen(game, atlas));
            this.dispose();
            return;
        }
        float delta = Gdx.graphics.getDeltaTime();
        manager.updateAll(delta, game.viewport);
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

    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
