package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class BossBullet implements Entity {

    private final float SPEED = 4f;
    private Sprite sprite;

    public BossBullet(float x, float y, TextureAtlas atlas) {
        sprite = new Sprite(atlas.findRegion("laser"));
        sprite.setPosition(x, y);
        sprite.setSize(0.3f, 0.3f);
    }

    public void update(float delta) {
        sprite.translateY(-SPEED * delta);
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

    public Rectangle getHitBox() {
        return sprite.getBoundingRectangle();
    }
}
