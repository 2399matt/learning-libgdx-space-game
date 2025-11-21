package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.tutorial.Asset;

public class Asteroid extends Entity implements Renderable {

    private static final float ASTEROID_SPEED = 4f;
    private static final TextureRegion TEXTURE = Asset.getAsteroidTexture();
    private static final Vector2 size = new Vector2(0.5f, 0.5f);
    private float timesHit;

    public Asteroid() {
        super();
        timesHit = 0;
    }

    public Asteroid(float x, float y) {
        super(x,y);
        timesHit = 0;
    }

    @Override
    public void render(Batch batch) {
        batch.draw(TEXTURE, getPosition().x, getPosition().y, size.x, size.y);
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    @Override
    public void update(float delta) {
        getPosition().y = getPosition().y + -ASTEROID_SPEED * delta;
    }

    public void init(float x, float y) {
        getPosition().set(x,y);
    }

    public void reset() {
        timesHit = 0;
    }

    public float getTimesHit() {
        return timesHit;
    }

    public void setTimesHit(float timesHit) {
        this.timesHit = timesHit;
    }
}
