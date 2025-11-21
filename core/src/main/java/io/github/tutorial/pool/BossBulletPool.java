package io.github.tutorial.pool;

import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.BossBullet;

public class BossBulletPool extends Pool<BossBullet> {



    @Override
    protected BossBullet newObject() {
        return new BossBullet();
    }
}
