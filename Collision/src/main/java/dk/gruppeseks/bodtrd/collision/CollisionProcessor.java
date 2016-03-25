/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.collision;

import dk.gruppeseks.bodtrd.common.data.Entity;
import static dk.gruppeseks.bodtrd.common.data.EntityType.PLAYER;
import static dk.gruppeseks.bodtrd.common.data.EntityType.PROJECTILE;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;

/**
 *
 * @author Chris
 */
public class CollisionProcessor implements IEntityProcessor
{
    @Override
    public void process(World world)
    {

        double dt = world.getGameData().getDeltaTime();

        for (Entity handled : world.entities())
        {
            Position handledPos = handled.get(Position.class);

            if (handledPos == null || handled.get(Velocity.class) == null || handled.get(Body.class) == null)
            {
                continue;
            }
            double startX = handledPos.getX();
            double startY = handledPos.getY();

//            // Temp test for collision
//            if (e.getType() == PLAYER)
//            {
//                for (Entity ent : world.values())
//                {
//                    if (ent.getType() == PLAYER)
//                    {
//                    }  //Don't collide with itself
//                    else if (CollisionHandler.isColliding(e, ent))
//                    {
//                        System.out.println("Collision true");
//                        p.setPosition(CollisionHandler.collisionResponse(e, ent));
//                        p.setX(CollisionHandler.collisionResponse(e, ent).getX());
//                        p.setY(CollisionHandler.collisionResponse(e, ent).getY());
//                    }
//                }
//            }
//            // Temp IMPROVED test for collision, ported over from the old system. ISN'T WORKING
            if (handled.getType() == PLAYER)
            {

//                temp.getBody().increasePosition(velocity.getX() * secondsSinceLastUpdate,
//                        velocity.getY() * secondsSinceLastUpdate);
                boolean collision = false;

                for (Entity ent : world.entities())
                {

                    if (ent.getID() == handled.getID() || ent.getType() == PROJECTILE)
                    {
                        continue;
                    }
                    if (CollisionHandler.isColliding(handled, ent))
                    {
                        Position firstPos = CollisionHandler.collisionResponse(handled, ent);
                        handledPos.setX(firstPos.getX());
                        handledPos.setY(firstPos.getY());

                        for (Entity ent2 : world.entities()) // Checks if it collides with anything.
                        {
                            if (ent2.getID() == handled.getID() || ent2.getID() == ent.getID() || ent2.getType() == PROJECTILE)
                            {
                                continue;
                            }
                            if (CollisionHandler.isColliding(handled, ent2))
                            {
                                for (Entity ent3 : world.entities())
                                {
                                    if (ent3.getID() == handled.getID() || ent3.getID() == ent.getID()
                                            || ent3.getID() == ent2.getID() || ent3.getType() == PROJECTILE)
                                    {
                                        continue;
                                    }
                                    if (CollisionHandler.isColliding(handled, ent3))
                                    {
                                        handledPos.setX(startX);
                                        handledPos.setY(startY); // If it even collides after a third collision correction, then dont do any corrections at all.
                                        collision = true;
                                        break;
                                    }

                                }
                                if (!collision)
                                {
                                    // if it doesnt collide with a third object after the second collision correction, put it to the secondcalculated position.
                                    Position response = CollisionHandler.collisionResponse(handled, ent2);
                                    handledPos.setX(response.getX());
                                    handledPos.setY(response.getY());
                                }
                                collision = true;
                                break;
                            }

                        }
                        if (!collision) // if it doesnt collide with a second object, put it to the first calculated position.
                        {
                            handledPos.setX(firstPos.getX());
                            handledPos.setY(firstPos.getY());
                        }
                        collision = true; // with the collided object anymore.
                        break;
                    }
                }
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

}
