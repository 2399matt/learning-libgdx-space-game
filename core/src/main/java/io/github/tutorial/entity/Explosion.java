package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import io.github.tutorial.Asset;

public class Explosion {

    private final Vector2 pos;
    private boolean finished;
    private float stateTime;

    public Explosion(float x, float y) {
        pos = new Vector2(x, y);
        stateTime = 0f;
        finished = false;
    }

    public void update(float delta) {
        stateTime += delta;
        if (Asset.explodeAnimation.isAnimationFinished(stateTime)) {
            finished = true;
        }
    }

    public void render(Batch batch, float delta) {
        if (finished) return;
        batch.draw(Asset.explodeAnimation.getKeyFrame(stateTime), pos.x, pos.y, 1f, 1f);
    }

    public boolean isFinished() {
        return finished;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }
}
