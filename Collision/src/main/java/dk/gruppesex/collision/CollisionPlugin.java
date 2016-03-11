/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.collision;

import dk.gruppesex.bodtrd.common.data.Entity;
import dk.gruppesex.bodtrd.common.data.GameData;
import dk.gruppesex.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppesex.bodtrd.common.services.GamePluginSPI;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Thanusaan
 */
public class CollisionPlugin implements GamePluginSPI
{
    private Map<Integer, Entity> _world;
    private GameData _gameData;

    @Override
    public void stop()
    {

    }

    @Override
    public void start(GameData gameData, Map<Integer, Entity> world, List<IEntityProcessor> processors)
    {
        Installer.Plugin = this;
        _gameData = gameData;
        this._world = world;
    }
}
