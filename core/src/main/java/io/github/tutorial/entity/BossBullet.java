package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.tutorial.Asset;

public class BossBullet extends Entity implements Renderable {

    private static final TextureRegion TEXTURE = Asset.getBulletTexture();
    private static final float SPEED = 4f;
    private static final Vector2 size = new Vector2(0.3f, 0.3f);

    public BossBullet() {
        super();
    }

    public BossBullet(float x, float y) {
        super(x,y);
    }

    public void reset() {
        getPosition().set(0f, 0f);
    }

    public void init(float x, float y) {
        getPosition().set(x,y);
    }

    public void update(float delta) {
        getPosition().y += -SPEED * delta;
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
