package io.github.tutorial.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.EnemyBullet;

public class EnemyBulletPool extends Pool<EnemyBullet> {

    private final TextureAtlas atlas;

    public EnemyBulletPool(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    protected EnemyBullet newObject() {
        return new EnemyBullet(atlas);
    }
}
