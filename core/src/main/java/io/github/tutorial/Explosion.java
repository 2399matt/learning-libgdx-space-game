package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Explosion {

    public static Texture texture = new Texture("explosion.png");

    public Sprite sprite;

    public boolean finished;

    public Explosion(float x, float y) {
        sprite = new Sprite(texture);
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
        finished = false;
    }


}
