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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tutorial.GridManager;
import io.github.tutorial.Main;
import io.github.tutorial.entity.*;

public class EntityManager {

    //TODO Transition sprite management to scene2d, then figure out box2d physics for the single contact listener method
    // can use reflection and remove plenty of the collision for loops we currently have.
    private final float ASTEROID_SPEED = 4f;
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
    private float asteroidTimer = 0f;
    private float enemyRespawnTimer = 10f;
    private boolean debug;

    public EntityManager() {
        this.enemies = new Array<>();
        this.asteroids = new Array<>();
        this.explosions = new Array<>();
        this.shipBullets = new Array<>();
        this.enemyBullets = new Array<>();
        gridManager = new GridManager(false);
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
        gridManager.clear();
        spawnEnemies(delta, viewport);
        ship.clampShip(viewport);
        gridManager.insert(ship);
        for (Enemy e : enemies) {
            e.update(delta);
            gridManager.insert(e);
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
                continue;
            }
            gridManager.insert(enemyBullet);
        }
        for (int i = shipBullets.size - 1; i >= 0; i--) {
            ShipBullet b = shipBullets.get(i);
            b.update(delta);
            if (b.getSprite().getY() > viewport.getWorldHeight() + b.getSprite().getHeight()) {
                shipBullets.removeIndex(i);
                continue;
            }
            gridManager.insert(b);
        }
        for (int i = asteroids.size - 1; i >= 0; i--) {
            Asteroid currAsteroid = asteroids.get(i);
            Sprite asteroid = currAsteroid.getSprite();
            asteroid.translateY(-ASTEROID_SPEED * delta);
            if (asteroid.getY() < -asteroid.getHeight()) {
                asteroids.removeIndex(i);
            } else {
                gridManager.insert(currAsteroid);
            }
        }
        handleCollisions(ship);
        for (ShipBullet b : shipBullets) {
            handleCollisions(b);
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
        font.draw(batch, "CURRENT SCORE: " + Main.playerScore, 15, 9);
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

    public void handleCollisions(Entity target) {
        Array<Entity> nearBy = gridManager.findNearbyEntities(target.getX(), target.getY());
        for (int i = 0; i < nearBy.size; i++) {
            Entity other = nearBy.get(i);
            if (other == target) {
                continue;
            }
            if (target instanceof Ship ship) {
                if (other instanceof Asteroid asteroid) {
                    if (ship.getHitBox().overlaps(asteroid.getHitBox())) {
                        oof.play(.5f);
                        explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                        asteroids.removeValue(asteroid, true);
                        ship.takeDamage();
                        break;
                    }
                } else if (other instanceof EnemyBullet enemyBullet) {
                    if (ship.getHitBox().overlaps(enemyBullet.getHitBox())) {
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
                        shipBullets.removeValue(shipBullet, true);
                        if (asteroid.getTimesHit() >= 3) {
                            asteroids.removeValue(asteroid, true);
                            Main.playerScore += 2;
                        }
                        break;
                    }
                } else if (other instanceof Enemy enemy) {
                    if (shipBullet.getHitBox().overlaps(enemy.getHitBox())) {
                        enemy.takeDamage();
                        shipBullets.removeValue(shipBullet, true);
                        if (enemy.getTimesHit() >= 2) {
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

    public void drawDebug(FitViewport viewport) {
        if (!debug || shapeRenderer == null) return;
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
        debug = false;
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
