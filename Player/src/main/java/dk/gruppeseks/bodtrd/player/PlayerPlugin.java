package dk.gruppeseks.bodtrd.player;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.entityelements.View;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
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
    private static final String PLAYER_IMAGE_FILE_PATH = "../../../Player/src/main/java/dk/gruppeseks/bodtrd/player/assets/ball_red.png";
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
        Body body = _entity.get(Body.class);
        Position pos = _entity.get(Position.class);
        _gameData.setPlayerPosition(pos);
        _gameData.setPlayerBody(body);
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
        entity.add(new View(PLAYER_IMAGE_FILE_PATH));

        return entity;
    }
}
