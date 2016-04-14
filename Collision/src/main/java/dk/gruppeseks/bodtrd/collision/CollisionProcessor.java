/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.collision;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityState;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Damage;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.DamageInstance;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.Health;
import dk.gruppeseks.bodtrd.common.data.entityelements.Owner;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import java.util.Collection;

/**
 *
 * @author Chris
 */
public class CollisionProcessor implements IEntityProcessor
{
    @Override
    public void process(World world)
    {
        Collection<Entity> entities = world.entities(); // todo move to World. we should only make one immutable collection per process call.

        for (Entity handled : entities)
        {
            Position handledPos = handled.get(Position.class);
            Velocity handledVel = handled.get(Velocity.class);
            Body handledBody = handled.get(Body.class);
            CollisionData handledData = handled.get(CollisionData.class);

            if (handledPos == null || handledVel == null || handledBody == null || handledData == null)
            {
                continue;
            }

            for (Entity ent : entities)
            {

                CollisionData entData = ent.get(CollisionData.class);

                if (ent.getID() == handled.getID() || entData == null)
                {
                    continue;
                }
                if (CollisionHandler.isColliding(handled, ent))
                {
                    handleCollision(handled, ent, entities);
                }
            }
        }
    }

    private void handleCollision(Entity handled, Entity ent, Collection<Entity> entities)
    {
        switch (handled.getType())
        {
            case PLAYER:
            {
                if (ent.getType() == EntityType.PROJECTILE)
                {

                }
                else if (ent.getType() == EntityType.WALL)
                {
                    calculateBounceResponse(handled, ent, entities);
                }
                break;
            }
            case PROJECTILE:
            {
                if (ent.getState() == EntityState.DESTROYED)
                {
                    break;
                }
                if (ent.getType() == EntityType.ENEMY)
                {
                    Owner o = handled.get(Owner.class);
                    if (o == null)
                    {
                        ent.get(Health.class).addDamageInstance(new DamageInstance(handled.get(Damage.class), 0));
                    }
                    else
                    {
                        ent.get(Health.class).addDamageInstance(new DamageInstance(handled.get(Damage.class), o.getId()));
                    }
                    handled.setState(EntityState.DESTROYED);
                }
                else if (ent.getType() == EntityType.WALL)
                {
                    handled.setState(EntityState.DESTROYED);
                }
                break;
            }
            case ENEMY:
            {
                if (ent.getType() == EntityType.PLAYER || ent.getType() == EntityType.ENEMY || ent.getType() == EntityType.WALL)
                {
                    calculateBounceResponse(handled, ent, entities);
                }
                break;
            }
        }
    }

    private void calculateBounceResponse(Entity e1, Entity e2, Collection<Entity> entities)
    {

        Position firstPos = CollisionHandler.collisionResponse(e1, e2);
        Position handledPos = e1.get(Position.class);
        handledPos.setX(firstPos.getX());
        handledPos.setY(firstPos.getY());

        for (Entity e3 : entities) // Checks if it collides with anything.
        {
            CollisionData e3Data = e3.get(CollisionData.class);

            if (e3.getID() == e1.getID() || e3.getID() == e2.getID() || e3Data == null)
            {
                continue;
            }
            if (CollisionHandler.isColliding(e1, e3))
            {
                for (Entity e4 : entities)
                {
                    CollisionData e4Data = e4.get(CollisionData.class);

                    if (e4.getID() == e1.getID() || e4.getID() == e2.getID()
                            || e4.getID() == e3.getID() || e4Data == null)
                    {
                        continue;
                    }
                    if (CollisionHandler.isColliding(e1, e4))
                    {
                        double startX = handledPos.getX();
                        double startY = handledPos.getY();

                        handledPos.setX(startX);
                        handledPos.setY(startY); // If it even collides after a third collision correction, then dont do any corrections at all.
                        break;
                    }

                }
                // if it doesnt collide with a third object after the second collision correction, put it to the secondcalculated position.
                Position response = CollisionHandler.collisionResponse(e1, e3);
                handledPos.setX(response.getX());
                handledPos.setY(response.getY());
            }
            break;
        }
    }

    @Override
    public void notifyEntitiesAdded(Entity entity)
    {
        CollisionHandler.addCollisionData(entity);
    }

    @Override
    public void notifyEntitiesRemoved(Entity entity)
    {
        entity.remove(CollisionData.class);
    }

}
