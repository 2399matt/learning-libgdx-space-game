package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class BossBullet {

    public static Texture texture = new Texture("laser.png");
    private final float SPEED = 4f;
    private Sprite sprite;

    public BossBullet(float x, float y) {
        if (texture == null) {
            texture = new Texture("laser.png");
        }
        sprite = new Sprite(texture);
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
}
