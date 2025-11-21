package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tutorial.Asset;

public class Ship extends Entity implements Renderable {

    private static final float SHIP_SPEED = 6f;
    private final Vector2 size;
    private int lives;
    private State state;
    private float stateTime;

    public Ship(float x, float y) {
        super(x,y);
        lives = 5;
        size = new Vector2(1f, 1f);
        state = State.IDLE;
        stateTime = 0f;
    }

    public Ship() {
        super();
        lives = 5;
        size = new Vector2(1f, 1f);
        state = State.IDLE;
        stateTime = 0f;
    }

    public void takeDamage() {
        if (this.lives > 0) {
            this.lives--;
        }
    }

    public boolean isAlive() {
        return this.lives > 0;
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    @Override
    public void update(float delta) {
        if (state == State.MOVING) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    public void clampShip(FitViewport viewport) {
        getPosition().x = MathUtils.clamp(getPosition().x, 0, viewport.getWorldWidth() - size.x);
        getPosition().y = MathUtils.clamp(getPosition().y, 0, viewport.getWorldHeight() - size.y);
    }

    @Override
    public void render(Batch batch) {
        TextureRegion texture = switch (state) {
            case IDLE -> Asset.idleShip;
            case MOVING -> Asset.shipAnimation.getKeyFrame(stateTime);
        };
        batch.draw(texture, getPosition().x, getPosition().y, size.x, size.y);
    }

    public void moveUp(float delta) {
        getPosition().y = getPosition().y + SHIP_SPEED * delta;
    }

    public void moveDown(float delta) {
        getPosition().y = getPosition().y - SHIP_SPEED * delta;
    }

    public void moveRight(float delta) {
        getPosition().x = getPosition().x + SHIP_SPEED * delta;
    }

    public void moveLeft(float delta) {
        getPosition().x = getPosition().x - SHIP_SPEED * delta;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {IDLE, MOVING}
}
