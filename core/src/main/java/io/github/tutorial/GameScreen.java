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

/**
 * First screen of the application. Displayed after the application is created.
 */
public class GameScreen implements Screen {

    public static final float SHIP_SPEED = 8f;
    public static final float ASTEROID_SPEED = 4f;
    public static float asteroidTimer = 0f;
    public static float enemyRespawnTimer = 10f;
    public final Main game;
    public Rectangle asteroidHitBox;
    public Texture background;
    public Rectangle jahHitBox;
    public Rectangle bulletHitBox;
    public Rectangle enemyHitBox;
    public Rectangle enemyBulletHitBox;
    public Sound oof;
    public Array<Asteroid> asteroids;
    public Ship ship;
    public Array<Explosion> explosions;
    public Array<ShipBullet> bullets;
    public Array<Enemy> enemies;
    public Array<EnemyBullet> enemyBullets;
    public float globalTimer;
    private boolean debug = false;
    private final ShapeRenderer shapeRenderer;


    public GameScreen(Main game) {
        ship = new Ship();
        oof = Gdx.audio.newSound(Gdx.files.internal("oof.mp3"));
        asteroids = new Array<>();
        jahHitBox = new Rectangle();
        asteroidHitBox = new Rectangle();
        bulletHitBox = new Rectangle();
        enemyHitBox = new Rectangle();
        enemyBulletHitBox = new Rectangle();
        shapeRenderer = new ShapeRenderer();
        explosions = new Array<>();
        bullets = new Array<>();
        enemies = new Array<>();
        enemyBullets = new Array<>();
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
            ship.sprite.translateY(SHIP_SPEED * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            ship.sprite.translateY(-SHIP_SPEED * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            ship.sprite.translateX(-SHIP_SPEED * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            ship.sprite.translateX(SHIP_SPEED * delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            game.laserSound.play(0.5f);
            createBullet();
        }
    }

    public void logic() {
        if (!ship.isAlive()) {
            game.setScreen(new DeathScreen(game));
            this.dispose();
        }
        float delta = Gdx.graphics.getDeltaTime();
        spawnEnemies(delta);
        ship.sprite.setX(MathUtils.clamp(ship.sprite.getX(), 0, game.viewport.getWorldWidth() - ship.sprite.getWidth()));
        ship.sprite.setY(MathUtils.clamp(ship.sprite.getY(), 0, game.viewport.getWorldHeight() - ship.sprite.getHeight()));
        jahHitBox.set(ship.sprite.getBoundingRectangle());
        handleEnemyBulletCollision(jahHitBox);
        for(Enemy e : enemies) {
            e.update(delta);
            if(e.bulletCooldown <= 0f) {
                e.bulletCooldown = 2f;
                enemyBullets.add(new EnemyBullet(e.sprite.getX(), e.sprite.getY() - 0.2f));
            }
        }
        for(int i = enemyBullets.size - 1 ; i >= 0; i--) {
            EnemyBullet enemyBullet = enemyBullets.get(i);
            enemyBullet.update(delta);
            if(enemyBullet.sprite.getY() <= - enemyBullet.sprite.getHeight()) {
                enemyBullets.removeIndex(i);
            }
        }
        for(int i = bullets.size - 1; i >= 0; i--) {
            ShipBullet b = bullets.get(i);
            b.sprite.translateY(5f * delta);
            if(b.sprite.getY() > game.viewport.getWorldHeight() + b.sprite.getHeight()) {
                bullets.removeIndex(i);
            }
        }
        for (int i = asteroids.size - 1; i >= 0; i--) {
            Sprite asteroid = asteroids.get(i).sprite;
            asteroid.translateY(-ASTEROID_SPEED * delta);
            if (asteroid.getY() < -asteroid.getHeight()) {
                asteroids.removeIndex(i);
            }
            asteroidHitBox.set(asteroid.getBoundingRectangle());
            handleShipBulletCollision(asteroids.get(i), asteroidHitBox);
            if(asteroids.get(i).timesHit >= 2) {
                asteroids.removeIndex(i);
            }
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
            if (asteroidTimer > 0.8f) {
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
        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(background, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        ship.sprite.draw(game.batch);
        game.font.draw(game.batch, "Current lives: " + ship.lives, 8, 8);
        game.font.draw(game.batch, globalTimer + "s", 8, 9);
        for (Asteroid asteroid : asteroids) {
            asteroid.sprite.draw(game.batch);
        }
        for(ShipBullet b : bullets) {
            b.sprite.draw(game.batch);
        }
        for(EnemyBullet b : enemyBullets) {
            b.sprite.draw(game.batch);
        }
        for(Enemy e : enemies) {
            e.sprite.draw(game.batch);
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

    public void spawnEnemies(float delta) {
        if(!enemies.isEmpty()) {
            return;
        }
        enemyRespawnTimer += delta;
        if(enemies.isEmpty() && enemyRespawnTimer >= 7f) {
            enemies.add(new Enemy(4f, game.viewport.getWorldHeight() - 0.5f));
            enemies.add(new Enemy(8f, game.viewport.getWorldHeight() - 0.5f));
            enemies.add(new Enemy(12f, game.viewport.getWorldHeight() - 0.5f));
            enemyRespawnTimer = 0f;
        }
    }

    public void handleShipBulletCollision(Asteroid asteroid, Rectangle asteroidBox) {
        for(int i = bullets.size - 1; i >= 0; i--) {
            ShipBullet b = bullets.get(i);
            Sprite bullet = b.sprite;
            bulletHitBox.set(bullet.getBoundingRectangle());
            if(handleEnemyHitCollision(bulletHitBox)) {
                bullets.removeIndex(i);
                break;
            }
            if(bulletHitBox.overlaps(asteroidBox)) {
                asteroid.timesHit++;
                bullets.removeIndex(i);
                break;
            }
        }
    }

    public void handleEnemyBulletCollision(Rectangle shipHitBox) {
        for(int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet e = enemyBullets.get(i);
            enemyBulletHitBox.set(e.sprite.getBoundingRectangle());
            if(enemyBulletHitBox.overlaps(shipHitBox)) {
                explosions.add(new Explosion(ship.sprite.getX(), ship.sprite.getY()));
                enemyBullets.removeIndex(i);
                ship.takeDamage();
            }
        }
    }

    public boolean handleEnemyHitCollision(Rectangle bulletHitBox) {
        if(enemies.isEmpty()) {
            return false;
        }
        for(int i = enemies.size - 1; i >= 0; i--) {
            Sprite enemy = enemies.get(i).sprite;
            enemyHitBox.set(enemy.getBoundingRectangle());
            if(enemyHitBox.overlaps(bulletHitBox)) {
                enemies.get(i).takeDamage();
                if(enemies.get(i).timesHit >= 2) {
                    enemies.removeIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    public void createBullet() {
        ShipBullet shipBullet = new ShipBullet(ship.sprite.getX(), ship.sprite.getY() + 1);
        bullets.add(shipBullet);
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
        background.dispose();
        shapeRenderer.dispose();
        asteroids.clear();
        bullets.clear();
        explosions.clear();
        enemyBullets.clear();
        enemies.clear();
        Enemy.dispose();
        EnemyBullet.dispose();
        Asteroid.dispose();
        ShipBullet.dispose();
        Explosion.dispose();
        oof.dispose();
    }
}
