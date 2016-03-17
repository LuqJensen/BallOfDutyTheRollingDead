/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.map;

import dk.gruppesex.bodtrd.common.data.Entity;
import dk.gruppesex.bodtrd.common.data.GameData;
import dk.gruppesex.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppesex.bodtrd.common.services.GamePluginSPI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author lucas
 */
@ServiceProvider(service = GamePluginSPI.class)
public class MapPlugin implements GamePluginSPI
{
    private Map<Integer, Entity> _world;
    private GameData _gameData;
    private List<Entity> _walls = new ArrayList<>();

    @Override
    public void stop()
    {
        for (Entity wall : _walls)
        {
            _world.remove(wall.getID(), wall);
        }
    }

    @Override
    public void start(GameData gameData, Map<Integer, Entity> world, List<IEntityProcessor> processors)
    {

        Installer.Plugin = this;
        _gameData = gameData;
        gameData.setMapWidth(4096);
        gameData.setMapHeight(4096);
        this._world = world;

        MapGenerator.generateMap(_walls, gameData);
        for (Entity wall : _walls)
        {
            world.put(wall.getID(), wall);
        }
    }
}
