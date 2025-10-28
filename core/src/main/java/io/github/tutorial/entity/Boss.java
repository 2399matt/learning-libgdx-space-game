package io.github.tutorial.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Boss implements Entity {

    private float health;

    private boolean isLeft;

    private Sprite sprite;

    private float homeX;

    private boolean isVulnerable;

    public Boss(TextureAtlas atlas) {
        setSprite(new Sprite(atlas.findRegion("boss")));
        getSprite().setPosition(7f, 4f);
        setHomeX(getSprite().getX());
        getSprite().setSize(4f, 3f);
        setHealth(100);
        setLeft(false);
        setVulnerable(true);
    }

    public void moveUp(float delta) {
        setVulnerable(false);
        getSprite().translateY(3f * delta);
    }

    public void moveDown(float delta) {
        setVulnerable(true);
        getSprite().translateY(-1f * delta);
    }

    public void clampBoss(FitViewport viewport) {
        getSprite().setX(MathUtils.clamp(getSprite().getX(), 0f, viewport.getWorldWidth() - getSprite().getWidth()));
        getSprite().setY(MathUtils.clamp(getSprite().getY(), 0f, viewport.getWorldHeight() - getSprite().getHeight()));
    }

    public void update(float delta) {
        if (isLeft()) {
            getSprite().translateX(-3f * delta);
            if (getSprite().getX() <= getHomeX() - 3f) {
                getSprite().setX(getHomeX() - 3f);
                setLeft(false);
            }
        } else {
            getSprite().translateX(3f * delta);
            if (getSprite().getX() >= getHomeX() + 3f) {
                getSprite().setX(getHomeX() + 3f);
                setLeft(true);
            }
        }
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

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
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

    @Override
    public float getX() {
        return sprite.getX();
    }

    @Override
    public float getY() {
        return sprite.getY();
    }

    public Rectangle getHitBox() {
        return sprite.getBoundingRectangle();
    }
}
