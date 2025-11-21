package io.github.tutorial.pool;

import com.badlogic.gdx.utils.Pool;
import io.github.tutorial.entity.Asteroid;

public class AsteroidPool extends Pool<Asteroid> {


    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }
}
