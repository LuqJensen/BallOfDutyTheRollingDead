/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.services.MapSPI;
import dk.gruppeseks.bodtrd.map.MapPlugin;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Morten
 */
public class MapPluginTest
{

    private MapSPI _plugin;
    private World _world;

    public MapPluginTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
        _plugin = new MapPlugin();
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that walls are present after generating a map
     */
    @Test
    public void testGenerateMap()
    {
        generateMap();

        int wallCount = 0;
        for (Entity e : _world.entities())
        {
            if (e.getType() == EntityType.WALL)
            {
                wallCount++;
            }
        }
        assertTrue(wallCount > 0);
    }

    /**
     * Test that no walls are present after unloading the map
     */
    @Test
    public void testUnloadMap()
    {
        generateMap();
        _plugin.unloadMap();

        int wallCount = 0;
        for (Entity e : _world.entities())
        {
            if (e.getType() == EntityType.WALL)
            {
                wallCount++;
            }
        }
        assertTrue(wallCount == 0);
    }

    /**
     * Helper method to generate map
     */
    private void generateMap()
    {
        _world = new World(null);
        _plugin.generateMap(_world);
    }

}
