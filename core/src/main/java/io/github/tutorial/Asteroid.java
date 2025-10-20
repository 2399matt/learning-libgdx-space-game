package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Asteroid {

    //TODO Add bullets, keep track of times hit in here.
    public Texture texture;

    public Sprite sprite;

    public float timesHit;

    public Asteroid(float x, float y) {
        this.texture = new Texture("asteroid.png");
        sprite = new Sprite(texture);
        timesHit = 0;
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
    }

    public void dispose() {
        this.texture.dispose();
    }
}
