package io.github.tutorial.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tutorial.GridManager;
import io.github.tutorial.Main;
import io.github.tutorial.entity.*;

public class BossEntityManager {

    private final Array<ShipBullet> shipBullets;
    private final Array<BossBullet> bossBullets;
    private final Array<Explosion> explosions;
    private final GridManager gridManager;
    private final Sound laserSound;
    private final Music music;
    private final Sound bossHit;
    private phase bossPhase = phase.RISING;
    private float BULLET_WAVE_TIMER = 0f;
    private float BULLET_COOLDOWN = 0f;
    private Ship ship;
    private Boss boss;

    public BossEntityManager(Ship ship) {
        this.ship = ship;
        boss = new Boss();
        shipBullets = new Array<>();
        bossBullets = new Array<>();
        explosions = new Array<>();
        gridManager = new GridManager(true);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("laser.mp3"));
        bossHit = Gdx.audio.newSound(Gdx.files.internal("boss_hit.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("boss_music.mp3"));
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    public void drawAll(Batch batch) {
        //font.draw(batch, "CURRENT SCORE: " + Main.playerScore, 15, 9);
        ship.getSprite().draw(batch);
        boss.getSprite().draw(batch);
        for (ShipBullet b : shipBullets) {
            b.getSprite().draw(batch);
        }
        for (BossBullet b : bossBullets) {
            b.getSprite().draw(batch);
        }
        for (Explosion e : explosions) {
            e.getSprite().draw(batch);
        }
    }

    public void updateAll(float delta, FitViewport viewport) {
        BULLET_WAVE_TIMER += delta;
        gridManager.clear();

        switch (bossPhase) {
            case RISING:
                boss.moveUp(delta);
                if (BULLET_WAVE_TIMER >= 2f) {
                    bossPhase = phase.ATTACKING;
                    BULLET_WAVE_TIMER = 0f;
                }
                break;
            case ATTACKING:
                BULLET_COOLDOWN += delta;
                if (BULLET_COOLDOWN >= 0.1f) {
                    BossBullet b = new BossBullet(MathUtils.random(0f, viewport.getWorldWidth()), boss.getSprite().getY() + 1);
                    bossBullets.add(b);
                    BULLET_COOLDOWN = 0f;
                }
                if (BULLET_WAVE_TIMER >= 3f) {
                    bossPhase = phase.FALLING;
                    BULLET_WAVE_TIMER = 0f;
                }
                break;
            case FALLING:
                boss.moveDown(delta);
                if (BULLET_WAVE_TIMER >= 2f) {
                    bossPhase = phase.CHILL;
                    BULLET_WAVE_TIMER = 0f;
                }
                break;
            case CHILL:
                boss.update(delta);
                if (BULLET_WAVE_TIMER >= 5f) {
                    bossPhase = phase.RISING;
                    BULLET_WAVE_TIMER = 0f;
                }
                break;
        }
        boss.clampBoss(viewport);
        ship.clampShip(viewport);
        gridManager.insert(ship);
        gridManager.insert(boss);
        for (int i = shipBullets.size - 1; i >= 0; i--) {
            ShipBullet b = shipBullets.get(i);
            b.update(delta);
            if (b.getSprite().getY() >= viewport.getWorldHeight() + b.getSprite().getHeight()) {
                shipBullets.removeIndex(i);
                continue;
            }
            gridManager.insert(b);
        }
        for (int i = bossBullets.size - 1; i >= 0; i--) {
            BossBullet b = bossBullets.get(i);
            b.update(delta);
            if (b.getSprite().getY() <= -b.getSprite().getHeight()) {
                bossBullets.removeIndex(i);
                continue;
            }
            gridManager.insert(b);
        }
        handleCollision(ship);
        for (ShipBullet b : shipBullets) {
            handleCollision(b);
        }
        for (int i = explosions.size - 1; i >= 0; i--) {
            Explosion e = explosions.get(i);
            e.update(delta);
            if (e.isFinished()) {
                explosions.removeIndex(i);
            }
        }
    }

    public void handleCollision(Entity target) {
        Array<Entity> nearByEntities = gridManager.findNearbyEntities(target.getX(), target.getY());
        for (int i = 0; i < nearByEntities.size; i++) {
            Entity other = nearByEntities.get(i);
            if (other == target) {
                continue;
            }
            if (target instanceof Ship) {
                if (other instanceof BossBullet bossBullet) {
                    if (ship.getHitBox().overlaps(bossBullet.getHitBox())) {
                        explosions.add(new Explosion(bossBullet.getX(), bossBullet.getY()));
                        bossBullets.removeValue(bossBullet, true);
                        ship.takeDamage();
                        break;
                    }
                }
            } else if (target instanceof ShipBullet shipBullet) {
                if (other instanceof Boss) {
                    if (shipBullet.getHitBox().overlaps(boss.getHitBox())) {
                        bossHit.play(.5f);
                        boss.takeDamage();
                        shipBullets.removeValue(shipBullet, true);
                        break;
                    }
                }
            }
        }
    }

    public void createBullet() {
        ShipBullet shipBullet = new ShipBullet(ship.getSprite().getX(), ship.getSprite().getY() + 1);
        shipBullets.add(shipBullet);
        laserSound.play(0.1f);
    }

    public void dispose() {
        shipBullets.clear();
        bossBullets.clear();
        laserSound.dispose();
        ship.dispose();
        bossHit.dispose();
        music.dispose();
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    private enum phase {
        RISING, ATTACKING, FALLING, CHILL
    }
}
