package io.github.tutorial.pool;

import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.Enemy;

public class EnemyPool extends Pool<Enemy> {

    @Override
    protected Enemy newObject() {
        return new Enemy();
    }
}
