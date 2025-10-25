package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Asteroid implements Entity {

    //TODO Add bullets, keep track of times hit in here.
    public static Texture texture;

    private Sprite sprite;

    private float timesHit;

    public Asteroid(float x, float y) {
        if (texture == null) {
            texture = new Texture("asteroid2.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        timesHit = 0;
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
