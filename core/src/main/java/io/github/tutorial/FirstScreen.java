package io.github.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    public final Main game;

    public Texture jahTexture;

    public Texture asteroidTexture;

    public Array<Sprite> asteroids;

    public Sprite jahSprite;

    public static final float JAH_SPEED = 9f;

    public static final float ASTEROID_SPEED = 4f;

    public static float asteroidTimer = 0;

    public Rectangle asteroidHitBox;

    public Rectangle jahHitBox;

    public Sound oof;

    private boolean debug = false;

    private ShapeRenderer shapeRenderer;

    Array<Explosion> explosions;

    float x;
    float y;

    public FirstScreen(Main game) {
        jahTexture = new Texture("jah.jfif");
        asteroidTexture = new Texture("asteroid.png");
        oof = Gdx.audio.newSound(Gdx.files.internal("oof.mp3"));
        jahSprite = new Sprite(jahTexture);
        jahSprite.setSize(1, 1);
        asteroids = new Array<>();
        jahHitBox = new Rectangle();
        asteroidHitBox = new Rectangle();
        shapeRenderer = new ShapeRenderer();
        explosions = new Array<>();
        this.game = game;
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic();
        draw();
        drawDebug();
    }

    public void input() {
        float delta = Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            jahSprite.translateY(JAH_SPEED * delta);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            jahSprite.translateY(-JAH_SPEED * delta);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            jahSprite.translateX(-JAH_SPEED * delta);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            jahSprite.translateX(JAH_SPEED * delta);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
        }
    }

    public void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        jahSprite.setX(MathUtils.clamp(jahSprite.getX(), 0, game.viewport.getWorldWidth()-jahSprite.getWidth()));
        jahSprite.setY(MathUtils.clamp(jahSprite.getY(), 0, game.viewport.getWorldHeight()-jahSprite.getHeight()));
        jahHitBox.set(jahSprite.getBoundingRectangle());
        for(int i = asteroids.size-1; i >= 0; i--) {
            Sprite asteroid = asteroids.get(i);
            asteroid.translateY(-ASTEROID_SPEED * delta);
            if(asteroid.getY() < -asteroid.getHeight()) {
                asteroids.removeIndex(i);
            }
            asteroidHitBox.set(asteroid.getBoundingRectangle());
            if(asteroidHitBox.overlaps(jahHitBox)) {
                oof.play(0.5f);
                explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                asteroids.removeIndex(i);
                continue;
            }

        }
        asteroidTimer += delta;
        if(asteroidTimer > 0.5f) {
            generateAsteroids();
            asteroidTimer = 0;
        }
    }

    public void draw(){
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        jahSprite.draw(game.batch);
        for(Sprite asteroid : asteroids) {
            asteroid.draw(game.batch);
        }
        for(Explosion e : explosions) {
            e.sprite.draw(game.batch);
        }
        game.batch.end();
    }


    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
        game.viewport.update(width, height, true);
    }

    public void generateAsteroids() {
        float worldHeight = game.viewport.getWorldHeight();
        float worldWidth = game.viewport.getWorldWidth();
        Sprite asteroid = new Sprite(asteroidTexture);
        asteroid.setSize(0.5f,0.5f);
        asteroid.setX(MathUtils.random(0f, worldWidth - 1));
        asteroid.setY(worldHeight);
        asteroids.add(asteroid);
    }

    public void drawDebug() {
        if(!debug) return;
        shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(jahSprite.getX(), jahSprite.getY(), jahSprite.getWidth(), jahSprite.getHeight());
        shapeRenderer.setColor(Color.GREEN);
        for(Sprite asteroid : asteroids) {
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
        jahTexture.dispose();
        shapeRenderer.dispose();
        asteroids.clear();
    }
}
