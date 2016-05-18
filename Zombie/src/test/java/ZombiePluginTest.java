/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.Map;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import dk.gruppeseks.bodtrd.zombie.ZombiePlugin;
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
public class ZombiePluginTest
{

    private GamePluginSPI _plugin;
    private World _world;

    public ZombiePluginTest()
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
        _plugin = new ZombiePlugin();
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that there are zombies present after plugin is started
     */
    @Test
    public void testStart()
    {
        startPlugin();

        int zombieCount = 0;
        for (Entity e : _world.entities())
        {
            if (e.getType() == EntityType.ENEMY)
            {
                zombieCount++;
            }
        }
        assertTrue(zombieCount > 0);
    }

    /**
     * Test that no zombies are present in the world after plugin is soptted
     */
    @Test
    public void testStop()
    {
        startPlugin();

        _plugin.stop();
        int zombieCount = 0;
        for (Entity e : _world.entities())
        {
            if (e.getType() == EntityType.ENEMY)
            {
                zombieCount++;
            }
        }
        assertTrue(zombieCount == 0);
    }

    private void startPlugin()
    {
        _world = new World(null);
        //Set map with dummy data
        _world.setMap(new Map(800, 600, 50, null));
        _plugin.start(_world);
    }
}
