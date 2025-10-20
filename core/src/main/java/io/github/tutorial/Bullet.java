package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Bullet {

    public Texture texture;

    public Sprite sprite;

    public Bullet(float x, float y) {
        texture = new Texture("laser.png");
        sprite = new Sprite(texture);
        sprite.setSize(0.1f, 0.1f);
        sprite.setPosition(x, y);
    }

    public Bullet() {
        texture = new Texture("laser.png");
        sprite = new Sprite(texture);
        sprite.setSize(0.2f, 0.2f);
    }
}
