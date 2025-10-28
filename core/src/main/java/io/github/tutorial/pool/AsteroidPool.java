package io.github.tutorial.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.Asteroid;

public class AsteroidPool extends Pool<Asteroid> {

    private final TextureAtlas atlas;

    public AsteroidPool(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid(atlas);
    }
}
