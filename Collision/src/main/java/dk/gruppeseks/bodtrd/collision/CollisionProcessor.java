/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.collision;

import dk.gruppeseks.bodtrd.common.data.AudioAction;
import dk.gruppeseks.bodtrd.common.data.AudioManager;
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityState;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Damage;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.DamageInstance;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.Health;
import dk.gruppeseks.bodtrd.common.data.entityelements.LifeTime;
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
            if (handled.getState() == EntityState.DESTROYED)
            {
                continue;
            }
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
                if (handled.getState() == EntityState.DESTROYED)
                {
                    break;
                }
                if (ent.getState() == EntityState.DESTROYED)
                {
                    continue;
                }
                CollisionData entData = ent.get(CollisionData.class);

                if (ent.getID() == handled.getID() || entData == null)
                {
                    continue;
                }

                if (CollisionHandler.isColliding(handled, ent))
                {
                    handleCollision(handled, ent, entities, world);
                    break;
                }
            }
        }
    }

    private void handleCollision(Entity handled, Entity ent, Collection<Entity> entities, World world)
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
                    world.addEntity(createBlood(handled.get(Position.class)));
                    AudioManager.playSound(CollisionPlugin.HITMARKER_SOUND_FILE_PATH, AudioAction.PLAY);
                }
                else if (ent.getType() == EntityType.WALL)
                {
                    handled.setState(EntityState.DESTROYED);
                }
                break;
            }
            case ENEMY:
            {
                // eg. getCollisionFlag returns INTANGIBLE = 2 (base 10) = 10 binary. CONCRETE = 1 (base 10) = 01 binary
                // 10 and'ed with 01 = 00.
                // CONCRETE and'ed with CONCRETE = 01 (!= 0)
                if ((ent.get(CollisionData.class).getCollisionFlag() & CollisionFlags.CONCRETE) != 0)
                {
                    calculateBounceResponse(handled, ent, entities);
                }
                break;
            }
        }
    }

    private void calculateBounceResponse(Entity handled, Entity e2, Collection<Entity> entities)
    {
        Position handledPos = handled.get(Position.class);
        Position firstResponse = CollisionHandler.collisionResponse(handled, e2);
        handledPos.setX(firstResponse.getX());
        handledPos.setY(firstResponse.getY());

        for (Entity e3 : entities) // Checks if it collides with anything after the first correction.
        {
            if (e3.getState() == EntityState.DESTROYED)
            {
                continue;
            }
            CollisionData e3Data = e3.get(CollisionData.class);

            if (e3.getID() == handled.getID() || e3.getID() == e2.getID() || e3Data == null)
            {
                continue;
            }
            if (CollisionHandler.isColliding(handled, e3)) // If it collides with anything -> new response
            {
                boolean collidedWithThird = false;
                Position response = CollisionHandler.collisionResponse(handled, e3);
                handledPos.setX(response.getX());
                handledPos.setY(response.getY());
                break;
            }
        }
    }

    private Entity createBlood(Position pos)
    {
        Entity entity = new Entity();
        entity.setType(EntityType.BLOOD);
        entity.add(new Body(15, 15, Body.Geometry.RECTANGLE));
        entity.add(new LifeTime(5));
        entity.add(pos);
        entity.add(ViewManager.getView(CollisionPlugin.BLOOD_IMAGE_FILE_PATH));

        return entity;
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
