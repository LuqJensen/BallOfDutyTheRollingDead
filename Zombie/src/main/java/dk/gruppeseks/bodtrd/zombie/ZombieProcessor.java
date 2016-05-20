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
import java.util.Map;

/**
 *
 * @author frede
 */
public class ZombieProcessor implements IEntityProcessor
{
    private Map<Integer, Entity> _zombies;
    private World _world;
    private final int MOVEMENT_SPEED = 130;
    private final int AGGRO_RANGE = 500; // When it should start following a path towards the target.
    private final int DUMB_AI_RANGE = 80; // When it shouldnt follow a path but just go directly to the target.
    private final int ATTACK_RANGE = 10; // When it should attack the target
    private final int RAY_COUNT = 100;
    private final int VISION_DISTANCE = 350;
    private final double VISION_DEGREES = 90;

    public ZombieProcessor(Map<Integer, Entity> zombies)
    {
        _zombies = zombies;

    }

    @Override
    public void process(World world)
    {
        _world = world;
        Entity player = world.getGameData().getPlayer();
        if (player == null)
        {
            return;
        }
        Position playerPos = player.get(Position.class);
        Body playerBod = player.get(Body.class);
        long currentTime = System.currentTimeMillis();
        if (playerPos == null || playerBod == null)
        {
            return;
        }
        Position playerCenter = new Position(playerPos.getX() + playerBod.getWidth() / 2, playerPos.getY() + playerBod.getHeight() / 2);

        for (Entity zombie : world.entities())
        {
            if (zombie.getType() != EntityType.ENEMY)
            {
                continue;
            }
            Position zombiePos = zombie.get(Position.class);
            Body zombieBod = zombie.get(Body.class);
            Position zombieCenter = new Position(zombiePos.getX() + zombieBod.getWidth() / 2, zombiePos.getY() + zombieBod.getHeight() / 2);
            Velocity zombieVel = zombie.get(Velocity.class);
            Vector2 velocity = new Vector2(zombieCenter, playerCenter);
            double distance = velocity.getMagnitude();

            velocity.setMagnitude(0);
            AIData aiDat = zombie.get(AIData.class);

            try
            {
                Path newPath = null;
                if (ZombiePlugin._ai != null)
                {
                    newPath = ZombiePlugin._ai.getPath(zombie, player, world);

                    if (newPath != null && newPath.size() > 0)
                    {
                        aiDat.setPath(newPath);
                    }
                }

            }
            catch (NoPathException ex)
            {
                System.out.println("There is no path to the target");
            }

            Path path = aiDat.getPath();

            if (path != null && path.size() > 0)
            {
                Vector2 currentVector = new Vector2(zombieCenter, path.peekPosition());

                if (currentVector.getMagnitude() < 10)
                {
                    if (path.size() > 0)
                    {
                        path.popPosition();
                    }
                }

                if (path.size() > 0)
                {
                    velocity = new Vector2(zombieCenter, path.peekPosition());
                    velocity.setMagnitude(MOVEMENT_SPEED);
                }

            }
            Vector2 playerZombieVec = new Vector2(zombiePos, playerPos);
            if (playerZombieVec.getMagnitude() - playerBod.getWidth() / 2 - zombieBod.getWidth() / 2 < ATTACK_RANGE)
            {
                attemptAttackTarget(zombie, player);
            }

            if (velocity.getMagnitude() != 0)
            {
                zombieBod.setOrientation(velocity);
            }
            else
            {
                zombieBod.getOrientation().rotateDegrees(aiDat.getRotateSpeed());
            }

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

    private void attemptAttackTarget(Entity zombie, Entity target)
    {
        Weapon zWep = zombie.get(Weapon.class);

        if (zWep != null && zWep.getAttackCooldown() <= 0)
        {
            target.get(Health.class).addDamageInstance(new DamageInstance(zWep.getAttackDamage(), zombie.getID()));
            zWep.setAttackCooldown(zWep.getAttackSpeed());
        }
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
