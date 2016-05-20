/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data.entityelements;

import dk.gruppeseks.bodtrd.common.data.util.Vector2;

/**
 *
 * @author Thanusaan
 */
public class Body
{
    private int _height;
    private int _width;
    private Geometry _geometry;
    private Vector2 _orientation;

    public enum Geometry
    {
        RECTANGLE, CIRCLE
    }

    public Body(int height, int width, Geometry geometry)
    {
        _height = height;
        _width = width;
        _geometry = geometry;
    }

    public Geometry getGeometry()
    {
        return _geometry;
    }

    public void setGeometry(Geometry geometry)
    {
        this._geometry = geometry;
    }

    public int getHeight()
    {
        return _height;
    }

    public void setHeight(int height)
    {
        this._height = height;
    }

    public int getWidth()
    {
        return _width;
    }

    public void setWidth(int width)
    {
        this._width = width;
    }

    public Vector2 getOrientation()
    {
        return _orientation;
    }

    public void setOrientation(Vector2 _orientation)
    {
        this._orientation = new Vector2(_orientation);
    }

}
