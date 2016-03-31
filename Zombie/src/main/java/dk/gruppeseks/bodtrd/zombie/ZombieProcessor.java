/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.zombie;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.common.services.AISPI;
import java.util.Map;

/**
 *
 * @author frede
 */
public class ZombieProcessor implements IEntityProcessor
{
    private Map<Integer, Entity> _zombies;
    private AISPI ai;
    private final int MOVEMENT_SPEED = 200;

    private final int AGGRO_RANGE = 500;

    public ZombieProcessor(Map<Integer, Entity> zombies)
    {
        _zombies = zombies;
    }

    @Override
    public void process(World world)
    {
        Position playerPos = world.getGameData().getPlayerPosition();
        for (Entity zombie : _zombies.values())
        {
            Position zombiePos = zombie.get(Position.class);
            Velocity zombieVel = zombie.get(Velocity.class);
            double dx = playerPos.getX() - zombiePos.getX();
            double dy = playerPos.getY() - zombiePos.getY();
            Vector2 velocity = new Vector2(dx, dy);
            if (velocity.getMagnitude() < AGGRO_RANGE)
            {
                velocity.setMagnitude(MOVEMENT_SPEED);

            }
            else
            {
                velocity.setMagnitude(0);
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

}
