package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class ShipBullet implements Entity {

    public static Texture texture;

    private Sprite sprite;


    public ShipBullet(float x, float y) {
        if (texture == null) {
            texture = new Texture("laser.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.3f, 0.3f);
        sprite.setPosition(x, y);
    }

    public ShipBullet() {
        if (texture == null) {
            texture = new Texture("laser.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.3f, 0.3f);
    }

    public static void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }

    public Rectangle getHitBox() {
        return sprite.getBoundingRectangle();
    }

    public void update(float delta) {
        sprite.translateY(5f * delta);
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
