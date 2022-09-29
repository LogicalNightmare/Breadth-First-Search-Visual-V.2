package com.bfs.v2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector3;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class UserInterface {
    private final Texture ui;
    private final Texture tiles;
    private final Texture setStartButton;
    private final Texture setEndButton;
    private final Texture setDefaultButton;
    private final Texture setBlockButton;
    private final Texture getPathButton;
    private final Texture resetButton;

    private final TextureRegion defaultTile;
    private final TextureRegion startTile;
    private final TextureRegion endTile;
    private final TextureRegion pathTile;
    private final TextureRegion blockTile;

    public final int[][] grid;
    public final Point gridZeroZero;
    public final int gridTop;
    public final int gridRightSide;

    public final int buttonWidth;
    public final int buttonHeight;
    public final Point startButtonPosition;
    public final Point endButtonPosition;
    public final Point defaultButtonPosition;
    public final Point blockButtonPosition;
    public final Point pathButtonPosition;
    public final Point resetButtonPosition;

    public boolean isStart;
    public boolean isEnd;
    public Point start;
    public Point end;

    public final ArrayList<Point> blockedTiles;

    public final PathFinder pathFinder;

    public UserInterface() {
        ui = new Texture(Gdx.files.internal("interface.png"));
        tiles = new Texture(Gdx.files.internal("tiles.png"));
        setStartButton = new Texture(Gdx.files.internal("set_start_button.png"));
        setEndButton = new Texture(Gdx.files.internal("set_end_button.png"));
        setDefaultButton = new Texture(Gdx.files.internal("set_default_button.png"));
        setBlockButton = new Texture(Gdx.files.internal("set_block_button.png"));
        getPathButton = new Texture(Gdx.files.internal("get_path_button.png"));
        resetButton = new Texture(Gdx.files.internal("reset_button.png"));

        defaultTile = new TextureRegion(tiles, 0, 0, 16, 16);
        startTile = new TextureRegion(tiles, 16, 0, 16, 16);
        endTile = new TextureRegion(tiles, 32, 0, 16, 16);
        pathTile = new TextureRegion(tiles, 48, 0, 16, 16);
        blockTile = new TextureRegion(tiles, 64, 0, 16, 16);

        grid = new int[10][20];
        gridZeroZero = new Point(16, 64);
        gridTop = gridZeroZero.y + (10 * 16);
        gridRightSide = gridZeroZero.x + (20 * 16);

        buttonWidth = 48;
        buttonHeight = 16;
        startButtonPosition = new Point(16, 40);
        endButtonPosition = new Point(16, 16);
        defaultButtonPosition = new Point(80, 16);
        blockButtonPosition = new Point(80, 40);
        pathButtonPosition = new Point(288, 40);
        resetButtonPosition = new Point(288, 16);

        isStart = false;
        isEnd = false;
        start = new Point();
        end = new Point();

        blockedTiles = new ArrayList<>();

        pathFinder = new PathFinder(grid);
    }

    public void drawUI(OrthographicCamera camera, SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(ui, 0, 0);
        batch.draw(setStartButton, startButtonPosition.x, startButtonPosition.y);
        batch.draw(setEndButton, endButtonPosition.x, endButtonPosition.y);
        batch.draw(setDefaultButton, defaultButtonPosition.x, defaultButtonPosition.y);
        batch.draw(setBlockButton, blockButtonPosition.x, blockButtonPosition.y);
        batch.draw(getPathButton, pathButtonPosition.x, pathButtonPosition.y);
        batch.draw(resetButton, resetButtonPosition.x, resetButtonPosition.y);

        for(int y = 0; y < grid.length; y++) {
            for(int x = 0; x < grid[y].length; x++) {
                Point gridPosition = convertGridCoords(new Point(x, y));
                switch(grid[y][x]) {
                    case 1:
                        batch.draw(startTile, gridPosition.x, gridPosition.y);
                        break;
                    case 2:
                        batch.draw(endTile, gridPosition.x, gridPosition.y);
                        break;
                    case 3:
                        batch.draw(pathTile, gridPosition.x, gridPosition.y);
                        break;
                    case 4:
                        batch.draw(blockTile, gridPosition.x, gridPosition.y);
                        break;
                    default:
                        batch.draw(defaultTile, gridPosition.x, gridPosition.y);
                }
            }
        }

        batch.end();
    }

    public void dispose() {
        ui.dispose();
        tiles.dispose();
        setStartButton.dispose();
        setEndButton.dispose();
        setDefaultButton.dispose();
        setBlockButton.dispose();
        getPathButton.dispose();
        resetButton.dispose();
    }


    public Point unconvertGridCoords(Vector3 coords) {
        int x = (((int)coords.x) - gridZeroZero.x) / 16;
        int y = (((int)coords.y) - gridZeroZero.y) / 16;
        return new Point(x, y);
    }

    public int getButton(Vector3 screenCoords) {
        if(screenCoords.y >= endButtonPosition.y && screenCoords.y <= endButtonPosition.y + buttonHeight) {
            if(screenCoords.x >= 16 && screenCoords.x <= buttonWidth + 16) {
                return 2;
            } else if(screenCoords.x >= defaultButtonPosition.x && screenCoords.x <= defaultButtonPosition.x + buttonWidth) {
                return 0;
            } else if(screenCoords.x >= resetButtonPosition.x && screenCoords.x <= resetButtonPosition.x + buttonWidth) {
                return 5;
            }
        } else if(screenCoords.y >= startButtonPosition.y && screenCoords.y <= startButtonPosition.y + buttonHeight) {
            if(screenCoords.x >= 16 && screenCoords.x <= buttonWidth + 16) {
                return 1;
            } else if(screenCoords.x >= blockButtonPosition.x && screenCoords.x <= blockButtonPosition.x + buttonWidth) {
                return 4;
            } else if(screenCoords.x >= pathButtonPosition.x && screenCoords.x <= pathButtonPosition.x + buttonWidth) {
                return 3;
            }
        }
        return -1;
    }

    public void reset() {
        for(int y = 0; y < grid.length; y++) {
            for(int x = 0; x < grid[y].length; x++) {
                grid[y][x] = 0;
            }
        }

        isStart = false;
        isEnd = false;
        start = new Point();
        end = new Point();
        blockedTiles.clear();
    }

    public void getPath() {
        LinkedList<Point> path = pathFinder.getPath(start, end, blockedTiles);

        for(Point node : path) {
            if(!node.equals(end)) {
                grid[node.y][node.x] = 3;
            }
        }
    }

    private Point convertGridCoords(Point coords) {
        int x = (coords.x * 16) + gridZeroZero.x;
        int y = (coords.y * 16) + gridZeroZero.y;
        return new Point(x, y);
    }
}
