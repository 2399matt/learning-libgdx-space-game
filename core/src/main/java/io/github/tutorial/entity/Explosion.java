package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Explosion {

    //private Sprite sprite;

    private static Animation<TextureRegion> explodeAnimation;
    private static boolean isLoaded = false;
    private float lifeTimer;
    private boolean finished;
    private final Vector2 pos;
    private float stateTime;

    public Explosion(float x, float y, TextureAtlas atlas) {
        initAnimation();
        pos = new Vector2(x, y);
        stateTime = 0f;
        finished = false;
    }

    private static void initAnimation() {
        if (!isLoaded) {
            Texture texture = new Texture("Explosion.png");
            TextureRegion[] aniFrames = new TextureRegion[12];
            int frameWidth = texture.getWidth() / 12;
            int frameHeight = texture.getHeight();
            for (int i = 0; i < aniFrames.length; i++) {
                aniFrames[i] = new TextureRegion(texture,
                    i * frameWidth, 0, frameWidth, frameHeight);
            }
            explodeAnimation = new Animation<>(0.1f, aniFrames);
            explodeAnimation.setPlayMode(Animation.PlayMode.LOOP);
            isLoaded = true;
            texture = null;
        }
    }

    public void update(float delta) {
        stateTime += delta;
        if (explodeAnimation.isAnimationFinished(stateTime)) {
            finished = true;
        }
    }

    public void render(Batch batch, float delta) {
        if (finished) return;
        batch.draw(explodeAnimation.getKeyFrame(stateTime), pos.x, pos.y, 1f, 1f);
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
