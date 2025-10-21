package io.github.tutorial;

import com.badlogic.gdx.Gdx;
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

public class EntityManager {

    public final float ASTEROID_SPEED = 4f;
    public float asteroidTimer = 0f;
    public float enemyRespawnTimer = 10f;
    public Array<Enemy> enemies;
    public Array<EnemyBullet> enemyBullets;
    public Array<Asteroid> asteroids;
    public Array<Explosion> explosions;
    public Array<ShipBullet> shipBullets;
    public Ship ship;
    public Sound oof;
    public Sound laserSound;
    public ShapeRenderer shapeRenderer;
    public boolean debug;

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
    }

    public void updateAll(float delta, FitViewport viewport, float globalTimer) {
        spawnEnemies(delta, viewport);
        ship.sprite.setX(MathUtils.clamp(ship.sprite.getX(), 0, viewport.getWorldWidth() - ship.sprite.getWidth()));
        ship.sprite.setY(MathUtils.clamp(ship.sprite.getY(), 0, viewport.getWorldHeight() - ship.sprite.getHeight()));
        handleEnemyBulletCollision(ship.getHitBox());
        for (Enemy e : enemies) {
            e.update(delta);
            if (e.bulletCooldown <= 0f) {
                e.bulletCooldown = 2f;
                enemyBullets.add(new EnemyBullet(e.sprite.getX(), e.sprite.getY() - 0.2f));
            }
        }
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet enemyBullet = enemyBullets.get(i);
            enemyBullet.update(delta);
            if (enemyBullet.sprite.getY() <= -enemyBullet.sprite.getHeight()) {
                enemyBullets.removeIndex(i);
            }
        }
        for (int i = shipBullets.size - 1; i >= 0; i--) {
            ShipBullet b = shipBullets.get(i);
            b.update(delta);
            if (b.sprite.getY() > viewport.getWorldHeight() + b.sprite.getHeight()) {
                shipBullets.removeIndex(i);
            }
        }
        for (int i = asteroids.size - 1; i >= 0; i--) {
            Sprite asteroid = asteroids.get(i).sprite;
            asteroid.translateY(-ASTEROID_SPEED * delta);
            if (asteroid.getY() < -asteroid.getHeight()) {
                asteroids.removeIndex(i);
            }
            handleShipBulletCollision(asteroids.get(i), asteroid.getBoundingRectangle());
            if (asteroids.get(i).timesHit >= 2) {
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
        ship.sprite.draw(batch);
        font.draw(batch, "Current lives: " + ship.lives, 8, 8);
        font.draw(batch, globalTimer + "s", 8, 9);
        for (Asteroid asteroid : asteroids) {
            asteroid.sprite.draw(batch);
        }
        for (ShipBullet b : shipBullets) {
            b.sprite.draw(batch);
        }
        for (EnemyBullet b : enemyBullets) {
            b.sprite.draw(batch);
        }
        for (Enemy e : enemies) {
            e.sprite.draw(batch);
        }
        for (int i = explosions.size - 1; i >= 0; i--) {
            Explosion e = explosions.get(i);
            e.sprite.draw(batch);
            e.update(delta);
            if (e.finished) {
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
        ShipBullet shipBullet = new ShipBullet(ship.sprite.getX(), ship.sprite.getY() + 1);
        shipBullets.add(shipBullet);
        laserSound.play(0.3f);
    }

    public void handleShipBulletCollision(Asteroid asteroid, Rectangle asteroidBox) {
        for (int i = shipBullets.size - 1; i >= 0; i--) {
            ShipBullet b = shipBullets.get(i);
            Sprite bullet = b.sprite;
            if (handleEnemyHitCollision(b.getHitBox())) {
                shipBullets.removeIndex(i);
                break;
            }
            if (b.getHitBox().overlaps(asteroidBox)) {
                asteroid.timesHit++;
                shipBullets.removeIndex(i);
                break;
            }
        }
    }

    public void handleEnemyBulletCollision(Rectangle shipHitBox) {
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet e = enemyBullets.get(i);
            if (e.getHitBox().overlaps(shipHitBox)) {
                explosions.add(new Explosion(ship.sprite.getX(), ship.sprite.getY()));
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
            Sprite enemy = enemies.get(i).sprite;
            if (enemy.getBoundingRectangle().overlaps(bulletHitBox)) {
                enemies.get(i).takeDamage();
                if (enemies.get(i).timesHit >= 2) {
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
        shapeRenderer.rect(ship.sprite.getX(), ship.sprite.getY(), ship.sprite.getWidth(), ship.sprite.getHeight());
        shapeRenderer.setColor(Color.GREEN);
        for (Asteroid a : asteroids) {
            Sprite asteroid = a.sprite;
            shapeRenderer.rect(asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getHeight());
        }
        shapeRenderer.setColor(Color.WHITE);
        for(ShipBullet b : shipBullets) {
            Sprite bullet = b.sprite;
            shapeRenderer.rect(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
        }
        for(EnemyBullet b : enemyBullets) {
            Sprite bullet = b.sprite;
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
