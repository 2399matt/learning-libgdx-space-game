package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class Asteroid implements Entity {

    private static final float ASTEROID_SPEED = 4f;

    private Sprite sprite;

    private float timesHit;

    public Asteroid(TextureAtlas atlas) {
        sprite = new Sprite(atlas.findRegion("asteroid2"));
        sprite.setSize(0.5f, 0.5f);
        timesHit = 0;
    }

    public Asteroid(float x, float y, TextureAtlas atlas) {
        sprite = new Sprite(atlas.findRegion("asteroid2"));
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        timesHit = 0;
    }

    public void update(float delta) {
        sprite.translateY(-ASTEROID_SPEED * delta);
    }

    public void init(float x, float y) {
        sprite.setPosition(x, y);
    }

    public void reset() {
        timesHit = 0;
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
