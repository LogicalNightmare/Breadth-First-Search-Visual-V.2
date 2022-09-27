package com.bfs.v2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class UserInterface {
    private Texture ui;

    private int[][] grid;

    public UserInterface() {
        ui = new Texture(Gdx.files.internal("interface.png"));

        grid = new int[BFS.SCREEN_HEIGHT][BFS.SCREEN_WIDTH];

    }

    public void drawUI(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(ui, 0, 0);
        batch.end();
    }

    public void dispose() {
        ui.dispose();
    }
}
