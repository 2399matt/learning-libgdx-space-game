package io.github.tutorial;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Asset {

    public static AssetManager manager = new AssetManager();

    public static TextureAtlas atlas;

    public static Animation<TextureRegion> shipAnimation;

    public static Animation<TextureRegion> explodeAnimation;

    public static TextureRegion idleShip;

    public static void load() {
        manager.load("atlas/sprite.atlas", TextureAtlas.class);
        manager.load("tinyShip7.png", Texture.class);
        manager.load("Explosion.png", Texture.class);
        manager.load("oof.mp3", Sound.class);
        manager.load("boss_hit.mp3", Sound.class);
        manager.load("norm_music.mp3", Music.class);
        manager.load("boss_music.mp3", Music.class);
        manager.load("laser.mp3", Sound.class);
    }

    public static void finishLoading() {
        manager.finishLoading();
        atlas = manager.get("atlas/sprite.atlas", TextureAtlas.class);
        loadAnimation();
    }

    public static void loadAnimation() {
        Texture texture = manager.get("tinyShip7.png", Texture.class);
        TextureRegion[] aniFrames = new TextureRegion[3 * 5];
        TextureRegion[][] frames = TextureRegion.split(texture, texture.getWidth() / 5, texture.getHeight() / 3);
        int index = 0;
        for (int i = 0; i < frames.length; i++) {
            for (int j = 0; j < frames[i].length; j++) {
                aniFrames[index++] = frames[i][j];
            }
        }
        shipAnimation = new Animation<>(0.1f, aniFrames);
        idleShip = aniFrames[0];
        shipAnimation.setPlayMode(Animation.PlayMode.LOOP);

        //This refactor is going to be annoying.
        texture = manager.get("Explosion.png", Texture.class);
        TextureRegion[] explosionFrames = new TextureRegion[12];
        int frameWidth = texture.getWidth() / 12;
        int frameHeight = texture.getHeight();
        for (int i = 0; i < explosionFrames.length; i++) {
            explosionFrames[i] = new TextureRegion(texture,
                i * frameWidth, 0, frameWidth, frameHeight);
        }
        explodeAnimation = new Animation<>(0.1f, explosionFrames);
        explodeAnimation.setPlayMode(Animation.PlayMode.LOOP);
        texture = null;
    }

    public static TextureRegion getBulletTexture() {
        return atlas.findRegion("laser");
    }

    public static TextureRegion getHeartTexture() {
        return atlas.findRegion("heart");
    }

    public static TextureRegion getAsteroidTexture() {
        return atlas.findRegion("asteroid2");
    }

    public static TextureRegion getEnemyTexture() {
        return atlas.findRegion("enemy");
    }

    public static TextureRegion getBossTexture() {
        return atlas.findRegion("boss");
    }

    public static Sound getLaserSound() {
        return manager.get("laser.mp3", Sound.class);
    }

    public static Sound getBossHitSound() {
        return manager.get("boss_hit.mp3", Sound.class);
    }

    public static Sound getPlayerHitSound() {
        return manager.get("oof.mp3", Sound.class);
    }

    public static Music getBossMusic() {
        return manager.get("boss_music.mp3", Music.class);
    }

    public static Music getNormMusic() {
        return manager.get("norm_music.mp3", Music.class);
    }

    public static TextureRegion getBackgroundTexture() {
        return atlas.findRegion("background2");
    }

    public static void dispose() {
        atlas.dispose();
        manager.dispose();
    }


}
