package io.github.tutorial.pool;

import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.ShipBullet;

public class ShipBulletPool extends Pool<ShipBullet> {

    @Override
    protected ShipBullet newObject() {
        return new ShipBullet();
    }
}
