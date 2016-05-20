package dk.gruppeseks.bodtrd.player;

import dk.gruppeseks.bodtrd.common.data.Action;
import dk.gruppeseks.bodtrd.common.data.ActionHandler;
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;

/**
 *
 * @author lucas
 */
public class PlayerProcessor implements IEntityProcessor
{
    private Entity _player;
    private int _movementSpeed = 250;

    public PlayerProcessor(Entity player)
    {
        _player = player;
    }

    @Override
    public void process(World world)
    {
        Vector2 vel = _player.get(Velocity.class).getVector();
        // Reset velocity direction, in case direction keys are no longer pressed.
        vel.setX(0);
        vel.setY(0);

        if (ActionHandler.isActive(Action.MOVE_DOWN))
        {
            vel.addVector(0, -1);
        }
        if (ActionHandler.isActive(Action.MOVE_UP))
        {
            vel.addVector(0, 1);
        }
        if (ActionHandler.isActive(Action.MOVE_RIGHT))
        {
            vel.addVector(1, 0);
        }
        if (ActionHandler.isActive(Action.MOVE_LEFT))
        {
            vel.addVector(-1, 0);
        }
        vel.setMagnitude(_movementSpeed);

        Weapon wep = _player.get(Weapon.class);
        if (wep == null)
        {
            return;
        }

        wep.setAttacking(ActionHandler.isActive(Action.SHOOT));

        if (ActionHandler.isActive(Action.RELOAD))
        {
            wep.setReloading(true);
        }

        Position p = _player.get(Position.class);
        Body b = _player.get(Body.class);
        Position center = new Position(p.getX() + b.getWidth() / 2, p.getY() + b.getHeight() / 2);
        Position mousePos = world.getGameData().getMousePosition();
        Vector2 orientation = new Vector2(mousePos.getX() - center.getX(), mousePos.getY() - center.getY()).setMagnitude(b.getWidth() / 2);

        b.setOrientation(orientation);
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
