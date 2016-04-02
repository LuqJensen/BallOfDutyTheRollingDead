/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.map;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author lucas
 */
@ServiceProvider(service = GamePluginSPI.class)
public class MapPlugin implements GamePluginSPI
{
    public final static String WALL_IMAGE_FILE_PATH = "../../../Map/src/main/java/dk/gruppeseks/bodtrd/map/assets/wall_box.png";
    public final static String BORDER_IMAGE_FILE_PATH = "../../../Map/src/main/java/dk/gruppeseks/bodtrd/map/assets/texture_border.png";
    private final static String DIRT_IMAGE_FILE_PATH = "../../../Map/src/main/java/dk/gruppeseks/bodtrd/map/assets/texture_dirt.png";

    private final static int MAP_WIDTH = 2000; // Be divisible by WALL_SIZE
    private final static int MAP_HEIGHT = 1000; // Be divisible by WALL_SIZE
    private final static int WALLS_PER_MEGA_PIXEL = 70;
    private final static int MEGA_PIXEL = 1000 * 1000;

    private final static int WALL_SIZE = 50; // Be divisible with MAP_WIDTH and MAP_HEIGHT

    private World _world;
    private List<Entity> _walls = new ArrayList<>();

    @Override
    public void stop()
    {
        ViewManager.removeView(WALL_IMAGE_FILE_PATH);
        ViewManager.removeView(DIRT_IMAGE_FILE_PATH);

        for (Entity wall : _walls)
        {
            _world.removeEntity(wall);
        }
    }

    @Override
    public void start(World world)
    {
        Installer.Plugin = this;

        ViewManager.createView(WALL_IMAGE_FILE_PATH, false);
        ViewManager.createView(BORDER_IMAGE_FILE_PATH, true);
        ViewManager.createView(DIRT_IMAGE_FILE_PATH, true);

        this._world = world;
        _world.getGameData().setMapWidth(MAP_WIDTH);
        _world.getGameData().setMapHeight(MAP_HEIGHT);
        _world.getGameData().setMapTextureView(ViewManager.getView(DIRT_IMAGE_FILE_PATH));

        int wallAmount = WALLS_PER_MEGA_PIXEL * MAP_HEIGHT * MAP_WIDTH / MEGA_PIXEL;
        MapGenerator.generateMap(_walls, MAP_WIDTH, MAP_HEIGHT, WALL_SIZE, wallAmount);
        for (Entity wall : _walls)
        {
            world.addEntity(wall);
        }
    }
}
