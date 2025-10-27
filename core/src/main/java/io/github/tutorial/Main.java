package io.github.tutorial;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    @Override
    public void create() {
        viewport = new FitViewport(16, 9);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("white.fnt"), false);
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
