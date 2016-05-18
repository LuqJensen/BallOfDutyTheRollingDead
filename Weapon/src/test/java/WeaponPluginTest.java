/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import dk.gruppeseks.bodtrd.weapon.WeaponPlugin;
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
public class WeaponPluginTest
{

    private GamePluginSPI _plugin;
    private Entity _player;
    private World _world;

    public WeaponPluginTest()
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
        _plugin = new WeaponPlugin();
        _player = new Entity();
        _player.setType(EntityType.PLAYER);
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that one or more weapons are present after the plugin is started
     */
    @Test
    public void testStart()
    {
        startPlugin();

        boolean containsWeapon = false;
        for (Entity e : _world.entities())
        {
            if (e.get(Weapon.class) != null)
            {
                containsWeapon = true;
            }
        }
        assertTrue(containsWeapon);
    }

    /**
     * Test that no weapons are present after the plugin is stopped
     */
    @Test
    public void testStop()
    {
        startPlugin();
        _plugin.stop();

        boolean containsWeapon = false;
        for (Entity e : _world.entities())
        {
            if (e.get(Weapon.class) != null)
            {
                containsWeapon = true;
            }
        }
        assertFalse(containsWeapon);
    }

    /**
     * Helper method to start the plugin being tested
     */
    private void startPlugin()
    {
        _world = new World(null);
        _world.addEntity(_player);
        _plugin.start(_world);
    }
}
