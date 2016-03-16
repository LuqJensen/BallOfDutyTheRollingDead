package dk.gruppesex.bodtrd.player;

import dk.gruppesex.bodtrd.common.data.Action;
import dk.gruppesex.bodtrd.common.data.ActionHandler;
import dk.gruppesex.bodtrd.common.data.Entity;
import dk.gruppesex.bodtrd.common.data.GameData;
import dk.gruppesex.bodtrd.common.data.entityelements.Position;
import dk.gruppesex.bodtrd.common.data.entityelements.Velocity;
import dk.gruppesex.bodtrd.common.interfaces.IEntityProcessor;
import java.util.Map;

/**
 *
 * @author lucas
 */
public class PlayerProcessor implements IEntityProcessor
{
    private Entity _player;

    public PlayerProcessor(Entity player)
    {
        _player = player;
    }

    @Override
    public void process(GameData gameData, Map<Integer, Entity> world)
    {
        Position pos = _player.get(Position.class);
        Velocity vel = _player.get(Velocity.class);

        if (ActionHandler.isActive(Action.MOVE_DOWN))
        {
            vel.setDx(0);
            vel.setDy(-1);
        }
        if (ActionHandler.isActive(Action.MOVE_UP))
        {
            vel.setDx(0);
            vel.setDy(1);
        }
        if (ActionHandler.isActive(Action.MOVE_RIGHT))
        {
            vel.setDx(1);
            vel.setDy(0);
        }
        if (ActionHandler.isActive(Action.MOVE_LEFT))
        {
            vel.setDx(-1);
            vel.setDy(0);
        }
    }

}
