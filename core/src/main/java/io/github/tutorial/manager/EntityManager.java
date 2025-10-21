package io.github.tutorial.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tutorial.Main;
import io.github.tutorial.entity.*;

public class EntityManager {

    private final float ASTEROID_SPEED = 4f;
    private float asteroidTimer = 0f;
    private float enemyRespawnTimer = 10f;
    private Array<Enemy> enemies;
    private Array<EnemyBullet> enemyBullets;
    private Array<Asteroid> asteroids;
    private Array<Explosion> explosions;
    private Array<ShipBullet> shipBullets;
    private Ship ship;
    private Sound oof;
    private Sound laserSound;
    private Music music;
    private ShapeRenderer shapeRenderer;
    private boolean debug;

    public EntityManager() {
        this.enemies = new Array<>();
        this.asteroids = new Array<>();
        this.explosions = new Array<>();
        this.shipBullets = new Array<>();
        this.enemyBullets = new Array<>();
        this.ship = new Ship();
        oof = Gdx.audio.newSound(Gdx.files.internal("oof.mp3"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("laser.mp3"));
        shapeRenderer = new ShapeRenderer();
        debug = false;
        music = Gdx.audio.newMusic(Gdx.files.internal("norm_music.mp3"));
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    public void updateAll(float delta, FitViewport viewport, float globalTimer) {
        spawnEnemies(delta, viewport);
        ship.getSprite().setX(MathUtils.clamp(ship.getSprite().getX(), 0, viewport.getWorldWidth() - ship.getSprite().getWidth()));
        ship.getSprite().setY(MathUtils.clamp(ship.getSprite().getY(), 0, viewport.getWorldHeight() - ship.getSprite().getHeight()));
        handleEnemyBulletCollision(ship.getHitBox());
        for (Enemy e : enemies) {
            e.update(delta);
            if (e.bulletCooldown <= 0f) {
                e.bulletCooldown = 2f;
                enemyBullets.add(new EnemyBullet(e.getSprite().getX(), e.getSprite().getY() - 0.2f, ship.getSprite().getX(), ship.getSprite().getY()));
            }
        }
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet enemyBullet = enemyBullets.get(i);
            enemyBullet.update(delta);
            if (enemyBullet.getSprite().getY() <= -enemyBullet.getSprite().getHeight()) {
                enemyBullets.removeIndex(i);
            }
        }
        for (int i = shipBullets.size - 1; i >= 0; i--) {
            ShipBullet b = shipBullets.get(i);
            b.update(delta);
            if (b.getSprite().getY() > viewport.getWorldHeight() + b.getSprite().getHeight()) {
                shipBullets.removeIndex(i);
            }
        }
        for (int i = asteroids.size - 1; i >= 0; i--) {
            Sprite asteroid = asteroids.get(i).getSprite();
            asteroid.translateY(-ASTEROID_SPEED * delta);
            if (asteroid.getY() < -asteroid.getHeight()) {
                asteroids.removeIndex(i);
            }
            handleShipBulletCollision(asteroids.get(i), asteroid.getBoundingRectangle());
            if (asteroids.get(i).getTimesHit() >= 2) {
                Main.playerScore += 2;
                asteroids.removeIndex(i);
            }
            if (asteroid.getBoundingRectangle().overlaps(ship.getHitBox())) {
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
                asteroids.add(new Asteroid(MathUtils.random(0f, viewport.getWorldWidth() - 1), viewport.getWorldHeight()));
                asteroidTimer = 0;
            }
        } else {
            if (asteroidTimer > 0.8f) {
                asteroids.add(new Asteroid(MathUtils.random(0f, viewport.getWorldWidth() - 1), viewport.getWorldHeight()));
                asteroidTimer = 0;
            }
        }
    }

    public void drawAll(Batch batch, float globalTimer, BitmapFont font, float delta) {
        ship.getSprite().draw(batch);
        font.draw(batch, "CURRENT SCORE: " + Main.playerScore, 8, 9);
        font.draw(batch, "CURRENT LIVES: " + ship.getLives(), 0, 9);
        font.draw(batch, globalTimer + "S", 15, 9);
        for (Asteroid asteroid : asteroids) {
            asteroid.getSprite().draw(batch);
        }
        for (ShipBullet b : shipBullets) {
            b.getSprite().draw(batch);
        }
        for (EnemyBullet b : enemyBullets) {
            b.getSprite().draw(batch);
        }
        for (Enemy e : enemies) {
            e.getSprite().draw(batch);
        }
        for (int i = explosions.size - 1; i >= 0; i--) {
            Explosion e = explosions.get(i);
            e.getSprite().draw(batch);
            e.update(delta);
            if (e.isFinished()) {
                explosions.removeIndex(i);
            }
        }
    }

    public void spawnEnemies(float delta, FitViewport viewport) {
        if (!enemies.isEmpty()) {
            return;
        }
        enemyRespawnTimer += delta;
        if (enemies.isEmpty() && enemyRespawnTimer >= 7f) {
            enemies.add(new Enemy(4f, viewport.getWorldHeight() - 0.5f));
            enemies.add(new Enemy(8f, viewport.getWorldHeight() - 0.5f));
            enemies.add(new Enemy(12f, viewport.getWorldHeight() - 0.5f));
            enemyRespawnTimer = 0f;
        }
    }

    public void createBullet() {
        ShipBullet shipBullet = new ShipBullet(ship.getSprite().getX(), ship.getSprite().getY() + 1);
        shipBullets.add(shipBullet);
        laserSound.play(0.1f);
    }

    public void handleShipBulletCollision(Asteroid asteroid, Rectangle asteroidBox) {
        for (int i = shipBullets.size - 1; i >= 0; i--) {
            ShipBullet b = shipBullets.get(i);
            Sprite bullet = b.getSprite();
            if (handleEnemyHitCollision(b.getHitBox())) {
                shipBullets.removeIndex(i);
                break;
            }
            if (b.getHitBox().overlaps(asteroidBox)) {
                asteroid.setTimesHit(asteroid.getTimesHit()+ 1);
                shipBullets.removeIndex(i);
                break;
            }
        }
    }

    public void handleEnemyBulletCollision(Rectangle shipHitBox) {
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet e = enemyBullets.get(i);
            if (e.getHitBox().overlaps(shipHitBox)) {
                explosions.add(new Explosion(ship.getSprite().getX(), ship.getSprite().getY()));
                enemyBullets.removeIndex(i);
                ship.takeDamage();
            }
        }
    }

    public boolean handleEnemyHitCollision(Rectangle bulletHitBox) {
        if (enemies.isEmpty()) {
            return false;
        }
        for (int i = enemies.size - 1; i >= 0; i--) {
            Sprite enemy = enemies.get(i).getSprite();
            if (enemy.getBoundingRectangle().overlaps(bulletHitBox)) {
                enemies.get(i).takeDamage();
                if (enemies.get(i).getTimesHit() >= 2) {
                    Main.playerScore += 10;
                    enemies.removeIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    public void drawDebug(FitViewport viewport) {
        if (!debug) return;
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(ship.getSprite().getX(), ship.getSprite().getY(), ship.getSprite().getWidth(), ship.getSprite().getHeight());
        shapeRenderer.setColor(Color.GREEN);
        for (Asteroid a : asteroids) {
            Sprite asteroid = a.getSprite();
            shapeRenderer.rect(asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight());
        }
        shapeRenderer.setColor(Color.WHITE);
        for (ShipBullet b : shipBullets) {
            Sprite bullet = b.getSprite();
            shapeRenderer.rect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
        }
        for (EnemyBullet b : enemyBullets) {
            Sprite bullet = b.getSprite();
            shapeRenderer.rect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
        }
        shapeRenderer.end();
    }

    public void flagDebug() {
        debug = !debug;
    }

    public void dispose() {
        ship.dispose();
        asteroids.clear();
        shipBullets.clear();
        explosions.clear();
        enemyBullets.clear();
        enemies.clear();
        Enemy.dispose();
        EnemyBullet.dispose();
        Asteroid.dispose();
        ShipBullet.dispose();
        Explosion.dispose();
        oof.dispose();
        shapeRenderer.dispose();
        laserSound.dispose();
        music.dispose();
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }


    public Array<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }


    public Array<Asteroid> getAsteroids() {
        return asteroids;
    }


    public Array<Explosion> getExplosions() {
        return explosions;
    }


    public Array<ShipBullet> getShipBullets() {
        return shipBullets;
    }


    public Ship getShip() {
        return ship;
    }

}
