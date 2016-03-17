/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import dk.gruppesex.bodtrd.common.data.Action;
import dk.gruppesex.bodtrd.common.data.ActionHandler;
import dk.gruppesex.bodtrd.common.data.GameData;

/**
 *
 * @author Morten
 */
public class GameInputManager extends InputAdapter
{
    private final GameData gameData;

    public GameInputManager(GameData gameData)
    {
        this.gameData = gameData;
    }

    @Override
    public boolean keyDown(int k)
    {
        switch (k)
        {
            case Keys.UP:
                ActionHandler.setAction(Action.MOVE_UP, true);
            case Keys.DOWN:
                ActionHandler.setAction(Action.MOVE_DOWN, true);
            case Keys.LEFT:
                ActionHandler.setAction(Action.MOVE_LEFT, true);
            case Keys.RIGHT:
                ActionHandler.setAction(Action.MOVE_RIGHT, true);
            case Keys.W:
                ActionHandler.setAction(Action.MOVE_UP, true);
            case Keys.S:
                ActionHandler.setAction(Action.MOVE_DOWN, true);
            case Keys.A:
                ActionHandler.setAction(Action.MOVE_LEFT, true);
            case Keys.D:
                ActionHandler.setAction(Action.MOVE_RIGHT, true);
            case Keys.ESCAPE:
                ActionHandler.setAction(Action.PAUSE_MENU, true);
            case Keys.SPACE:
                ActionHandler.setAction(Action.SHOOT, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int k)
    {
        switch (k)
        {
            case Keys.UP:
                ActionHandler.setAction(Action.MOVE_UP, false);
            case Keys.DOWN:
                ActionHandler.setAction(Action.MOVE_DOWN, false);
            case Keys.LEFT:
                ActionHandler.setAction(Action.MOVE_LEFT, false);
            case Keys.RIGHT:
                ActionHandler.setAction(Action.MOVE_RIGHT, false);
            case Keys.W:
                ActionHandler.setAction(Action.MOVE_UP, false);
            case Keys.S:
                ActionHandler.setAction(Action.MOVE_DOWN, false);
            case Keys.A:
                ActionHandler.setAction(Action.MOVE_LEFT, false);
            case Keys.D:
                ActionHandler.setAction(Action.MOVE_RIGHT, false);
            case Keys.ESCAPE:
                ActionHandler.setAction(Action.PAUSE_MENU, false);
            case Keys.SPACE:
                ActionHandler.setAction(Action.SHOOT, false);
        }
        return true;
    }
}
