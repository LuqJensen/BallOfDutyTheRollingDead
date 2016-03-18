/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data;

import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;

/**
 *
 * @author Morten
 */
public class GameData
{
    private int _mapWidth;
    private int _mapHeight;

    private int _displayWidth;
    private int _displayHeight;

    private double _deltaTime;
    private GameState _gameState;

    private Position _playerPosition;

    private Body _playerBody;

    public Body getPlayerBody()
    {
        return _playerBody;
    }

    public void setPlayerBody(Body body)
    {
        this._playerBody = body;
    }

    public Position getPlayerPosition()
    {
        return _playerPosition;
    }

    public void setPlayerPosition(Position position)
    {
        this._playerPosition = position;
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

    public int getMapWidth()
    {
        return _mapWidth;
    }

    public int getMapHeight()
    {
        return _mapHeight;
    }

    public void setMapWidth(int mapWidth)
    {
        this._mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight)
    {
        this._mapHeight = mapHeight;
    }
}
