/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.ai;

import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.services.AISPI;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author frede
 */
@ServiceProvider(service = AISPI.class)
public class AIProvider implements AISPI
{
    boolean[][] _grid;

    @Override
    public Position[] getPath(Position start, Position goal, World world)
    {
        //The following is dummy-code, untill the map is fixed
        boolean[][] testGrid =
        {
            {
                true, true, true, true
            },
            {
                true, false, true, false
            },
            {
                true, false, true, true
            },
            {
                true, true, false, true
            }
        };
        world.getGameData().setGrid(testGrid);
        world.getGameData().setCellsize(50);
        //End of dummy-code

        _grid = world.getGameData().getGrid();
        int cellSize = world.getGameData().getCellsize();

        int startX = (int)(start.getX() / cellSize);
        int startY = (int)(start.getY() / cellSize);

        int goalX = (int)(goal.getX() / cellSize);
        int goalY = (int)(goal.getY() / cellSize);

        Node endNode = AStarSearch(startX, startY, goalX, goalY);
        List<Node> path = endNode.getPath();
        Position[] output = new Position[path.size()];

        for (int i = 0; i < path.size(); i++)
        {
            Position temp = new Position(path.get(i).getCellX() * cellSize + cellSize / 2, path.get(i).getCellY() * cellSize + cellSize / 2);
            output[output.length - 1 - i] = temp;
        }

        return output;
    }

    private Node removeBest(List<Node> fringe, int goalX, int goalY)
    {
        int bestNode = 0;
        double cost = fringe.get(bestNode).heuristic(goalX, goalY);

        for (int i = 0; i < fringe.size(); i++)
        {
            double current = fringe.get(i).heuristic(goalX, goalY);
            if (current < cost)
            {
                cost = current;
                bestNode = i;
            }
        }
        Node output = fringe.get(bestNode);
        fringe.remove(bestNode);
        return output;
    }

    ;
    private Node AStarSearch(int startX, int startY, int goalX, int goalY)
    {
        List<Node> fringe = new ArrayList();
        Node root = new Node(startX, startY, null, 0);

        fringe.add(root);
        while (fringe.size() > 0)
        {
            Node bestNode = removeBest(fringe, goalX, goalY);

            if (bestNode.getCellX() == goalX && bestNode.getCellY() == goalY)
            {
                return bestNode;
            }
            fringe.addAll(expand(bestNode));
        }
        return null;
    }

    ;
    private List<Node> expand(Node node)
    {
        int downX = node.getCellX();
        int downY = node.getCellY() - 1;

        int upX = node.getCellX();
        int upY = node.getCellY() + 1;

        int rightX = node.getCellX() + 1;
        int rightY = node.getCellY();

        int leftX = node.getCellX() - 1;
        int leftY = node.getCellY();

        List<Node> expansion = new ArrayList();

        if (downY >= 0 && _grid[downX][downY])
        {
            expansion.add(new Node(downX, downY, node, node.getDepth() + 1));
        }
        if (upY < _grid[0].length && _grid[upX][upY])
        {
            expansion.add(new Node(upX, upY, node, node.getDepth() + 1));
        }
        if (rightX < _grid.length && _grid[rightX][rightY])
        {
            expansion.add(new Node(rightX, rightY, node, node.getDepth() + 1));
        }
        if (leftX >= 0 && _grid[leftX][leftY])
        {
            expansion.add(new Node(leftX, leftY, node, node.getDepth() + 1));
        }

        return expansion;
    }

    ;
    private void insert()
    {
    }

    ;
    private void insertAll()
    {
    }

    ;
    private void empty()
    {
    }

    ;
    private void removeFirst()
    {
    }

    ;
    private void solution()
    {
    }
;

}
