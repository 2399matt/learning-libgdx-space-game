package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Asteroid {

    //TODO Add bullets, keep track of times hit in here.
    public static Texture texture;

    public Sprite sprite;

    public float timesHit;

    public Asteroid(float x, float y) {
        if(texture == null) {
            texture = new Texture("asteroid2.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        timesHit = 0;
    }

    public static void dispose() {
        if(texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
