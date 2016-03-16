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
    private OrthographicCamera _camera;
    private final Lookup _lookup = Lookup.getDefault();
    private final GameData _gameData = new GameData();
    private List<IEntityProcessor> _entityProcessors = new ArrayList<>();
    private Map<Integer, Entity> _world = new ConcurrentHashMap<>();
    private List<GamePluginSPI> _gamePlugins;

    @Override
    public void create()
    {
        _gameData.setDisplayWidth(Gdx.graphics.getWidth());
        _gameData.setDisplayHeight(Gdx.graphics.getHeight());
        _camera = new OrthographicCamera(_gameData.getDisplayWidth(), _gameData.getDisplayHeight());
        _camera.translate(_gameData.getDisplayWidth() / 2, _gameData.getDisplayHeight() / 2);
        _camera.update();
        Lookup.Result<GamePluginSPI> result = _lookup.lookupResult(GamePluginSPI.class);
        result.addLookupListener(lookupListener);
        _gamePlugins = new ArrayList(result.allInstances());
        result.allItems();

        for (GamePluginSPI plugin : _gamePlugins)
        {
            plugin.start(_gameData, _world, _entityProcessors);
        }
    }

    private final LookupListener lookupListener = new LookupListener()
    {
        @Override
        public void resultChanged(LookupEvent le)
        {
            for (GamePluginSPI updatedGamePlugin : _lookup.lookupAll(GamePluginSPI.class))
            {
                if (!_gamePlugins.contains(updatedGamePlugin))
                {
                    updatedGamePlugin.start(_gameData, _world, _entityProcessors);
                    _gamePlugins.add(updatedGamePlugin);
                }
            }
        }
    };

    @Override
    public void resize(int i, int i1)
    {
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _gameData.setDeltaTime(Gdx.graphics.getDeltaTime());
        for (IEntityProcessor entityProcessorService : _entityProcessors)
        {
            entityProcessorService.process(_gameData, _world);
        }

        _camera.position.x = (float)_gameData.getPlayerPosition().getX();
        _camera.position.y = (float)_gameData.getPlayerPosition().getY();
        _camera.update();
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {
    }

    @Override
    public void dispose()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
