/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.collision.CollisionData;
import dk.gruppeseks.bodtrd.collision.CollisionPlugin;
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Morten
 */
public class CollisionPluginTest
{
    private GamePluginSPI _plugin;
    private World _world;
    private Entity _e1, _e2, _e3;

    public CollisionPluginTest()
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
        _plugin = new CollisionPlugin();
        _e1 = new Entity();
        _e1.setType(EntityType.PLAYER);
        _e2 = new Entity();
        _e2.setType(EntityType.WALL);
        _e3 = new Entity();
        _e3.setType(EntityType.PROJECTILE);
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that all entities in the world has collision data after the plugin
     * is started
     */
    @Test
    public void testStart()
    {
        startPlugin();

        boolean hasCollisionData = true;
        for (Entity e : _world.entities())
        {
            if (e.get(CollisionData.class) == null)
            {
                hasCollisionData = false;
            }
        }
        assertTrue(hasCollisionData);
    }

    /**
     * Test that no entities in the world has collision data after the plugin is
     * stopped
     */
    @Test
    public void testStop()
    {
        startPlugin();
        _plugin.stop();

        boolean hasCollisionData = false;
        for (Entity e : _world.entities())
        {
            if (e.get(CollisionData.class) != null)
            {
                hasCollisionData = true;
            }
        }
        assertFalse(hasCollisionData);
    }

    /**
     * Helper method to start the plugin
     */
    private void startPlugin()
    {
        _world = new World(null);
        _world.addEntity(_e1);
        _world.addEntity(_e2);
        _world.addEntity(_e3);
        _plugin.start(_world);
    }
}
