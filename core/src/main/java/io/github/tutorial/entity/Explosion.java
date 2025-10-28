package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Explosion {

    private Sprite sprite;

    private float lifeTimer;

    private boolean finished;

    public Explosion(float x, float y, TextureAtlas atlas) {
        sprite = new Sprite(atlas.findRegion("explosion"));
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        finished = false;
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
