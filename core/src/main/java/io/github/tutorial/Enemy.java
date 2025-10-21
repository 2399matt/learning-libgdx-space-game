package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {

    public static Texture texture = new Texture("enemy.png");

    public Sprite sprite;

    public float bulletCooldown;

    public boolean isLeft;

    public float timesHit;

    public float moveTimer;

    public Enemy() {
        if (texture == null) {
            texture = new Texture("enemy.png");
        }
        this.sprite = new Sprite(texture);
        sprite.setSize(0.5f, 0.5f);
        bulletCooldown = 2f;
        moveTimer = 0f;
        isLeft = true;
    }

    public Enemy(float x, float y) {
        if (texture == null) {
            texture = new Texture("enemy.png");
        }
        this.sprite = new Sprite(texture);
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        moveTimer = 0f;
        bulletCooldown = 2f;
    }

    public static void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
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
}
