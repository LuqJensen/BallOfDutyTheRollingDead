/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.ai;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dzenita Hasic
 */
public class Node
{
    private int _gridX;
    private int _gridY;
    private Node _parent;
    private int _depth;

    public int getCellX()
    {
        return _gridX;
    }

    public int getCellY()
    {
        return _gridY;
    }

    public int getDepth()
    {
        return _depth;
    }

    public void setDepth(int depth)
    {
        this._depth = depth;
    }

    public Node(int gridX, int gridY, Node parant, int depth)
    {
        this._gridX = gridX;
        this._gridY = gridY;
        this._parent = parant;
        this._depth = depth;
    }

    public double heuristic(int goalX, int goalY)
    {
        double a = _gridX - goalX;
        double b = _gridY - goalY;
        double c = Math.sqrt(a * a + b * b);
        return _depth + c;
    }

    public List<Node> getPath()
    {
        List<Node> output = new ArrayList();
        output.add(this);

        if (_parent == null)
        {
            return output;
        }
        output.addAll(_parent.getPath());
        return output;
    }

}
