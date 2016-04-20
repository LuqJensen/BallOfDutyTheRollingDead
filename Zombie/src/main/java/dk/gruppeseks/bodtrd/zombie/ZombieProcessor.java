/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.zombie;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.Path;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.AIData;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.DamageInstance;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.Health;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;
import dk.gruppeseks.bodtrd.common.exceptions.NoPathException;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.common.services.AISPI;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author frede
 */
public class ZombieProcessor implements IEntityProcessor
{
    private Map<Integer, Entity> _zombies;
    private AISPI _ai;
    private World _world;
    private final int MOVEMENT_SPEED = 130;
    private final int AGGRO_RANGE = 500; // When it should start following a path towards the target.
    private final int DUMB_AI_RANGE = 80; // When it shouldnt follow a path but just go directly to the target.
    private final int ATTACK_RANGE = 10; // When it should attack the target
    private final int TIME_BETWEEN_PATH_UPDATE = 1500; // How many milliseconds between path update
    private final int RAY_COUNT = 60;
    private final int VISION_DISTANCE = 350;

    public ZombieProcessor(Map<Integer, Entity> zombies, AISPI ai)
    {
        _zombies = zombies;
        _ai = ai;

    }

    @Override
    public void process(World world)
    {
        _world = world;
        Entity player = world.getGameData().getPlayer();
        Position playerPos = player.get(Position.class);
        Body playerBod = player.get(Body.class);
        long currentTime = System.currentTimeMillis();
        if (playerPos == null || playerBod == null)
        {
            return;
        }
        Position playerCenter = new Position(playerPos.getX() + playerBod.getWidth() / 2, playerPos.getY() + playerBod.getHeight() / 2);

        for (Entity ent : world.entities())
        {
            if (ent.getType() != EntityType.ENEMY)
            {
                continue;
            }
            Position zombiePos = ent.get(Position.class);
            Body zombieBod = ent.get(Body.class);
            Position zombieCenter = new Position(zombiePos.getX() + zombieBod.getWidth() / 2, zombiePos.getY() + zombieBod.getHeight() / 2);
            Velocity zombieVel = ent.get(Velocity.class);
            Vector2 velocity = new Vector2(zombieCenter, playerCenter);
            Weapon zWeap = ent.get(Weapon.class);
            zWeap.setAttackCooldown((float)(zWeap.getAttackCooldown() - world.getGameData().getDeltaTime()));
            if (velocity.getMagnitude() < DUMB_AI_RANGE)
            {
                if (velocity.getMagnitude() <= ATTACK_RANGE + (zombieBod.getWidth() / 2) + (playerBod.getWidth() / 2))
                {
                    attackPlayer(ent, player);
                }
                velocity.setMagnitude(MOVEMENT_SPEED);
            }
            else if (velocity.getMagnitude() < AGGRO_RANGE)
            {
                AIData aiDat = ent.get(AIData.class);
                Path path = aiDat.getPath();

                if (path == null || (currentTime - path.getCreationTime()) > TIME_BETWEEN_PATH_UPDATE)
                {
                    try
                    {
                        aiDat.setPath(_ai.getPath(zombieCenter, playerCenter, world));
                        path = aiDat.getPath();
                        velocity = new Vector2(zombieCenter, path.peekPosition());
                    }
                    catch (NoPathException ex)
                    {
                        velocity.setMagnitude(MOVEMENT_SPEED);
                    }
                }
                if (aiDat.getPath() != null)
                {
                    Vector2 currentVector = new Vector2(zombieCenter, path.peekPosition());
                    if (currentVector.getMagnitude() < 10) // Currently the fact that their position isn't at center is a problem, because sometimes it goes under 851-849.99 for example and it thinks its on the other grid. etc etc
                    {
                        if (path.size() > 1)
                        {
                            path.popPosition();
                        }

                    }
                    velocity = new Vector2(zombieCenter, path.peekPosition());
                    velocity.setMagnitude(MOVEMENT_SPEED);
                }
            }
            else
            {
                velocity.setMagnitude(0); // Should maybe roam about and not just stay still?
            }
            rayCast(ent);
            zombieVel.setVector(velocity);
        }
    }

    @Override
    public void notifyEntitiesAdded(Entity entity)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyEntitiesRemoved(Entity entity)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void attackPlayer(Entity zombie, Entity player)
    {
        Weapon zWep = zombie.get(Weapon.class);

        if (zWep.getAttackCooldown() <= 0)
        {
            player.get(Health.class).addDamageInstance(new DamageInstance(zWep.getAttackDamage(), zombie.getID()));
            zWep.setAttackCooldown(zWep.getAttackSpeed());
        }
    }

    private void rayCast(Entity zombie)
    {
        ArrayList<Float> shape = new ArrayList<>();
        double rotSteps = 360 / RAY_COUNT;

        for (int i = 0; i < RAY_COUNT; i++)
        {
            double rotAmount = rotSteps * i;
            Vector2 ray = new Vector2(0, 1);
            ray.setMagnitude(VISION_DISTANCE);
            ray.rotateDegrees(rotAmount);
            Position collision = getFirstCollision(ray, zombie.get(Position.class));
            shape.add((float)collision.getX());
            shape.add((float)collision.getY());
        }
        float[] floatShape = new float[shape.size()];
        for (int i = 0; i < shape.size(); i++)
        {
            floatShape[i] = shape.get(i);
        }
        AIData aiData = zombie.get(AIData.class);
        aiData.setFoVShape(floatShape);
    }

    private Position getFirstCollision(Vector2 ray, Position start)
    {
        boolean[][] grid = _world.getMap().getGrid();
        int cellSize = _world.getMap().getGridCellSize();

        for (int i = 0; i < ray.getMagnitude(); i += 2)
        {
            Vector2 v = new Vector2(ray);
            v.setMagnitude(i);
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
}
