package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.tutorial.Asset;

public class Enemy extends Entity implements Renderable {

    private static final TextureRegion TEXTURE = Asset.getEnemyTexture();
    private static final Vector2 size = new Vector2(0.5f, 0.5f);
    public float bulletCooldown;
    private boolean isLeft;
    private float timesHit;
    private float moveTimer;

    public Enemy() {
        super();
        bulletCooldown = 2f;
        moveTimer = 0f;
        isLeft = true;
    }

    public Enemy(float x, float y) {
        super(x,y);
        moveTimer = 0f;
        bulletCooldown = 2f;
    }

    public void init(float x, float y) {
        //sprite.setPosition(x, y);
        getPosition().set(x,y);
    }

    public void reset() {
        isLeft = true;
        moveTimer = 0f;
        bulletCooldown = 2f;
    }

    public void update(float delta) {
        bulletCooldown -= delta;
        moveTimer += delta;
        if (isLeft) {
            getPosition().x += 2f * delta;
        } else {
            getPosition().x -= 2f * delta;
        }
        if (moveTimer > 1f) {
            isLeft = !isLeft;
            moveTimer = 0f;
        }
    }

    @Override
    public void render(Batch batch) {
        batch.draw(TEXTURE, getPosition().x, getPosition().y, size.x, size.y);
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    public void takeDamage() {
        timesHit++;
    }

    public float getTimesHit() {
        return timesHit;
    }

    public void setTimesHit(float timesHit) {
        this.timesHit = timesHit;
    }

}
