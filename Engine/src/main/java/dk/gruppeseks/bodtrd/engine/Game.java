/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.View;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import dk.gruppeseks.bodtrd.managers.GameInputManager;
import java.util.ArrayList;
import java.util.List;
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
    private World _world;
    private List<GamePluginSPI> _gamePlugins;
    private SpriteBatch _batch;
    private ShapeRenderer _shapeRenderer;
    private AssetManager _assetManager;
    private GameData _gameData;

    @Override
    public void create()
    {
        _shapeRenderer = new ShapeRenderer();
        _batch = new SpriteBatch();
        _assetManager = new AssetManager();

        _gameData = new GameData();
        _world = new World(_gameData);

        _gameData.setDisplayWidth(Gdx.graphics.getWidth());
        _gameData.setDisplayHeight(Gdx.graphics.getHeight());
        _camera = new OrthographicCamera(_gameData.getDisplayWidth(), _gameData.getDisplayHeight());

        Gdx.input.setInputProcessor(new GameInputManager());

        Lookup.Result<GamePluginSPI> result = _lookup.lookupResult(GamePluginSPI.class);
        result.addLookupListener(lookupListener);
        _gamePlugins = new ArrayList(result.allInstances());
        result.allItems();

        for (GamePluginSPI plugin : _gamePlugins)
        {
            plugin.start(_world);
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
                    updatedGamePlugin.start(_world);
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

        _world.getGameData().setDeltaTime(Gdx.graphics.getDeltaTime());

        update();
        draw();
    }

    private void loadViews()
    {
        for (View view : ViewManager.views())
        {
            String imagePath = view.getImageFilePath();

            if (!_assetManager.isLoaded(imagePath, Texture.class))
            {
                _assetManager.load(imagePath, Texture.class);
            }
        }
    }

    private void update()
    {
        _gameData.setMousePosition(Gdx.input.getX() + (int)(_camera.position.x - _camera.viewportWidth / 2), -Gdx.input.getY() + Gdx.graphics.getHeight() + (int)(_camera.position.y - _camera.viewportHeight / 2));
        _world.update();
        _assetManager.update();
    }

    private void draw()
    {
        _camera.position.x = (float)(_world.getGameData().getPlayerPosition().getX());
        _camera.position.y = (float)(_world.getGameData().getPlayerPosition().getY());
        _camera.update();
        _batch.setProjectionMatrix(_camera.combined);

        _batch.begin();
        for (Entity e : _world.entities())
        {
            View view = e.get(View.class);
            Body body = e.get(Body.class);
            Position pos = e.get(Position.class);

            if (body == null || pos == null || view == null)
            {
                continue;
            }

            if (_assetManager.isLoaded(view.getImageFilePath()))
            {
                _batch.draw(_assetManager.get(view.getImageFilePath(), Texture.class), (float)pos.getX(), (float)pos.getY());
            }
        }
        _batch.end();

        //Debug rendering
        drawMouse();
    }

    private void drawMouse()
    {
        _shapeRenderer.setProjectionMatrix(_camera.combined);
        _shapeRenderer.begin(ShapeType.Filled);
        _shapeRenderer.setColor(1, 1, 0, 1);
        _shapeRenderer.circle((float)_gameData.getMousePosition().getX(), (float)_gameData.getMousePosition().getY(), 10);
        _shapeRenderer.end();
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
