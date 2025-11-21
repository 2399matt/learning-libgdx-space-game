package io.github.tutorial.pool;

import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.EnemyBullet;

public class EnemyBulletPool extends Pool<EnemyBullet> {

    @Override
    protected EnemyBullet newObject() {
        return new EnemyBullet();
    }
}
