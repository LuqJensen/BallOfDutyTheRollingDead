package dk.gruppeseks.bodtrd.player;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityState;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body.Geometry;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.Health;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
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
    private World _world;
    private static final int DIAMETER = 50;

    @Override
    public void start(World world)
    {
        Installer.Plugin = this;

        ViewManager.createView(PLAYER_IMAGE_FILE_PATH, false);

        _world = world;

        _entity = createPlayerEntity();
        world.addEntity(_entity);

        _processor = new PlayerProcessor(_entity);
        _world.addProcessor(1, _processor);

        _world.getGameData().setPlayer(_entity);
    }

    @Override
    public void stop()
    {
        ViewManager.removeView(PLAYER_IMAGE_FILE_PATH);

        _world.removeProcessor(_processor);
        _world.removeEntity(_entity);
    }

    private Entity createPlayerEntity()
    {
        Entity entity = new Entity();
        entity.setType(EntityType.PLAYER);
        entity.setState(EntityState.ALIVE);

        Position newPosition = null;
        boolean[][] grid = _world.getMap().getGrid();
        int cellSize = _world.getMap().getGridCellSize();
        while (newPosition == null)
        {
            newPosition = new Position(DIAMETER * 2 + Math.random() * (_world.getMap().getWidth() - DIAMETER * 4), DIAMETER * 2 + Math.random() * (_world.getMap().getHeight() - DIAMETER * 4));
            int x = (int)newPosition.getX() / cellSize;
            int y = (int)newPosition.getY() / cellSize;
            if (x >= 0 && y >= 0 && x < grid.length && y < grid[0].length && !grid[x][y])
            {
                newPosition = null;
            }
        }
        entity.add(newPosition);
        entity.add(new Body(DIAMETER, DIAMETER, Geometry.CIRCLE));
        entity.add(new Velocity());
        entity.add(ViewManager.getView(PLAYER_IMAGE_FILE_PATH));
        entity.add(new Health(100, 1));

        return entity;
    }
}
