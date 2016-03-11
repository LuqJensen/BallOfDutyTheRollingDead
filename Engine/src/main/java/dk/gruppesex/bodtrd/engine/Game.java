/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
public class Game implements ApplicationListener
{
    private OrthographicCamera cam;
    private final Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private List<IEntityProcessor> entityProcessors = new ArrayList<>();
    private Map<Integer, Entity> world = new ConcurrentHashMap<>();
    private List<GamePluginSPI> gamePlugins;

    public void create()
    {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();
        Lookup.Result<GamePluginSPI> result = lookup.lookupResult(GamePluginSPI.class);
        result.addLookupListener(lookupListener);
        gamePlugins = new ArrayList(result.allInstances());
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

    @Override
    public void resize(int i, int i1)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDeltaTime(Gdx.graphics.getDeltaTime());
        for (IEntityProcessor entityProcessorService : entityProcessors)
        {
            entityProcessorService.process(gameData, world);
        }
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dispose()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
