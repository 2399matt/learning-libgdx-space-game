package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class Enemy implements Entity {

    public float bulletCooldown;
    private Sprite sprite;
    private boolean isLeft;

    private float timesHit;

    private float moveTimer;

    public Enemy(TextureAtlas atlas) {
        this.sprite = new Sprite(atlas.findRegion("enemy"));
        sprite.setSize(0.5f, 0.5f);
        bulletCooldown = 2f;
        moveTimer = 0f;
        isLeft = true;
    }

    public Enemy(float x, float y, TextureAtlas atlas) {
        this.sprite = new Sprite(atlas.findRegion("enemy"));
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        moveTimer = 0f;
        bulletCooldown = 2f;
    }

    public void init(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void reset() {
        isLeft = true;
        moveTimer = 0f;
        bulletCooldown = 2f;
    }

    public void update(float delta) {
        bulletCooldown -= delta;
        moveTimer += delta;
        if (isLeft) {
            sprite.translateX(2f * delta);
        } else {
            sprite.translateX(-2f * delta);
        }
        if (moveTimer > 1f) {
            isLeft = !isLeft;
            moveTimer = 0f;
        }
    }

    public void takeDamage() {
        timesHit++;
    }

    public Rectangle getHitBox() {
        return sprite.getBoundingRectangle();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public float getTimesHit() {
        return timesHit;
    }

    public void setTimesHit(float timesHit) {
        this.timesHit = timesHit;
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
