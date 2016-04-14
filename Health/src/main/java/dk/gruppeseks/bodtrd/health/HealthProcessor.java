package dk.gruppeseks.bodtrd.health;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityState;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.DamageInstance;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.Health;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;

/**
 *
 * @author Thanusaan
 */
public class HealthProcessor implements IEntityProcessor
{
    @Override
    public void process(World world)
    {
        for (Entity e : world.entities())
        {
            Health health = e.get(Health.class);

            if (health != null)
            {
                takeDamage(health);
                if (health.getHp() <= 0)
                {
                    e.setState(EntityState.DESTROYED);
                    continue;
                }
                regenHealth(health, world.getGameData().getDeltaTime());
            }
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

    private void takeDamage(Health h)
    {
        for (DamageInstance di : h.getDamageInstances())
        {
            h.setHp(h.getHp() - di.getDamage());
        }
        h.getDamageInstances().clear();
    }

    private void regenHealth(Health h, double deltaTime)
    {
        if (h.getHp() < h.getMaxHp())
        {
            double newHealth = h.getHp() + (h.getHpRegen() * deltaTime);
            if (newHealth > h.getMaxHp())
            {
                newHealth = h.getMaxHp();
            }
            h.setHp(newHealth);
        }
    }

}
