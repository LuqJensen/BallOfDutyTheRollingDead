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
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import java.util.Collection;
import java.util.Set;
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
    private World _world;
    private Set<GamePluginSPI> _gamePlugins;
    private SpriteBatch _batch;
    private ShapeRenderer _shapeRenderer;
    private AssetManager _assetManager;
    private Texture background;
    private BitmapFont _font;

    @Override
    public void create()
    {
        _font = new BitmapFont();
        _shapeRenderer = new ShapeRenderer();
        _batch = new SpriteBatch();
        _assetManager = new AssetManager();

        GameData gameData = new GameData();
        _world = new World(gameData);

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        _camera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());

        Gdx.input.setInputProcessor(new GameInputManager());

        Lookup.Result<GamePluginSPI> result = _lookup.lookupResult(GamePluginSPI.class);
        result.addLookupListener(lookupListener);
        _gamePlugins = ConcurrentHashMap.newKeySet();
        _gamePlugins.addAll(result.allInstances());
        result.allItems();

        for (GamePluginSPI plugin : _gamePlugins)
        {
            plugin.start(_world);
        }

        loadViews();
        loadBackground();
    }

    private final LookupListener lookupListener = new LookupListener()
    {
        @Override
        public void resultChanged(LookupEvent le)
        {
            Collection<GamePluginSPI> updatedPlugins = (Collection<GamePluginSPI>)_lookup.lookupAll(GamePluginSPI.class);
            for (GamePluginSPI updatedPlugin : updatedPlugins)
            {
                if (!_gamePlugins.contains(updatedPlugin))
                {
                    updatedPlugin.start(_world);
                    _gamePlugins.add(updatedPlugin);
                }
            }

            for (GamePluginSPI oldPlugin : _gamePlugins)
            {
                if (!updatedPlugins.contains(oldPlugin))
                {
                    _gamePlugins.remove(oldPlugin);
                }
            }

            loadViews();
            loadBackground();
        }
    };

    @Override
    public void resize(int i, int i1)
    {
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, (float)0.6, (float)0.2, 1);
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
        _assetManager.finishLoading();
    }

    private void loadBackground()
    {
        View backgroundTextureView = _world.getGameData().getMapTextureView();
        if (backgroundTextureView != null)
        {
            if (_assetManager.isLoaded(backgroundTextureView.getImageFilePath()))
            {
                background = _assetManager.get(backgroundTextureView.getImageFilePath(), Texture.class);
                background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            }
        }
    }

    private void update()
    {
        _world.getGameData().setMousePosition(Gdx.input.getX() + (int)(_camera.position.x - _camera.viewportWidth / 2),
                -Gdx.input.getY() + Gdx.graphics.getHeight() + (int)(_camera.position.y - _camera.viewportHeight / 2));
        _world.update();
        _assetManager.update();
    }

    private void draw()
    {
        Position pPosition = _world.getGameData().getPlayerPosition();
        Body pBody = _world.getGameData().getPlayerBody();
        _camera.position.x = (float)(pPosition.getX() + pBody.getWidth() / 2);
        _camera.position.y = (float)(pPosition.getY() + pBody.getHeight() / 2);

        _camera.update();
        _batch.setProjectionMatrix(_camera.combined);
        _batch.begin();

        if (background != null)
        {
            int mapWidth = _world.getGameData().getMapWidth();
            int mapHeight = _world.getGameData().getMapHeight();

            _batch.draw(background, 0, 0, background.getWidth() * (mapWidth / background.getWidth()), background.getHeight() * (mapHeight / background.getHeight()), 0, mapHeight / background.getHeight(), mapWidth / background.getWidth(), 0);
        }

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
                _batch.draw(_assetManager.get(view.getImageFilePath(), Texture.class), (float)pos.getX(), (float)pos.getY(), (float)body.getWidth(), (float)body.getHeight());
            }
        }

        //Debug rendering
//        drawMouse();
        drawFps();
        _batch.end();
    }

    private void drawMouse()
    {
        _shapeRenderer.setProjectionMatrix(_camera.combined);
        _shapeRenderer.begin(ShapeType.Filled);
        _shapeRenderer.setColor(1, 1, 0, 1);
        _shapeRenderer.circle((float)_world.getGameData().getMousePosition().getX(), (float)_world.getGameData().getMousePosition().getY(), 7);
        _shapeRenderer.end();
    }

    private void drawFps()
    {
        _font.draw(_batch, "fps: " + Gdx.graphics.getFramesPerSecond(), 10, 10); // Current anchored to the world.

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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
