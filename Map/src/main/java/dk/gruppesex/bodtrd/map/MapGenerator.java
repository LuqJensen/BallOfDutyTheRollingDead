/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.map;

import dk.gruppesex.bodtrd.common.data.Entity;
import dk.gruppesex.bodtrd.common.data.EntityType;
import dk.gruppesex.bodtrd.common.data.GameData;
import dk.gruppesex.bodtrd.common.data.entityelements.Body;
import dk.gruppesex.bodtrd.common.data.entityelements.Position;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Thanusaan
 */
public class MapGenerator
{

    private static Random _rand = new Random(); // TODO needs to be able to pick seed
    private static int _wallAmount = 50;
    private static int _wallSize = 50;

    private static int _pathUp = 20;
    private static int _pathRight = 40;
    private static int _pathDown = 60;
    private static int _pathLeft = 80;

    public static void GenerateMap(ArrayList<Entity> walls, GameData gameData)
    {
        int mapWidth = gameData.getMapWidth();
        int mapHeight = gameData.getMapHeight();
        int mapGridX = mapWidth / _wallSize;
        int mapGridY = mapHeight / _wallSize;

        Position pos = new Position(0, 0);

        for (int i = 0; i < mapGridX; i++)
        {
            walls.add(createWall(pos));
            pos.setX(pos.getX() + _wallSize);
        }

        pos.setX(0);
        pos.setY(mapHeight - _wallSize);

        for (int i = 0; i < mapGridX; i++)
        {
            walls.add(createWall(pos));
            pos.setX(pos.getX() + _wallSize);
        }

        pos.setX(0);
        pos.setY(_wallSize);

        for (int i = 0; i < mapGridY - 2; i++)
        {
            walls.add(createWall(pos));
            pos.setY(pos.getY() + _wallSize);
        }

        pos.setX(mapWidth - _wallSize);
        pos.setY(_wallSize);

        for (int i = 0; i < mapGridY - 2; i++)
        {
            walls.add(createWall(pos));
            pos.setY(pos.getY() + _wallSize);
        }

        int wallCount = 0;

        while (true)
        {
            while (true)
            {
                pos.setX((_rand.nextInt(mapGridX) + 1) * _wallSize);
                pos.setY((_rand.nextInt(mapGridY) + 1) * _wallSize);

                if (CheckValidWall(pos, walls))
                {
                    walls.add(createWall(pos));
                    ++wallCount;
                    break;
                }
            }
            while (true)
            {
                Position pos2 = new Position(pos.getX(), pos.getY());
                int r = _rand.nextInt(100);

                if (r < _pathUp)
                {
                    pos.setY(pos.getY() - _wallSize);
                }
                else if (r < _pathRight)
                {
                    pos.setY(pos.getX() + _wallSize);
                }
                else if (r < _pathDown)
                {
                    pos.setY(pos.getY() + _wallSize);
                }
                else if (r < _pathLeft)
                {
                    pos.setY(pos.getX() - _wallSize);
                }
                else
                {
                    break;
                }

                if (CheckValidWall(pos, walls))
                {
                    walls.add(createWall(pos));
                    pos = pos2;
                    ++wallCount;
                }

                if (wallCount >= _wallAmount)
                {
                    break;
                }
            }
            if (wallCount >= _wallAmount)
            {
                break;
            }
        }
    }

    private static boolean CheckValidWall(Position newWallPos, ArrayList<Entity> walls)
    {
        for (Entity w : walls)
        {
            if (newWallPos.equals(w.get(Position.class)))
            {
                return false;
            }
        }
        return true;
    }

    private static Entity createWall(Position pos)
    {
        Entity wall = new Entity();
        wall.setType(EntityType.WALL);

        wall.add(pos);
        wall.add(new Body(_wallSize));

        return wall;
    }
}
