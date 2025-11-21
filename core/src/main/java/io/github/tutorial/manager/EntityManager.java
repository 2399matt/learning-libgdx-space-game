package io.github.tutorial.manager;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tutorial.Asset;
import io.github.tutorial.Main;
import io.github.tutorial.entity.*;
import io.github.tutorial.pool.AsteroidPool;
import io.github.tutorial.pool.EnemyBulletPool;
import io.github.tutorial.pool.EnemyPool;
import io.github.tutorial.pool.ShipBulletPool;

public class EntityManager {

    private final Array<Enemy> enemies;
    private final Array<EnemyBullet> enemyBullets;
    private final Array<Asteroid> asteroids;
    private final Array<Explosion> explosions;
    private final Array<ShipBullet> shipBullets;
    private final Ship ship;
    private final Sound oof;
    private final Sound laserSound;
    private final Music music;
    private final ShapeRenderer shapeRenderer;
    private final GridManager gridManager;
    private final AsteroidPool asteroidPool;
    private final EnemyBulletPool enemyBulletPool;
    private final EnemyPool enemyPool;
    private final ShipBulletPool shipBulletPool;
    private float asteroidTimer = 0f;
    private float enemyRespawnTimer = 10f;
    private boolean debug;

    public EntityManager() {
        this.enemies = new Array<>();
        this.asteroids = new Array<>();
        this.explosions = new Array<>();
        this.shipBullets = new Array<>();
        this.enemyBullets = new Array<>();
        asteroidPool = new AsteroidPool();
        enemyBulletPool = new EnemyBulletPool();
        shipBulletPool = new ShipBulletPool();
        enemyPool = new EnemyPool();
        gridManager = new GridManager(false);
        this.ship = new Ship();
        oof = Asset.getPlayerHitSound();
        laserSound = Asset.getLaserSound();
        shapeRenderer = new ShapeRenderer();
        debug = false;
        music = Asset.getNormMusic();
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    public void updateAll(float delta, FitViewport viewport, float globalTimer) {
        gridManager.clear();
        ship.update(delta);
        spawnEnemies(delta, viewport);
        ship.clampShip(viewport);
        gridManager.insert(ship);
        for (Enemy e : enemies) {
            e.update(delta);
            gridManager.insert(e);
            if (e.bulletCooldown <= 0f) {
                e.bulletCooldown = 2f;
                EnemyBullet eBullet = enemyBulletPool.obtain();
                eBullet.reset();
                eBullet.init(e.getX(), e.getY() - 0.2f, ship.getX(), ship.getY());
                enemyBullets.add(eBullet);
            }
        }
        for (int i = enemyBullets.size - 1; i >= 0; i--) {
            EnemyBullet enemyBullet = enemyBullets.get(i);
            enemyBullet.update(delta);
            if (enemyBullet.getY() <= -enemyBullet.getHeight()) {
                enemyBulletPool.free(enemyBullet);
                enemyBullets.removeIndex(i);
                continue;
            }
            gridManager.insert(enemyBullet);
        }
        for (int i = shipBullets.size - 1; i >= 0; i--) {
            ShipBullet b = shipBullets.get(i);
            b.update(delta);
            if (b.getY() > viewport.getWorldHeight() + b.getHeight()) {
                shipBulletPool.free(b);
                shipBullets.removeIndex(i);
                continue;
            }
            gridManager.insert(b);
        }
        for (int i = asteroids.size - 1; i >= 0; i--) {
            Asteroid asteroid = asteroids.get(i);
            asteroid.update(delta);
            if (asteroid.getY() < -asteroid.getHeight()) {
                asteroidPool.free(asteroid);
                asteroids.removeIndex(i);
            } else {
                gridManager.insert(asteroid);
            }
        }
        handleCollisions(ship);
        for (ShipBullet b : shipBullets) {
            handleCollisions(b);
        }
        asteroidTimer += delta;
        if (globalTimer > 30f) {
            if (asteroidTimer > 0.2f) {
                Asteroid asteroid = asteroidPool.obtain();
                asteroid.reset();
                asteroid.init(MathUtils.random(0f, viewport.getWorldWidth() - 1), viewport.getWorldHeight());
                asteroids.add(asteroid);
                asteroidTimer = 0f;
            }
        } else {
            if (asteroidTimer > 0.8f) {
                Asteroid asteroid = asteroidPool.obtain();
                asteroid.reset();
                asteroid.init(MathUtils.random(0f, viewport.getWorldWidth() - 1), viewport.getWorldHeight());
                asteroids.add(asteroid);
                asteroidTimer = 0f;
            }
        }
    }

    public void drawAll(Batch batch, float globalTimer, BitmapFont font, float delta) {
        ship.render(batch);
        font.draw(batch, "CURRENT SCORE: " + Main.playerScore, 15, 9);
        for (Asteroid asteroid : asteroids) {
            asteroid.render(batch);
        }
        for (ShipBullet b : shipBullets) {
            b.render(batch);
        }
        for (EnemyBullet b : enemyBullets) {
            b.render(batch);
        }
        for (Enemy e : enemies) {
            e.render(batch);
        }
        for (int i = explosions.size - 1; i >= 0; i--) {
            Explosion e = explosions.get(i);
            e.update(delta);
            e.render(batch, delta);
            if (e.isFinished()) {
                explosions.removeIndex(i);
            }
        }
    }

    public void handleCollisions(Entity target) {
        Array<Entity> nearBy = gridManager.findNearbyEntities(target.getX(), target.getY());
        for (int i = 0; i < nearBy.size; i++) {
            Entity other = nearBy.get(i);
            if (other == target) {
                continue;
            }
            if (target instanceof Ship) {
                if (other instanceof Asteroid asteroid) {
                    if (ship.getHitBox().overlaps(asteroid.getHitBox())) {
                        oof.play(.5f);
                        explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                        asteroidPool.free(asteroid);
                        asteroids.removeValue(asteroid, true);
                        ship.takeDamage();
                        break;
                    }
                } else if (other instanceof EnemyBullet enemyBullet) {
                    if (ship.getHitBox().overlaps(enemyBullet.getHitBox())) {
                        enemyBulletPool.free(enemyBullet);
                        enemyBullets.removeValue(enemyBullet, true);
                        explosions.add(new Explosion(enemyBullet.getX(), enemyBullet.getY()));
                        ship.takeDamage();
                        break;
                    }
                }
            } else if (target instanceof ShipBullet shipBullet) {
                if (other instanceof Asteroid asteroid) {
                    if (shipBullet.getHitBox().overlaps(asteroid.getHitBox())) {
                        asteroid.setTimesHit(asteroid.getTimesHit() + 1);
                        shipBulletPool.free(shipBullet);
                        shipBullets.removeValue(shipBullet, true);
                        if (asteroid.getTimesHit() >= 3) {
                            asteroidPool.free(asteroid);
                            asteroids.removeValue(asteroid, true);
                            Main.playerScore += 2;
                        }
                        break;
                    }
                } else if (other instanceof Enemy enemy) {
                    if (shipBullet.getHitBox().overlaps(enemy.getHitBox())) {
                        enemy.takeDamage();
                        shipBulletPool.free(shipBullet);
                        shipBullets.removeValue(shipBullet, true);
                        if (enemy.getTimesHit() >= 2) {
                            enemyPool.free(enemy);
                            enemies.removeValue(enemy, true);
                            Main.playerScore += 10;
                        }
                        break;
                    }
                }
            }
        }
    }

    public void spawnEnemies(float delta, FitViewport viewport) {
        if (!enemies.isEmpty()) {
            return;
        }
        enemyRespawnTimer += delta;
        if (enemies.isEmpty() && enemyRespawnTimer >= 7f) {
            // 16 wide vp, spawn inc 4.
            for (int i = 4; i < 13; i += 4) {
                Enemy enemy = enemyPool.obtain();
                enemy.reset();
                enemy.init((float) i, viewport.getWorldHeight() - 0.5f);
                enemies.add(enemy);
            }
            enemyRespawnTimer = 0f;
        }
    }

    public void createBullet() {
        ShipBullet shipBullet = shipBulletPool.obtain();
        shipBullet.init(ship.getX(), ship.getY() + 1f);
        shipBullets.add(shipBullet);
        laserSound.play(0.1f);
    }

    public void drawDebug(FitViewport viewport) {
        if (!debug || shapeRenderer == null) return;
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(ship.getX(), ship.getY(), 1f, 1f);
        shapeRenderer.setColor(Color.GREEN);
        for (Asteroid a : asteroids) {
            shapeRenderer.rect(a.getX(), a.getY(), a.getWidth(), a.getHeight());
        }
        shapeRenderer.setColor(Color.WHITE);
        for (ShipBullet b : shipBullets) {
            shapeRenderer.rect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }
        for (EnemyBullet b : enemyBullets) {
            shapeRenderer.rect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }
        shapeRenderer.end();
    }

    public void flagDebug() {
        debug = !debug;
    }

    public void dispose() {
        debug = false;
        asteroids.clear();
        shipBullets.clear();
        explosions.clear();
        enemyBullets.clear();
        enemies.clear();
        shipBulletPool.clear();
        asteroidPool.clear();
        enemyBulletPool.clear();
        enemyPool.clear();
        music.dispose();
        shapeRenderer.dispose();
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
