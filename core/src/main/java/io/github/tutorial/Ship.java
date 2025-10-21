package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Ship {

    //TODO Possibly store bullets on the ship?
    public static final float SHIP_SPEED = 7f;
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

    public Rectangle getHitBox() {
        return sprite.getBoundingRectangle();
    }

    public void clampShip(FitViewport viewport) {
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, viewport.getWorldWidth() - sprite.getWidth()));
        sprite.setY(MathUtils.clamp(sprite.getY(), 0, viewport.getWorldHeight() - sprite.getHeight()));
    }

    public void moveUp(float delta) {
        sprite.translateY(SHIP_SPEED * delta);
    }

    public void moveDown(float delta) {
        sprite.translateY(-SHIP_SPEED * delta);
    }

    public void moveRight(float delta) {
        sprite.translateX(SHIP_SPEED * delta);
    }

    public void moveLeft(float delta) {
        sprite.translateX(-SHIP_SPEED * delta);
    }

    public void dispose() {
        this.texture.dispose();
    }
}
