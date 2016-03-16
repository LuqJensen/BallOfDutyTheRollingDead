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
import dk.gruppesex.bodtrd.common.data.entityelements.View;
import java.io.File;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Thanusaan
 */
public class MapGenerator
{
    private static Random _rand = new Random(); // TODO needs to be able to pick seed
    private final static int WALL_AMOUNT = 50;
    private final static int WALL_SIZE = 50;
    private final static String WALL_IMAGE_FILE_PATH = "../../../Map/src/main/java/dk/gruppesex/bodtrd/map/assets/wall_box.png";

    private static int _pathUp = 20;
    private static int _pathRight = 40;
    private static int _pathDown = 60;
    private static int _pathLeft = 80;

    public static void generateMap(List<Entity> walls, GameData gameData)
    {
        int mapWidth = gameData.getMapWidth();
        int mapHeight = gameData.getMapHeight();
        int mapGridX = mapWidth / WALL_SIZE;
        int mapGridY = mapHeight / WALL_SIZE;

        Position pos = new Position(0, 0);

        for (int i = 0; i < mapGridX; i++)
        {
            walls.add(createWall(pos));
            pos.setX(pos.getX() + WALL_SIZE);
        }

        pos.setX(0);
        pos.setY(mapHeight - WALL_SIZE);

        for (int i = 0; i < mapGridX; i++)
        {
            walls.add(createWall(pos));
            pos.setX(pos.getX() + WALL_SIZE);
        }

        pos.setX(0);
        pos.setY(WALL_SIZE);

        for (int i = 0; i < mapGridY - 2; i++)
        {
            walls.add(createWall(pos));
            pos.setY(pos.getY() + WALL_SIZE);
        }

        pos.setX(mapWidth - WALL_SIZE);
        pos.setY(WALL_SIZE);

        for (int i = 0; i < mapGridY - 2; i++)
        {
            walls.add(createWall(pos));
            pos.setY(pos.getY() + WALL_SIZE);
        }

        int wallCount = 0;

        while (true)
        {
            while (true)
            {
                pos.setX((_rand.nextInt(mapGridX) + 1) * WALL_SIZE);
                pos.setY((_rand.nextInt(mapGridY) + 1) * WALL_SIZE);

                if (checkValidWall(pos, walls))
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
                    pos.setY(pos.getY() - WALL_SIZE);
                }
                else if (r < _pathRight)
                {
                    pos.setY(pos.getX() + WALL_SIZE);
                }
                else if (r < _pathDown)
                {
                    pos.setY(pos.getY() + WALL_SIZE);
                }
                else if (r < _pathLeft)
                {
                    pos.setY(pos.getX() - WALL_SIZE);
                }
                else
                {
                    break;
                }

                if (checkValidWall(pos, walls))
                {
                    walls.add(createWall(pos));
                    pos = pos2;
                    ++wallCount;
                }

                if (wallCount >= WALL_AMOUNT)
                {
                    break;
                }
            }
            if (wallCount >= WALL_AMOUNT)
            {
                break;
            }
        }
    }

    private static boolean checkValidWall(Position newWallPos, List<Entity> walls)
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

        // Position is pass by reference and thus we must copy its values. Otherwise all Walls will have the same position.
        wall.add(new Position(pos.getX(), pos.getY()));
        wall.add(new Body(WALL_SIZE, WALL_SIZE));
        wall.add(new View(new File(WALL_IMAGE_FILE_PATH)));

        return wall;
    }
}
