package io.github.tutorial.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.tutorial.entity.Ship;
import io.github.tutorial.manager.BossEntityManager;
import io.github.tutorial.Main;

public class BossScreen implements Screen {

    public final Main game;

    public Texture background;

    public float globalTimer;

    public BossEntityManager manager;

    public BossScreen(Main game, Ship ship) {
        this.game = game;
        this.background = new Texture("background2.jpg");
        globalTimer = 0f;
        manager = new BossEntityManager(ship);
    }


    @Override
    public void show() {

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
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        manager.drawAll(globalTimer, delta, game.batch, game.font);
        game.batch.end();
    }

    public void logic() {
        if(!manager.getShip().isAlive()) {
            game.setScreen(new DeathScreen(game));
            this.dispose();
            return;
        }
        if(!manager.getBoss().isAlive()) {
            game.setScreen(new DeathScreen(game));
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
        background.dispose();
        manager.dispose();
    }
}
