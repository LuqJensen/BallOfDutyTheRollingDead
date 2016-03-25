/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data;

import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body.Geometry;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.View;

/**
 *
 * @author Morten
 */
public class GameData
{
    private int _mapWidth;
    private int _mapHeight;
    private View _mapTextureView;

    private int _displayWidth;
    private int _displayHeight;

    private double _deltaTime;
    private GameState _gameState;

    private Position _mousePosition = new Position(0, 0);

    private Position _playerPosition = new Position(0, 0);
    private Body _playerBody = new Body(0, 0, Geometry.CIRCLE);

    public Position getMousePosition()
    {
        return _mousePosition;
    }

    public void setMousePosition(int x, int y)
    {
        _mousePosition.setX(x);
        _mousePosition.setY(y);
    }

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

    public View getMapTextureView()
    {
        return _mapTextureView;
    }

    public void setMapTextureView(View _mapTextureView)
    {
        this._mapTextureView = _mapTextureView;
    }

}
