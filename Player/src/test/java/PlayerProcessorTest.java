/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.common.data.Action;
import dk.gruppeseks.bodtrd.common.data.ActionHandler;
import dk.gruppeseks.bodtrd.common.data.AudioManager;
import dk.gruppeseks.bodtrd.common.data.AudioType;
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.player.PlayerProcessor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Morten
 */
public class PlayerProcessorTest
{

    private IEntityProcessor _processor;
    private World _world;
    private Entity _player;
    private Velocity _velocity;
    private Weapon _wep;
    public static final String RELOAD_SOUND_FILE_PATH = "../../../Weapon/src/main/java/dk/gruppeseks/bodtrd/weapon/assets/reload.mp3";

    public PlayerProcessorTest()
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
        _player = new Entity();
        _velocity = new Velocity();
        _wep = new Weapon();
        _player.add(_wep);
        _player.add(_velocity);
        _world = new World(null);

        _processor = new PlayerProcessor(_player);
        //Necesssary to test weapon reload
        AudioManager.createSound(RELOAD_SOUND_FILE_PATH, AudioType.SOUND);
        _wep.setCurrentMagazineSize(5);
        _wep.setMaxMagazineSize(10);
        _wep.setReloadSpeed(10);
        _wep.setReloadSound(AudioManager.getAudio(RELOAD_SOUND_FILE_PATH));
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test the velocity of the player upon moving up
     */
    @Test
    public void testMoveUp()
    {
        _velocity = new Velocity();
        ActionHandler.setActive(Action.MOVE_DOWN, false);
        ActionHandler.setActive(Action.MOVE_LEFT, false);
        ActionHandler.setActive(Action.MOVE_RIGHT, false);
        ActionHandler.setActive(Action.MOVE_UP, true);
        _processor.process(_world);

        Vector2 expectedVec = new Vector2(0, 1);
        Vector2 actualVec = _player.get(Velocity.class).getVector().normalize();

        assertTrue(expectedVec.equals(actualVec));
    }

    /**
     * Test the velocity of the player upon moving down
     */
    @Test
    public void testMoveDown()
    {
        _velocity = new Velocity();
        ActionHandler.setActive(Action.MOVE_LEFT, false);
        ActionHandler.setActive(Action.MOVE_UP, false);
        ActionHandler.setActive(Action.MOVE_RIGHT, false);
        ActionHandler.setActive(Action.MOVE_DOWN, true);
        _processor.process(_world);

        Vector2 expectedVec = new Vector2(0, -1);
        Vector2 actualVec = _player.get(Velocity.class).getVector().normalize();

        assertTrue(expectedVec.equals(actualVec));
    }

    /**
     * Test the velocity of the player upon moving left
     */
    @Test
    public void testMoveLeft()
    {
        _velocity = new Velocity();
        ActionHandler.setActive(Action.MOVE_DOWN, false);
        ActionHandler.setActive(Action.MOVE_UP, false);
        ActionHandler.setActive(Action.MOVE_RIGHT, false);
        ActionHandler.setActive(Action.MOVE_LEFT, true);
        _processor.process(_world);

        Vector2 expectedVec = new Vector2(-1, 0);
        Vector2 actualVec = _player.get(Velocity.class).getVector().normalize();

        assertTrue(expectedVec.equals(actualVec));
    }

    /**
     * Test the velocity of the player upon moving right
     */
    @Test
    public void testMoveRight()
    {
        _velocity = new Velocity();
        ActionHandler.setActive(Action.MOVE_DOWN, false);
        ActionHandler.setActive(Action.MOVE_UP, false);
        ActionHandler.setActive(Action.MOVE_LEFT, false);
        ActionHandler.setActive(Action.MOVE_RIGHT, true);
        _processor.process(_world);

        Vector2 expectedVec = new Vector2(1, 0);
        Vector2 actualVec = _player.get(Velocity.class).getVector().normalize();

        assertTrue(expectedVec.equals(actualVec));
    }

    /**
     * Test that player's weapon is attacking when the action SHOOT is true
     */
    @Test
    public void testShoot()
    {
        ActionHandler.setActive(Action.SHOOT, true);
        _processor.process(_world);

        assertTrue(_wep.isAttacking());
    }

    /**
     * Test that weapon is reloading when the action RELOAD is true
     */
    @Test
    public void testReload()
    {
        ActionHandler.setActive(Action.RELOAD, true);
        _processor.process(_world);

        assertTrue(_wep.isReloading());
    }

}
