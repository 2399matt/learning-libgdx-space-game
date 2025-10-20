package io.github.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {

    public static final float JAH_SPEED = 8f;
    public static final float ASTEROID_SPEED = 4f;
    public static float asteroidTimer = 0;
    public final Main game;
    public Rectangle asteroidHitBox;

    public Rectangle jahHitBox;

    public Sound oof;
    public Array<Asteroid> asteroids;
    public Ship ship;
    public Array<Explosion> explosions;
    public float globalTimer;
    private boolean debug = false;
    private final ShapeRenderer shapeRenderer;

    public FirstScreen(Main game) {
        ship = new Ship();
        oof = Gdx.audio.newSound(Gdx.files.internal("oof.mp3"));
        asteroids = new Array<>();
        jahHitBox = new Rectangle();
        asteroidHitBox = new Rectangle();
        shapeRenderer = new ShapeRenderer();
        explosions = new Array<>();
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
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic();
        draw();
        drawDebug();
    }

    //TODO left click input to fire bullets.
    // standard hitbox, check overlap with asteroid and increment times hit
    // say 3 shots destroys asteroids, will be done in logic()
    public void input() {
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            ship.sprite.translateY(JAH_SPEED * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            ship.sprite.translateY(-JAH_SPEED * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            ship.sprite.translateX(-JAH_SPEED * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            ship.sprite.translateX(JAH_SPEED * delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
        }
    }

    public void logic() {
        if (!ship.isAlive()) {
            game.setScreen(new DeathScreen(game));
            this.dispose();
        }
        float delta = Gdx.graphics.getDeltaTime();
        ship.sprite.setX(MathUtils.clamp(ship.sprite.getX(), 0, game.viewport.getWorldWidth() - ship.sprite.getWidth()));
        ship.sprite.setY(MathUtils.clamp(ship.sprite.getY(), 0, game.viewport.getWorldHeight() - ship.sprite.getHeight()));
        jahHitBox.set(ship.sprite.getBoundingRectangle());
        for (int i = asteroids.size - 1; i >= 0; i--) {
            Sprite asteroid = asteroids.get(i).sprite;
            asteroid.translateY(-ASTEROID_SPEED * delta);
            if (asteroid.getY() < -asteroid.getHeight()) {
                asteroids.removeIndex(i);
            }
            asteroidHitBox.set(asteroid.getBoundingRectangle());
            if (asteroidHitBox.overlaps(jahHitBox)) {
                oof.play(0.5f);
                explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                asteroids.removeIndex(i);
                ship.takeDamage();
                continue;
            }

        }
        asteroidTimer += delta;
        if (globalTimer > 30f) {
            if (asteroidTimer > 0.2f) {
                generateAsteroids();
                asteroidTimer = 0;
            }
        } else {
            if (asteroidTimer > 0.5f) {
                generateAsteroids();
                asteroidTimer = 0;
            }
        }
    }

    public void draw() {
        float delta = Gdx.graphics.getDeltaTime();
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        ship.sprite.draw(game.batch);
        game.font.draw(game.batch, "Current lives: " + ship.lives, 8, 8);
        game.font.draw(game.batch, globalTimer + "s", 8, 9);
        for (Asteroid asteroid : asteroids) {
            asteroid.sprite.draw(game.batch);
        }
        for (int i = explosions.size - 1; i >= 0; i--) {
            Explosion e = explosions.get(i);
            e.sprite.draw(game.batch);
            e.update(delta);
            if (e.finished) {
                explosions.removeIndex(i);
            }
        }
        game.batch.end();
    }


    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if (width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
        game.viewport.update(width, height, true);
    }

    public void generateAsteroids() {
        float worldHeight = game.viewport.getWorldHeight();
        float worldWidth = game.viewport.getWorldWidth();
        Asteroid asteroid = new Asteroid(MathUtils.random(0f, worldWidth - 1), worldHeight);
        asteroids.add(asteroid);
    }

    public void drawDebug() {
        if (!debug) return;
        shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(ship.sprite.getX(), ship.sprite.getY(), ship.sprite.getWidth(), ship.sprite.getHeight());
        shapeRenderer.setColor(Color.GREEN);
        for (Asteroid a : asteroids) {
            Sprite asteroid = a.sprite;
            shapeRenderer.rect(asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight());
        }
        shapeRenderer.end();
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
        ship.dispose();
        shapeRenderer.dispose();
        for (Asteroid a : asteroids) {
            a.dispose();
        }
        asteroids.clear();
        for (Explosion e : explosions) {
            e.dispose();
        }
        explosions.clear();
        oof.dispose();
    }
}
