package io.github.tutorial.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.tutorial.manager.EntityManager;
import io.github.tutorial.Main;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class GameScreen implements Screen {

    public final Main game;
    public EntityManager entityManager;
    public Texture background;
    public float globalTimer;


    public GameScreen(Main game) {
        entityManager = new EntityManager();
        background = new Texture("background2.jpg");
        this.game = game;
        globalTimer = 0f;
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        globalTimer += delta;
        input();
        logic();
        draw();
    }

    //TODO left click input to fire bullets.
    // standard hitbox, check overlap with asteroid and increment times hit
    // say 3 shots destroys asteroids, will be done in logic()
    public void input() {
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            entityManager.getShip().moveUp(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            entityManager.getShip().moveDown(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            entityManager.getShip().moveLeft(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            entityManager.getShip().moveRight(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            entityManager.flagDebug();
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            entityManager.createBullet();
        }
    }

    public void logic() {
        if (!entityManager.getShip().isAlive()) {
            game.setScreen(new DeathScreen(game));
            this.dispose();
        }
        if (globalTimer >= 45) {
            game.setScreen(new BossScreen(game));
            this.dispose();
        }
        float delta = Gdx.graphics.getDeltaTime();
        entityManager.updateAll(delta, game.viewport, globalTimer);
    }

    public void draw() {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(background, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        entityManager.drawAll(game.batch, globalTimer, game.font, delta);
        game.batch.end();
        entityManager.drawDebug(game.viewport);
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if (width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
        game.viewport.update(width, height, true);
    }


    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        background.dispose();
        entityManager.dispose();
    }
}
