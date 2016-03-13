/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.engine;

import dk.gruppesex.bodtrd.common.data.Entity;
import dk.gruppesex.bodtrd.common.data.GameData;
import dk.gruppesex.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppesex.bodtrd.common.services.GamePluginSPI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author lucas
 */
public class Game
{
    private final Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private List<IEntityProcessor> entityProcessors = new ArrayList<>();
    private Map<Integer, Entity> world = new ConcurrentHashMap<>();
    private List<GamePluginSPI> gamePlugins;

    public void create()
    {
        Lookup.Result<GamePluginSPI> result = lookup.lookupResult(GamePluginSPI.class);
        result.addLookupListener(lookupListener);
        gamePlugins = new ArrayList<>(result.allInstances());
        result.allItems();

        for (GamePluginSPI plugin : gamePlugins)
        {
            plugin.start(gameData, world, entityProcessors);
        }
    }

    private final LookupListener lookupListener = new LookupListener()
    {
        @Override
        public void resultChanged(LookupEvent le)
        {
            for (GamePluginSPI updatedGamePlugin : lookup.lookupAll(GamePluginSPI.class))
            {
                if (!gamePlugins.contains(updatedGamePlugin))
                {
                    updatedGamePlugin.start(gameData, world, entityProcessors);
                    gamePlugins.add(updatedGamePlugin);
                }
            }
        }
    };
}
