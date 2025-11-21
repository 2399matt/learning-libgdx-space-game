package io.github.tutorial.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    private Vector2 position;

    public Entity() {
        position = new Vector2(0f, 0f);
    }

    public Entity(float x, float y) {
        position = new Vector2(x, y);
    }

    public abstract Vector2 getSize();
    public abstract void update(float delta);

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return getSize().x;
    }

    public float getHeight() {
        return getSize().y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getHitBox() {
        Vector2 size = getSize();
        return new Rectangle(position.x, position.y, size.x, size.y);
    }
}
