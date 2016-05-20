/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.aifov;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.Path;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.AIData;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;
import dk.gruppeseks.bodtrd.common.exceptions.NoPathException;
import dk.gruppeseks.bodtrd.common.services.AISPI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Morten
 */
@ServiceProvider(service = AISPI.class)
public class AIFoVProvider implements AISPI
{
    public static World _world;
    private static final int RAY_COUNT = 200;
    private static final int VISION_DISTANCE = 350;
    private static final double VISION_DEGREES = 110;
    private static final int DUMB_AI_RANGE = 80;
    private static final double ROTATE_MAX = 1.3;
    private java.util.Map<Integer, Position> latestKnownPosition = new ConcurrentHashMap();
    private java.util.Map<Integer, Long> lastRotateUpdate = new ConcurrentHashMap();
    private java.util.Map<Integer, Long> lastRandom = new ConcurrentHashMap();

    @Override
    public Path getPath(Entity entity, Entity target, World world) throws NoPathException
    {
        if (Installer.uninstalled)
        {
            return null;
        }
        AIData dat = entity.get(AIData.class);
        synchronized (dat)
        {
            List<Position> path = new ArrayList<>();
            _world = world;
            Position targetPos = target.get(Position.class);
            Position entPos = entity.get(Position.class);
            Body targetBod = target.get(Body.class);
            Body entBod = entity.get(Body.class);

            Position targetCenter = new Position(targetPos.getX() + targetBod.getWidth() / 2, targetPos.getY() + targetBod.getHeight() / 2);
            Position entCenter = new Position(entPos.getX() + entBod.getWidth() / 2, entPos.getY() + entBod.getHeight() / 2);
            Position raycastPos = rayCast(entity, targetCenter, entBod.getWidth() / 2);

            Vector2 distance = new Vector2(targetCenter, entCenter);
            if (distance.getMagnitude() < DUMB_AI_RANGE)
            {
                path.add(targetCenter);
                dat.setLatestKnownPosition(null);
            }
            else if (raycastPos != null)
            {
                latestKnownPosition.put(entity.getID(), raycastPos);
                path.add(raycastPos);
                dat.setLatestKnownPosition(null);

            }
            else
            {
                dat.setLatestKnownPosition(latestKnownPosition.get(entity.getID()));
            }
            long timeSince = 0;
            if (lastRotateUpdate.get(entity.getID()) != null)
            {
                timeSince = System.currentTimeMillis() - lastRotateUpdate.get(entity.getID());

            }

            if (lastRandom.get(entity.getID()) == null || timeSince > lastRandom.get(entity.getID()))
            {
                dat.setRotateSpeed(-ROTATE_MAX + Math.random() * ROTATE_MAX * 2);
                lastRotateUpdate.put(entity.getID(), System.currentTimeMillis());
                lastRandom.put(entity.getID(), (long)(Math.random() * 2000));
            }
            Position[] pathArray = new Position[path.size()];
            for (int i = 0; i < path.size(); i++)
            {
                pathArray[i] = path.get(i);
            }

            return new Path(pathArray);
        }

    }

    private Position rayCast(Entity zombie, Position targetCenter, int radius)
    {
        ArrayList<Float> shape = new ArrayList<>();
        double degreesPerStep = VISION_DEGREES / (double)RAY_COUNT; // 1.5
        Vector2 orientation = zombie.get(Body.class).getOrientation();
        Vector2 startRay = new Vector2(orientation);
        startRay.rotateDegrees(-VISION_DEGREES / 2);

        Position p = zombie.get(Position.class);
        Body b = zombie.get(Body.class);

        Position zombieCenter = new Position(p.getX() + b.getWidth() / 2, p.getY() + b.getHeight() / 2);
        Position retval = null;
        boolean playerCollision = false;
        for (int i = 0; i < RAY_COUNT; i++)
        {
            double rotAmount = degreesPerStep * i;
            Vector2 ray = new Vector2(startRay);
            ray.setMagnitude(VISION_DISTANCE);
            ray.rotateDegrees(rotAmount);
            Position collision = getFirstCollision(ray, zombieCenter);
            if (!playerCollision)
            {
                Position playerCollisionPoint = lineCircleCollision(targetCenter, radius, zombieCenter, collision);
                if (playerCollisionPoint != null)
                {
                    retval = playerCollisionPoint;
                    playerCollision = true;
                }

            }
            shape.add((float)collision.getX());
            shape.add((float)collision.getY());
        }

        shape.add((float)zombieCenter.getX());
        shape.add((float)zombieCenter.getY());

        float[] floatShape = new float[shape.size()];
        for (int i = 0; i < shape.size(); i++)
        {
            floatShape[i] = shape.get(i);
        }
        AIData aiData = zombie.get(AIData.class);
        aiData.setFoVShape(floatShape);
        return retval;

    }

    private Position getFirstCollision(Vector2 ray, Position start)
    {
        boolean[][] grid = _world.getMap().getGrid();
        int cellSize = _world.getMap().getGridCellSize();

        for (int i = 0; i < ray.getMagnitude() * 5 - 1; i++) // change to *1 / nothing, this is for demo purposes
        {
            Vector2 v = new Vector2(ray);
            v.setMagnitude(i / 5);
            Position p = new Position(start, v);

            int x = (int)p.getX() / cellSize;
            int y = (int)p.getY() / cellSize;
            if (x >= 0 && y >= 0 && x < grid.length && y < grid[0].length && !grid[x][y])
            {
                return p;
            }

        }
        return new Position(start, ray);    //No collision
    }

    private Position lineCircleCollision(Position circleCen, int circleRadius, Position start, Position end)
    {
        Vector2 endToTarget = new Vector2(end, circleCen);
        Vector2 startToTarget = new Vector2(start, circleCen);
        Vector2 startToEnd = new Vector2(start, end); // This is the ray (Start point on line to end point on line).

        double dotProduct = startToTarget.scalarProduct(startToEnd);
        double squaredMagnitude = Math.pow(startToEnd.getX(), 2) + Math.pow(startToEnd.getY(), 2);
        double t = dotProduct / squaredMagnitude;
        Position closest = new Position(start.getX() + startToEnd.getX() * t, start.getY() + startToEnd.getY() * t);

        Vector2 targetToNearest = new Vector2(circleCen, closest);
        double rayLength = startToEnd.getMagnitude();

        boolean lineCollision = ((targetToNearest.getMagnitude() < circleRadius) && (startToTarget.getMagnitude() < (circleRadius + rayLength)) && (endToTarget.getMagnitude() < (circleRadius + rayLength)));
        if (lineCollision)
        {
            return closest;
        }

        return null;

    }

}
