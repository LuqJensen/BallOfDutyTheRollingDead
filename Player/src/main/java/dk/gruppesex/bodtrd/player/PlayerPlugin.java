package dk.gruppesex.bodtrd.player;

import dk.gruppesex.bodtrd.common.data.Entity;
import dk.gruppesex.bodtrd.common.data.EntityType;
import dk.gruppesex.bodtrd.common.data.GameData;
import dk.gruppesex.bodtrd.common.data.entityelements.Body;
import dk.gruppesex.bodtrd.common.data.entityelements.Position;
import dk.gruppesex.bodtrd.common.data.entityelements.Velocity;
import dk.gruppesex.bodtrd.common.data.entityelements.View;
import dk.gruppesex.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppesex.bodtrd.common.services.GamePluginSPI;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author lucas
 */
@ServiceProvider(service = GamePluginSPI.class)
public class PlayerPlugin implements GamePluginSPI
{
    private Entity _entity;
    private IEntityProcessor _processor;
    private GameData _gameData;
    private Map<Integer, Entity> _world;
    private List<IEntityProcessor> _processors;

    @Override
    public void start(GameData gameData, Map<Integer, Entity> world, List<IEntityProcessor> processors)
    {
        Installer.Plugin = this;

        _world = world;
        _entity = createPlayerEntity();
        world.put(_entity.getID(), _entity);

        _processors = processors;
        _processor = new PlayerProcessor(_entity);
        processors.add(_processor);

        _gameData = gameData;
        gameData.setPlayerPosition(_entity.get(Position.class));
    }

    @Override
    public void stop()
    {
        _processors.remove(_processor);
        _world.remove(_entity.getID());
        _gameData.setPlayerPosition(null);
    }

    private Entity createPlayerEntity()
    {

        Entity entity = new Entity();
        entity.setType(EntityType.PLAYER);
        entity.add(new Position(200, 100));
        entity.add(new Body(50, 50));
        entity.add(new Velocity());

        entity.add(new View("../../../Player/src/main/java/dk/gruppesex/bodtrd/player/assets/ball_red.png"));
        return entity;

    }

}
