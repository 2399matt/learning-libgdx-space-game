package io.github.tutorial.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.Enemy;

public class EnemyPool extends Pool<Enemy> {

    private final TextureAtlas atlas;

    public EnemyPool(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(atlas);
    }
}
