package io.github.tutorial.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.ShipBullet;

public class ShipBulletPool extends Pool<ShipBullet> {

    private final TextureAtlas atlas;

    public ShipBulletPool(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    protected ShipBullet newObject() {
        return new ShipBullet(atlas);
    }
}
