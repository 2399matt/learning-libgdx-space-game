package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Ship implements Entity {

    private static final float SHIP_SPEED = 6f;
    private final Vector2 position;
    private final Vector2 size;
    private int lives;
    private State state;
    private TextureRegion idle;
    private Animation<TextureRegion> movingAnimation;
    private float stateTime;

    public Ship(float x, float y) {
        lives = 5;
        position = new Vector2(x, y);
        size = new Vector2(1f, 1f);
        state = State.IDLE;
        stateTime = 0f;
        initAnimation();
    }

    public Ship() {
        lives = 5;
        position = new Vector2(0f, 0f);
        size = new Vector2(1f, 1f);
        state = State.IDLE;
        stateTime = 0f;
        initAnimation();
    }

    public void initAnimation() {
        Texture texture = new Texture("tinyShip7.png");
        TextureRegion[] aniFrames = new TextureRegion[3 * 5];
        TextureRegion[][] frames = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight() / 3);
        int index = 0;
        for (int i = 0; i < frames.length; i++) {
            for (int j = 0; j < frames[i].length; j++) {
                aniFrames[index++] = frames[i][j];
            }
        }
        movingAnimation = new Animation<>(0.1f, aniFrames);
        idle = aniFrames[0];
        movingAnimation.setPlayMode(Animation.PlayMode.LOOP);
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
        return new Rectangle(position.x, position.y, size.x, size.y);
    }

    public void clampShip(FitViewport viewport) {
        position.x = MathUtils.clamp(position.x, 0, viewport.getWorldWidth() - size.x);
        position.y = MathUtils.clamp(position.y, 0, viewport.getWorldHeight() - size.y);
    }

    public void render(Batch batch, float delta) {
        if (state == State.MOVING) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
        TextureRegion texture = switch (state) {
            case IDLE -> idle;
            case MOVING -> movingAnimation.getKeyFrame(stateTime);
        };
        batch.draw(texture, position.x, position.y, size.x, size.y);
    }

    public void moveUp(float delta) {
        position.y = position.y + SHIP_SPEED * delta;
    }

    public void moveDown(float delta) {
        position.y = position.y - SHIP_SPEED * delta;
    }

    public void moveRight(float delta) {
        position.x = position.x + SHIP_SPEED * delta;
    }

    public void moveLeft(float delta) {
        position.x = position.x - SHIP_SPEED * delta;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getY() {
        return position.y;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {IDLE, MOVING}
}
