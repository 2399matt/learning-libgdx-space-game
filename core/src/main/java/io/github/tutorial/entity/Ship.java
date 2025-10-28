package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Ship implements Entity {

    private static final float SHIP_SPEED = 6f;
    private Sprite sprite;
    private int lives;

    public Ship(float x, float y, TextureAtlas atlas) {
        lives = 5;
        sprite = new Sprite(atlas.findRegion("ship"));
        sprite.setPosition(x, y);
        sprite.setSize(1, 1);
    }

    public Ship(TextureAtlas atlas) {
        lives = 5;
        sprite = new Sprite(atlas.findRegion("ship"));
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


    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
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
