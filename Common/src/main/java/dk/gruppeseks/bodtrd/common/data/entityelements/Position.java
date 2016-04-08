/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data.entityelements;

import java.io.Serializable;

/**
 *
 * @author Morten
 */
public class Position implements Serializable
{
    private double _x;
    private double _y;

    @Override
    public String toString()
    {
        return "Position{" + "_x=" + _x + ", _y=" + _y + '}';
    }

    public Position(double x, double y)
    {
        this._x = x;
        this._y = y;
    }

    public double getX()
    {
        return _x;
    }

    public void setX(double x)
    {
        this._x = x;
    }

    public double getY()
    {
        return _y;
    }

    public void setY(double y)
    {
        this._y = y;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Position other = (Position)obj;
        if (_x != other.getX() || _y != other.getY())
        {
            return false;
        }
        return true;
    }
}
