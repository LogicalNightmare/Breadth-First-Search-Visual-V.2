package com.bfs.v2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainScreen implements Screen {
    private OrthographicCamera camera;
    private FitViewport viewport;
    private BFS bfs;
    private UserInterface ui;

    public MainScreen(BFS bfs, UserInterface ui) {
        this.bfs = bfs;
        this.ui = ui;

        camera = new OrthographicCamera();
        viewport = new FitViewport(352, 240, camera);
    }

    @Override
    public void show() {
        viewport.apply(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ui.drawUI(camera, bfs.batch);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        ui.dispose();
    }
}
