/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.managers;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import dk.gruppeseks.bodtrd.common.data.Action;
import dk.gruppeseks.bodtrd.common.data.ActionHandler;

/**
 *
 * @author Morten
 */
public class GameInputManager extends InputAdapter
{

    @Override
    public boolean touchDown(int x, int y, int pointer, int button)
    {
        switch (button)
        {
            case Buttons.LEFT:
            {
                ActionHandler.setActive(Action.SHOOT, true);
                break;
            }
            default:
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button)
    {
        switch (button)
        {
            case Buttons.LEFT:
            {
                ActionHandler.setActive(Action.SHOOT, true);
                break;
            }
            default:
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean keyDown(int k)
    {
        switch (k)
        {
            case Keys.UP:
            case Keys.W:
            {
                ActionHandler.setActive(Action.MOVE_UP, true);
                break;
            }
            case Keys.LEFT:
            case Keys.A:
            {
                ActionHandler.setActive(Action.MOVE_LEFT, true);
                break;
            }
            case Keys.DOWN:
            case Keys.S:
            {
                ActionHandler.setActive(Action.MOVE_DOWN, true);
                break;
            }
            case Keys.RIGHT:
            case Keys.D:
            {
                ActionHandler.setActive(Action.MOVE_RIGHT, true);
                break;
            }
            case Keys.ESCAPE:
            {
                ActionHandler.setActive(Action.PAUSE_MENU, true);
                break;
            }
            case Keys.SPACE:
            {
                ActionHandler.setActive(Action.SHOOT, true);
                break;
            }
            default:
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int k)
    {
        switch (k)
        {
            case Keys.UP:
            case Keys.W:
            {
                ActionHandler.setActive(Action.MOVE_UP, false);
                break;
            }
            case Keys.LEFT:
            case Keys.A:
            {
                ActionHandler.setActive(Action.MOVE_LEFT, false);
                break;
            }
            case Keys.DOWN:
            case Keys.S:
            {
                ActionHandler.setActive(Action.MOVE_DOWN, false);
                break;
            }
            case Keys.RIGHT:
            case Keys.D:
            {
                ActionHandler.setActive(Action.MOVE_RIGHT, false);
                break;
            }
            case Keys.ESCAPE:
            {
                ActionHandler.setActive(Action.PAUSE_MENU, false);
                break;
            }
            case Keys.SPACE:
            {
                ActionHandler.setActive(Action.SHOOT, false);
                break;
            }
            default:
            {
                return false;
            }
        }
        return true;
    }
}
