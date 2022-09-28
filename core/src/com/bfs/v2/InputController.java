package com.bfs.v2;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import java.awt.*;

public class InputController implements InputProcessor {
    private final OrthographicCamera camera;
    private final UserInterface ui;

    private int operationCode;



    public InputController(OrthographicCamera camera, UserInterface ui) {
        this.camera = camera;
        this.ui = ui;

        operationCode = 0;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            Vector3 screenCoords = camera.unproject(new Vector3(screenX, screenY, 0));

            if(screenCoords.x >= ui.gridZeroZero.x && screenCoords.x <= ui.gridRightSide &&
                    screenCoords.y >= ui.gridZeroZero.y && screenCoords.y <= ui.gridTop) {

                Point coords = ui.unconvertGridCoords(screenCoords);
                switch (operationCode) {
                    case 0:
                        ui.grid[coords.y][coords.x] = 0;

                        Point p = new Point(coords.x, coords.y);

                        if(ui.blockedTiles.contains(p)) {
                            ui.blockedTiles.remove(p);
                        } else if(ui.start.equals(p)) {
                            ui.isStart = false;
                            ui.start = new Point();
                        } else if(ui.end.equals(p)) {
                            ui.isEnd = false;
                            ui.end = new Point();
                        }

                        break;
                    case 1:
                        if(!ui.isStart) {
                            ui.grid[coords.y][coords.x] = 1;
                            ui.isStart = true;
                            ui.start.x = coords.x;
                            ui.start.y = coords.y;
                        }
                        break;
                    case 2:
                        if(!ui.isEnd) {
                            ui.grid[coords.y][coords.x] = 2;
                            ui.isEnd = true;
                            ui.end.x = coords.x;
                            ui.end.y = coords.y;
                        }
                        break;
                    case 3:
                        break;
                    case 4:
                        ui.grid[coords.y][coords.x] = 4;
                        ui.blockedTiles.add(new Point(coords.x, coords.y));
                        break;
                }

            } else {
                switch (ui.getButton(screenCoords)) {
                    case 0:
                        operationCode = 0;
                        break;
                    case 1:
                        operationCode = 1;
                        break;
                    case 2:
                        operationCode = 2;
                        break;
                    case 3:
                        operationCode = 3;
                        //call findpath method
                        break;
                    case 4:
                        operationCode = 4;
                        break;
                    case 5:
                        operationCode = 0;
                        ui.reset();
                        break;
                    default:
                        break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
