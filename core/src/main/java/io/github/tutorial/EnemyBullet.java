package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class EnemyBullet {

    public static Texture texture = new Texture("laser.png");

    public Sprite sprite;

    public float startX;

    public float startY;

    public float targetX;

    public float targetY;

    public EnemyBullet() {
        if (texture == null) {
            texture = new Texture("laser.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.2f, 0.2f);
    }

    public EnemyBullet(float x, float y) {
        if (texture == null) {
            texture = new Texture("laser.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.2f, 0.2f);
        sprite.setPosition(x, y);
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
        sprite.translateY(-3f * delta);
    }
}
