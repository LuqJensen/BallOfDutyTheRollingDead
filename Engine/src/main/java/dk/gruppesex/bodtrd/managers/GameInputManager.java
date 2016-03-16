/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import dk.gruppesex.bodtrd.common.data.Action;
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
        if (k == Keys.UP)
        {
            gameData.getKeys().setAction(Action.MOVE_UP, true);
        }
        if (k == Keys.LEFT)
        {
            gameData.getKeys().setAction(Action.MOVE_LEFT, true);
        }
        if (k == Keys.DOWN)
        {
            gameData.getKeys().setAction(Action.MOVE_DOWN, true);
        }
        if (k == Keys.RIGHT)
        {
            gameData.getKeys().setAction(Action.MOVE_RIGHT, true);
        }
        if (k == Keys.ESCAPE)
        {
            gameData.getKeys().setAction(Action.PAUSE_MENU, true);
        }
        if (k == Keys.SPACE)
        {
            gameData.getKeys().setAction(Action.SHOOT, true);
        }
        return true;
    }

    public boolean keyUp(int k)
    {
        if (k == Keys.UP)
        {
            gameData.getKeys().setAction(Action.MOVE_UP, false);
        }
        if (k == Keys.LEFT)
        {
            gameData.getKeys().setAction(Action.MOVE_LEFT, false);
        }
        if (k == Keys.DOWN)
        {
            gameData.getKeys().setAction(Action.MOVE_DOWN, false);
        }
        if (k == Keys.RIGHT)
        {
            gameData.getKeys().setAction(Action.MOVE_RIGHT, false);
        }
        if (k == Keys.ESCAPE)
        {
            gameData.getKeys().setAction(Action.PAUSE_MENU, false);
        }
        if (k == Keys.SPACE)
        {
            gameData.getKeys().setAction(Action.SHOOT, false);
        }
        return true;
    }
}
