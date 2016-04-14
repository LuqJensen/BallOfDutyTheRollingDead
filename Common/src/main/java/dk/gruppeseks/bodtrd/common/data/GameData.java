/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data;

import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body.Geometry;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;

/**
 *
 * @author Morten
 */
public class GameData
{
    private int _displayWidth;
    private int _displayHeight;

    private double _deltaTime;
    private GameState _gameState;

    private Position _mousePosition = new Position(0, 0);

    private Entity _player;

    public Position getMousePosition()
    {
        return _mousePosition;
    }

    public void setMousePosition(int x, int y)
    {
        _mousePosition.setX(x);
        _mousePosition.setY(y);
    }

    public Entity getPlayer() 
    {
        return _player;
    }

    public void setPlayer(Entity player) 
    {
        this._player = player;
    }

    public int getDisplayWidth()
    {
        return _displayWidth;
    }

    public void setDisplayWidth(int displayWidth)
    {
        this._displayWidth = displayWidth;
    }

    public int getDisplayHeight()
    {
        return _displayHeight;
    }

    public void setDisplayHeight(int displayHeight)
    {
        this._displayHeight = displayHeight;
    }

    public GameState getGameState()
    {
        return _gameState;
    }

    public void setGameState(GameState gameState)
    {
        this._gameState = gameState;
    }

    public double getDeltaTime()
    {
        return _deltaTime;
    }

    public void setDeltaTime(double deltaTime)
    {
        this._deltaTime = deltaTime;
    }
}
