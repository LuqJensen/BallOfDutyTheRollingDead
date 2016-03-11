/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.common.data;

/**
 *
 * @author Morten
 */
public class GameData
{
    private int _mapWidth;
    private int _mapHeight;

    private int displayWidth;

    public int getDisplayWidth()
    {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth)
    {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight()
    {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight)
    {
        this.displayHeight = displayHeight;
    }
    private int displayHeight;
    private double _deltaTime;
    private GameState gameState;

    public GameState getGameState()
    {
        return gameState;
    }

    public void setGameState(GameState gameState)
    {
        this.gameState = gameState;
    }

    public GameData()
    {

    }

    public double getDeltaTime()
    {
        return _deltaTime;
    }

    public void setDeltaTime(double _deltaTime)
    {
        this._deltaTime = _deltaTime;
    }

    public int getMapWidth()
    {
        return _mapWidth;
    }

    public int getMapHeight()
    {
        return _mapHeight;
    }

}
