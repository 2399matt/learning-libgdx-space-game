package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tutorial.Asset;

public class Boss extends Entity implements Renderable {

    private static final Vector2 size = new Vector2(4f, 3f);
    private static final TextureRegion TEXTURE = Asset.getBossTexture();
    private float health;
    private boolean isLeft;
    private float homeX;
    private boolean isVulnerable;

    public Boss() {
        super(7f, 4f);
        homeX = getPosition().x;
        setHealth(100);
        setLeft(false);
        setVulnerable(true);
    }

    public void moveUp(float delta) {
        setVulnerable(false);
        getPosition().y += 3f * delta;
    }

    public void moveDown(float delta) {
        setVulnerable(true);
        getPosition().y -= delta;
    }

    public void clampBoss(FitViewport viewport) {
        getPosition().x = MathUtils.clamp(getPosition().x, 0f, viewport.getWorldWidth() - size.x);
        getPosition().y = MathUtils.clamp(getPosition().y, 0f, viewport.getWorldHeight() - size.y);
    }

    @Override
    public void update(float delta) {
        if (isLeft()) {
            getPosition().x -= 3f * delta;
            if (getPosition().x <= getHomeX() - 3f) {
                getPosition().x = homeX - 3f;
                setLeft(false);
            }
        } else {
            getPosition().x += 3f * delta;
            if (getPosition().x >= getHomeX() + 3f) {
                getPosition().x = getHomeX() + 3f;
                setLeft(true);
            }
        }
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    @Override
    public void render(Batch batch) {
        batch.draw(TEXTURE, getPosition().x, getPosition().y, size.x, size.y);
    }

    public void takeDamage() {
        if (getHealth() > 0 && isVulnerable()) {
            setHealth(getHealth() - 1);
        }
    }

    public boolean isAlive() {
        return getHealth() > 0;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public float getHomeX() {
        return homeX;
    }

    public void setHomeX(float homeX) {
        this.homeX = homeX;
    }

    public boolean isVulnerable() {
        return isVulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        isVulnerable = vulnerable;
    }

}
