package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Ship {

    //TODO Possibly store bullets on the ship?
    public Texture texture;
    public Sprite sprite;
    public int lives;

    public Ship(float x, float y) {
        lives = 3;
        texture = new Texture("ship.jpg");
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(1, 1);
    }

    public Ship() {
        lives = 3;
        texture = new Texture("ship.png");
        sprite = new Sprite(texture);
        sprite.setSize(1, 1);
    }

    public void takeDamage() {
        if (this.lives > 0) {
            this.lives--;
        }
    }

    public boolean isAlive() {
        return this.lives > 0;
    }

    public void dispose() {
        this.texture.dispose();
    }
}
