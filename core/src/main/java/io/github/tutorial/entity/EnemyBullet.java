package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class EnemyBullet implements Entity {

    private final float BULLET_SPEED = 3f;

    private Sprite sprite;

    private float targetX, targetY, vx, vy;

    public EnemyBullet(TextureAtlas atlas) {
        sprite = new Sprite(atlas.findRegion("laser"));
        sprite.setSize(0.3f, 0.3f);
    }

    public EnemyBullet(float x, float y, float targetX, float targetY, TextureAtlas atlas) {
        sprite = new Sprite(atlas.findRegion("laser"));
        sprite.setSize(0.3f, 0.3f);
        sprite.setPosition(x, y);
        sprite.setOriginCenter();
        this.targetX = targetX;
        this.targetY = targetY;
        float dx = targetX - sprite.getX();
        float dy = targetY - sprite.getY();
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        vx = (dx / length) * BULLET_SPEED;
        vy = (dy / length) * BULLET_SPEED;
    }

    public void init(float x, float y, float targetX, float targetY) {
        sprite.setPosition(x, y);
        sprite.setOriginCenter();
        this.targetX = targetX;
        this.targetY = targetY;
        float dx = targetX - sprite.getX();
        float dy = targetY - sprite.getY();
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        vx = (dx / length) * BULLET_SPEED;
        vy = (dy / length) * BULLET_SPEED;
    }

    public void reset() {
        vx = vy = 0f;
        targetX = 0f;
        targetY = 0f;
    }

    public Rectangle getHitBox() {
        return sprite.getBoundingRectangle();
    }

    public void update(float delta) {
        sprite.translateY(vy * delta);
        sprite.translateX(vx * delta);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public float getX() {
        return sprite.getX();
    }

    @Override
    public float getY() {
        return sprite.getY();
    }
}
