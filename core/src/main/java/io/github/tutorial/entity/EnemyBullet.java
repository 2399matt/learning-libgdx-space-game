package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.tutorial.Asset;

public class EnemyBullet extends Entity implements Renderable {

    private static final TextureRegion TEXTURE = Asset.getBulletTexture();
    private static final float BULLET_SPEED = 3f;
    private static final Vector2 size = new Vector2(0.3f, 0.3f);

    private float targetX, targetY, vx, vy;

    public EnemyBullet() {
        super();
    }

    public EnemyBullet(float x, float y, float targetX, float targetY) {
        super(x,y);
        this.targetX = targetX;
        this.targetY = targetY;
        float dx = targetX - getPosition().x;
        float dy = targetY - getPosition().y;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        vx = (dx / length) * BULLET_SPEED;
        vy = (dy / length) * BULLET_SPEED;
    }

    public void init(float x, float y, float targetX, float targetY) {
        getPosition().set(x,y);
        this.targetX = targetX;
        this.targetY = targetY;
        float dx = targetX - getPosition().x;
        float dy = targetY - getPosition().y;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        vx = (dx / length) * BULLET_SPEED;
        vy = (dy / length) * BULLET_SPEED;
    }

    public void reset() {
        vx = vy = 0f;
        targetX = 0f;
        targetY = 0f;
    }

    @Override
    public void update(float delta) {
        getPosition().set(getPosition().x + vx * delta, getPosition().y + vy * delta);
    }

    @Override
    public void render(Batch batch) {
        batch.draw(TEXTURE, getPosition().x, getPosition().y, size.x, size.y);
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

}
