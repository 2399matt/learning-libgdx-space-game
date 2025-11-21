package io.github.tutorial.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import io.github.tutorial.Asset;
import io.github.tutorial.Main;

public class LoadingScreen implements Screen {
    //TODO Work with the Asset to load all needed assets
    // Display simple progress and then proceed to GameScreen

    private final Main game;

    public LoadingScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        Asset.load();
    }

    @Override
    public void render(float delta) {
        if (!Asset.manager.update()) {
            float progress = Asset.manager.getProgress();
            game.batch.begin();
            game.font.draw(game.batch,  progress * 100 + "%", Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
            game.batch.end();
        } else {
            Asset.finishLoading();
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
