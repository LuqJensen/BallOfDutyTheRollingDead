/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;
import dk.gruppeseks.bodtrd.common.data.Audio;
import dk.gruppeseks.bodtrd.common.data.AudioAction;
import dk.gruppeseks.bodtrd.common.data.AudioManager;
import dk.gruppeseks.bodtrd.common.data.AudioType;
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.AIData;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.Health;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.entityelements.View;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import dk.gruppeseks.bodtrd.common.services.MapSPI;
import dk.gruppeseks.bodtrd.managers.AudioPlayer;
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
    private static final String BACKGROUND_MUSIC_FILE_PATH = "../../../Engine/src/main/java/dk/gruppeseks/bodtrd/assets/ambientmusic.mp3";
    private OrthographicCamera _gameCamera;
    private OrthographicCamera _hudCamera;
    private final Lookup _lookup = Lookup.getDefault();
    private World _world;
    private Set<GamePluginSPI> _gamePlugins = ConcurrentHashMap.newKeySet();
    private Lookup.Result<GamePluginSPI> _result;
    private Lookup.Result<MapSPI> _mapResult;
    private SpriteBatch _batch;
    private ShapeRenderer _shapeRenderer;
    private AssetManager _assetManager;
    private AudioPlayer _audioPlayer = new AudioPlayer();
    private Texture background;
    private BitmapFont _font;
    private MapSPI _map;

    private PolygonSpriteBatch _polyBatch;
    private Texture _textureSolid;
    private TextureRegion _textureRegion;
    private Pixmap _pix;

    @Override
    public void create()
    {

        _pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888); // Creates a pixel map with height and width of 1 pixel. RGBA8888 = 8 bit per color and alpha (32bit color system).
        _pix.setColor(1, 0.3f, 0.1f, 0.3f);  // Red Green Blue Alpha. 1,1,1,1 would be white. 0,0,0,1 would be black.
        _pix.fill();
        _textureSolid = new Texture(_pix); // A texture of one pixel (With a specific color)
        _textureRegion = new TextureRegion(_textureSolid); // A texture region keeps repeating a texture.
        _polyBatch = new PolygonSpriteBatch();
        _font = new BitmapFont();
        _shapeRenderer = new ShapeRenderer();
        _batch = new SpriteBatch();
        _assetManager = new AssetManager();

        GameData gameData = new GameData();
        _world = new World(gameData);

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        _gameCamera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        _hudCamera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        _hudCamera.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        _hudCamera.update();

        _mapResult = _lookup.lookupResult(MapSPI.class);
        _mapResult.addLookupListener(mapLookupListener);

        _map = _lookup.lookup(MapSPI.class);
        _map.generateMap(_world);

        Gdx.input.setInputProcessor(new GameInputManager());

        _result = _lookup.lookupResult(GamePluginSPI.class);
        _result.addLookupListener(lookupListener);

        _gamePlugins.addAll(_result.allInstances());

        for (GamePluginSPI plugin : _gamePlugins)
        {
            plugin.start(_world);
        }

        AudioManager.createSound(BACKGROUND_MUSIC_FILE_PATH, AudioType.MUSIC);

        loadViews();
        loadAudio();
        _assetManager.finishLoading();

        AudioManager.playSound(BACKGROUND_MUSIC_FILE_PATH, AudioAction.LOOP);
    }
    private final LookupListener mapLookupListener = new LookupListener()
    {
        @Override
        public void resultChanged(LookupEvent le)
        {
            MapSPI newMap = _lookup.lookup(MapSPI.class);

            if (newMap != null && _map != newMap)
            {
                _map = newMap;
                _map.generateMap(_world);
            }
        }
    };
    private final LookupListener lookupListener = new LookupListener()
    {
        @Override
        public void resultChanged(LookupEvent le)
        {
            Collection<? extends GamePluginSPI> updatedPlugins = _result.allInstances();
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
            loadAudio();
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
    }

    private void loadAudio()
    {
        for (Audio audio : AudioManager.audios())
        {
            String audioPath = audio.getFilePath();
            Class c = Music.class;

            if (audio.getType() == AudioType.SOUND)
            {
                c = Sound.class;
            }

            if (!_assetManager.isLoaded(audioPath, c))
            {
                _assetManager.load(audioPath, c);
            }
        }
    }

    private void tryInitiliazeBackground()
    {
        if (background != null)
        {
            return;
        }
        View backgroundTextureView = _world.getMap().getMapTextureView();
        if (backgroundTextureView != null)
        {
            if (_assetManager.isLoaded(backgroundTextureView.getImageFilePath()))
            {
                background = _assetManager.get(backgroundTextureView.getImageFilePath(), Texture.class);
                if (backgroundTextureView.isRepeat())
                {
                    background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                }
            }
        }
    }

    private void update()
    {
        _world.getGameData().setMousePosition(Gdx.input.getX() + (int)(_gameCamera.position.x - _gameCamera.viewportWidth / 2),
                -Gdx.input.getY() + Gdx.graphics.getHeight() + (int)(_gameCamera.position.y - _gameCamera.viewportHeight / 2));
        _world.update();
        _assetManager.update();
        _audioPlayer.handleAudioTasks(_assetManager);
    }

    private void draw()
    {
        Entity p = _world.getGameData().getPlayer();
        if (p != null)
        {
            Position pPosition = p.get(Position.class);
            Body pBody = p.get(Body.class);
            _gameCamera.position.x = (float)(pPosition.getX() + pBody.getWidth() / 2);
            _gameCamera.position.y = (float)(pPosition.getY() + pBody.getHeight() / 2);
        }

        _gameCamera.update();
        _polyBatch.setProjectionMatrix(_gameCamera.combined);
        _batch.setProjectionMatrix(_gameCamera.combined);
        _shapeRenderer.setProjectionMatrix(_gameCamera.combined);
        _batch.begin();
        tryInitiliazeBackground();
        if (background != null)
        {
            int backgroundWidth = background.getWidth();
            int backgroundHeight = background.getHeight();
            int backgroundRepeatWidth = _world.getMap().getWidth() / backgroundWidth;
            int backgroundRepeatHeight = _world.getMap().getHeight() / backgroundHeight;
            _batch.draw(background, 0, 0, backgroundWidth * backgroundRepeatWidth, backgroundHeight * backgroundRepeatHeight, 0, backgroundRepeatHeight, backgroundRepeatWidth, 0);
        }

        _batch.end();
        for (Entity e : _world.entities())
        {
            View view = e.get(View.class);
            Velocity vel = e.get(Velocity.class);
            Body body = e.get(Body.class);
            Position pos = e.get(Position.class);
            Health health = e.get(Health.class);

            if (body == null || pos == null || view == null)
            {
                continue;
            }
            if (_assetManager.isLoaded(view.getImageFilePath()))
            {
                _batch.begin();
                Texture texture = _assetManager.get(view.getImageFilePath(), Texture.class);
                if (view.isRepeat())
                {
                    texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

                    int textureWidth = texture.getWidth();
                    int textureHeight = texture.getHeight();
                    int textureRepeatWidth = body.getWidth() / textureWidth;
                    int textureRepeatHeight = body.getHeight() / textureWidth;

                    _batch.draw(texture, (float)pos.getX(), (float)pos.getY(), textureWidth * textureRepeatWidth, textureHeight * textureRepeatHeight, 0, textureRepeatHeight, textureRepeatWidth, 0);

                }
                else
                {
                    _batch.draw(texture, (float)pos.getX(), (float)pos.getY(), (float)body.getWidth(), (float)body.getHeight());
                }
                _batch.end();
                if (health != null)
                {
                    _batch.begin();
                    _shapeRenderer.begin(ShapeType.Filled);
                    double centerX = pos.getX() + (body.getWidth() / 2);
                    double upperY = pos.getY() + (body.getHeight());

                    float healthPerc = (float)health.getHp() / (float)health.getMaxHp() * 100;

                    _shapeRenderer.setColor(Color.RED);
                    _shapeRenderer.rect((float)centerX - 25, (float)upperY + 10, 50, 10);
                    _shapeRenderer.setColor(Color.GREEN);
                    _shapeRenderer.rect((float)centerX - 25, (float)upperY + 10, healthPerc / 2, 10);

                    _shapeRenderer.end();

                    _batch.end();
                }
            }
            AIData aiData = e.get(AIData.class);
            if (aiData != null)
            {
                if (aiData.getFoVShape() != null)
                {
                    drawFoV(aiData.getFoVShape());
                }
                if (aiData.getLatestKnownPosition() != null && vel.getVector().getMagnitude() > 0)
                {
                    drawLastKnown(aiData.getLatestKnownPosition());
                }

            }
        }
        if (p != null)
        {
            drawHUD(p);
        }
    }

    private void drawLastKnown(Position lastKnown)
    {
        _shapeRenderer.setProjectionMatrix(_gameCamera.combined);
        _shapeRenderer.begin(ShapeType.Filled);
        _shapeRenderer.setColor(Color.CYAN);
        _shapeRenderer.circle((float)lastKnown.getX(), (float)lastKnown.getY(), 10);
        _shapeRenderer.end();
    }

    private void drawFoV(float[] shape)
    {
        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(shape);
        PolygonRegion polyReg = new PolygonRegion(_textureRegion, shape, triangleIndices.toArray());
        PolygonSprite polySprite = new PolygonSprite(polyReg);

        _polyBatch.begin();
        polySprite.draw(_polyBatch);
        _polyBatch.end();

        _shapeRenderer.setProjectionMatrix(_gameCamera.combined);
        _shapeRenderer.begin(ShapeType.Line);
        _shapeRenderer.setColor(Color.BROWN);
        _shapeRenderer.polygon(shape);
        _shapeRenderer.end();

    }

    private void drawMouse()
    {
        _shapeRenderer.setProjectionMatrix(_gameCamera.combined);
        _shapeRenderer.begin(ShapeType.Filled);
        _shapeRenderer.setColor(1, 1, 0, 1);
        _shapeRenderer.circle((float)_world.getGameData().getMousePosition().getX(), (float)_world.getGameData().getMousePosition().getY(), 7);
        _shapeRenderer.end();
    }

    private void drawHUD(Entity p)
    {

        _batch.setProjectionMatrix(_hudCamera.combined);
        _batch.begin();
        Health pHealth = p.get(Health.class);
        Weapon pWeapon = p.get(Weapon.class);

        _font.draw(_batch, "fps: " + Gdx.graphics.getFramesPerSecond(), (float)(10), (float)(700)); // Need to create HUD
        if (pHealth != null)
        {
            _font.draw(_batch, "Hp: " + (int)pHealth.getHp() + "/" + (int)pHealth.getMaxHp(), (float)(10), (float)(680));
        }
        if (pWeapon != null)
        {
            _font.draw(_batch, "Ammo: " + pWeapon.getCurrentMagazineSize() + "/" + pWeapon.getCurrentAmmunition(), (float)(10), (float)(660));

        }
        _batch.end();
        _batch.setProjectionMatrix(_gameCamera.combined);
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
    }
}
