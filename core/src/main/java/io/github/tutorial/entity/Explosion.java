package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Explosion {

    public static Texture texture;

    private Sprite sprite;

    private float lifeTimer;

    private boolean finished;

    public Explosion(float x, float y) {
        if (texture == null) {
            texture = new Texture("explosion.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        finished = false;
    }

    public static void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
    }

    public void update(float delta) {
        lifeTimer += delta;
        if (lifeTimer > 0.7f) {
            finished = true;
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public boolean isFinished() {
        return finished;
    }


}
