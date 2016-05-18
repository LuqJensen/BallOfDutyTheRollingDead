/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import dk.gruppeseks.bodtrd.player.PlayerPlugin;
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
public class PlayerPluginTest
{

    private World _world;
    private GameData _gameData;
    private GamePluginSPI _plugin;

    public PlayerPluginTest()
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
        _gameData = new GameData();
        _plugin = new PlayerPlugin();
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that a player is present in the world upon plugin start
     */
    @Test
    public void testStart()
    {
        startPlugin();

        boolean containsPlayer = false;
        for (Entity e : _world.entities())
        {
            if (e.getType() == EntityType.PLAYER)
            {
                containsPlayer = true;
            }
        }
        assertTrue(containsPlayer);
    }

    /**
     * Test that there is no player present in the world upothe n plugin stop
     */
    @Test
    public void testStop()
    {
        startPlugin();
        _plugin.stop();

        boolean containsPlayer = false;
        for (Entity e : _world.entities())
        {
            if (e.getType() == EntityType.PLAYER)
            {
                containsPlayer = true;
            }
        }
        assertFalse(containsPlayer);
    }

    private void startPlugin()
    {
        _world = new World(_gameData);
        _plugin.start(_world);
    }
}
