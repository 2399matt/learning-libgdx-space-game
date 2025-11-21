package io.github.tutorial;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.tutorial.screen.GameScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {

    public static int playerScore = 0;
    public FitViewport viewport;
    public SpriteBatch batch;
    public BitmapFont font;
    public TextureAtlas textureAtlas;

    // TODO Create a loading screen class and an static Asset class
    // TODO Loading screen will call load() to load all static assets, then finish loading (which will include an init method for hte animations)
    // TODO THEN go to game.
    @Override
    public void create() {
        viewport = new FitViewport(16, 9);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("white.fnt"), false);
        //TODO: Change atlas to point to new one with animation
        textureAtlas = new TextureAtlas("atlas/sprite.atlas");
        setScreen(new GameScreen(this, textureAtlas));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        font.dispose();
        batch.dispose();
        super.dispose();
    }
}
