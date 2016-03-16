/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.movement;

import dk.gruppesex.bodtrd.common.data.Entity;
import dk.gruppesex.bodtrd.common.data.GameData;
import dk.gruppesex.bodtrd.common.data.entityelements.Position;
import dk.gruppesex.bodtrd.common.data.entityelements.Velocity;
import dk.gruppesex.bodtrd.common.interfaces.IEntityProcessor;
import java.util.Map;

/**
 *
 * @author Morten
 */
public class MovementProcessor implements IEntityProcessor
{
    @Override
    public void process(GameData gameData, Map<Integer, Entity> world)
    {
        double dt = gameData.getDeltaTime();
        for (Entity e : world.values())
        {
            if (e.get(Velocity.class) == null)
            {
                continue;
            }
            double dx = e.get(Velocity.class).getDx();
            double dy = e.get(Velocity.class).getDy();
            double posXOld = e.get(Position.class).getX();
            double posYOld = e.get(Position.class).getY();

            e.get(Position.class).setX(posXOld + (dx * dt));
            e.get(Position.class).setY(posYOld + (dy * dt));
        }
    }

}