package io.github.tutorial;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ShipBullet {

    public static Texture texture;

    public Sprite sprite;

    public ShipBullet(float x, float y) {
        if(texture == null) {
            texture = new Texture("laser.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.2f, 0.2f);
        sprite.setPosition(x, y);
    }

    public ShipBullet() {
        if(texture == null) {
            texture = new Texture("laser.png");
        }
        sprite = new Sprite(texture);
        sprite.setSize(0.2f, 0.2f);
    }

    public static void dispose() {
        if(texture != null) {
            texture.dispose();
            texture = null;
        }
    }
}
