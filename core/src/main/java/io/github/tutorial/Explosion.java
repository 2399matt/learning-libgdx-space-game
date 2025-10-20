package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Explosion {

    public Texture texture = new Texture("explosion.png");

    public Sprite sprite;

    public float lifeTimer;

    public boolean finished;

    public Explosion(float x, float y) {
        sprite = new Sprite(texture);
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        finished = false;
    }

    public void update(float delta) {
        lifeTimer += delta;
        if (lifeTimer > 1f) {
            finished = true;
        }
    }

    public void dispose() {
        this.texture.dispose();
    }


}
