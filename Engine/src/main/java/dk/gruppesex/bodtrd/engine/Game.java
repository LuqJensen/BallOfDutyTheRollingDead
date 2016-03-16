/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.bodtrd.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dk.gruppesex.bodtrd.common.data.Entity;
import dk.gruppesex.bodtrd.common.data.GameData;
import dk.gruppesex.bodtrd.common.data.entityelements.Body;
import dk.gruppesex.bodtrd.common.data.entityelements.Position;
import dk.gruppesex.bodtrd.common.data.entityelements.View;
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
    private Map<Integer, Entity> _world;
    private List<GamePluginSPI> _gamePlugins;
    private SpriteBatch _batch;
    private AssetManager _assetManager;

    @Override
    public void create()
    {
        _world = new ConcurrentHashMap<>();
        _assetManager = new AssetManager();
        _batch = new SpriteBatch();
        _gameData.setDisplayWidth(Gdx.graphics.getWidth());
        _gameData.setDisplayHeight(Gdx.graphics.getHeight());
        _camera = new OrthographicCamera(_gameData.getDisplayWidth(), _gameData.getDisplayHeight());
        Lookup.Result<GamePluginSPI> result = _lookup.lookupResult(GamePluginSPI.class);
        result.addLookupListener(lookupListener);
        _gamePlugins = new ArrayList(result.allInstances());
        result.allItems();

        for (GamePluginSPI plugin : _gamePlugins)
        {
            plugin.start(_gameData, _world, _entityProcessors);
        }

        loadViews();

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
            loadViews();

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

        update();
        draw();

    }

    private void loadViews()
    {
        for (Entity e : _world.values())
        {
            View view = e.get(View.class);

            if (view == null)
            {
                continue;
            }
            String imagePath = view.getImageFilePath();

            if (!_assetManager.isLoaded(imagePath, Texture.class))
            {
                _assetManager.load(imagePath, Texture.class);
            }

        }
    }

    private void update()
    {
        for (IEntityProcessor entityProcessorService : _entityProcessors)
        {
            entityProcessorService.process(_gameData, _world);
        }
        _assetManager.update();
    }

    private void draw()
    {
        _camera.position.x = (float)(_gameData.getPlayerPosition().getX());
        _camera.position.y = (float)(_gameData.getPlayerPosition().getY());
        _camera.update();
        _batch.setProjectionMatrix(_camera.combined);
        _batch.begin();
        for (Entity e : _world.values())
        {

            View view = e.get(View.class);
            Body body = e.get(Body.class);
            Position pos = e.get(Position.class);

            if (body != null && pos != null && view != null)
            {
                if (_assetManager.isLoaded(view.getImageFilePath()))
                {
                    _batch.draw(_assetManager.get(view.getImageFilePath(), Texture.class), (float)pos.getX(), (float)pos.getY());
                }
            }
        }
        _batch.end();

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
