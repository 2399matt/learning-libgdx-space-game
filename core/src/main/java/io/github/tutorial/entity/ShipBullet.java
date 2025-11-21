package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

public class ShipBullet implements Entity, Renderable {

    private Sprite sprite;


    public ShipBullet(float x, float y, TextureAtlas atlas) {
        sprite = new Sprite(atlas.findRegion("laser"));
        sprite.setSize(0.3f, 0.3f);
        sprite.setPosition(x, y);
    }

    public ShipBullet(TextureAtlas atlas) {
        sprite = new Sprite(atlas.findRegion("laser"));
        sprite.setSize(0.3f, 0.3f);
    }

    public static void dispose() {

    }

    public void init(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Rectangle getHitBox() {
        return sprite.getBoundingRectangle();
    }

    public void update(float delta) {
        sprite.translateY(5f * delta);
    }

    @Override
    public void render(Batch batch) {
        sprite.draw(batch);
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
