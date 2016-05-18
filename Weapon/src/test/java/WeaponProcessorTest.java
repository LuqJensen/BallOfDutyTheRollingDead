/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.common.data.AudioManager;
import dk.gruppeseks.bodtrd.common.data.AudioType;
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Damage;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import static dk.gruppeseks.bodtrd.weapon.WeaponPlugin.BULLET_IMAGE_FILE_PATH;
import static dk.gruppeseks.bodtrd.weapon.WeaponPlugin.NINE_MM_SOUND_FILE_PATH;
import dk.gruppeseks.bodtrd.weapon.WeaponProcessor;
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
public class WeaponProcessorTest
{

    private World _world;
    private GameData _gameData;
    private IEntityProcessor _processor;
    private Entity _player;

    public WeaponProcessorTest()
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
        _processor = new WeaponProcessor();
        _gameData = new GameData();
        _gameData.setDeltaTime(0.03f);
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that a bullet is present in the world after shooting
     */
    @Test
    public void testPlayerShoot()
    {
        _gameData.setMousePosition(20, 20);
        _world = new World(_gameData);
        _player = new Entity();

        Position pos = new Position(300, 300);
        Body bod = new Body(50, 50, Body.Geometry.CIRCLE);

        shoot(_player);

        _player.add(pos);
        _player.add(bod);
        _player.setType(EntityType.PLAYER);
        _world.addEntity(_player);
        ViewManager.createView(BULLET_IMAGE_FILE_PATH, false);
        AudioManager.createSound(NINE_MM_SOUND_FILE_PATH, AudioType.SOUND);

        _processor.process(_world);

        boolean containsBullet = false;
        for (Entity e : _world.entities())
        {
            if (e.getType() == EntityType.PROJECTILE)
            {
                containsBullet = true;
            }
        }
        assertTrue(containsBullet);
    }

    /**
     * Test that vector between player and bullet is the same as between player
     * and mouse position
     */
    @Test
    public void testAim()
    {
        //Setup
        _gameData.setMousePosition(100, 100);
        _world = new World(_gameData);
        _player = new Entity();
        Position playerPos = new Position(50, 50);
        Body bod = new Body(50, 50, Body.Geometry.CIRCLE);

        shoot(_player);

        _player.add(playerPos);
        _player.add(bod);
        _player.setType(EntityType.PLAYER);
        _world.addEntity(_player);
        //Necessary to shoot
        ViewManager.createView(BULLET_IMAGE_FILE_PATH, false);
        AudioManager.createSound(NINE_MM_SOUND_FILE_PATH, AudioType.SOUND);
        //Calculate expected vector
        Position mousePos = _gameData.getMousePosition();
        Position playerCenter = new Position(playerPos.getX() + bod.getWidth() / 2, playerPos.getY() + bod.getHeight() / 2);
        Vector2 expectedVec = new Vector2(playerCenter, mousePos).normalize();

        _processor.process(_world);
        Entity bullet = null;
        //Find bullet
        for (Entity e : _world.entities())
        {
            if (e.getType() == EntityType.PROJECTILE)
            {
                bullet = e;
            }
        }
        //Calculating actual vector
        Position bulletPos = bullet.get(Position.class);
        Vector2 actualVec = new Vector2(playerCenter, bulletPos).normalize();

        assertTrue(expectedVec.equals(actualVec));
    }

    /**
     * Helper method to set weapon dummy data and add the weapon to an entity
     *
     * @param ent The entity to be given a dummy weapon
     */
    private void shoot(Entity ent)
    {
        Weapon wep = new Weapon();
        wep.setReloading(false);
        wep.setAttackDamage(new Damage(20));
        wep.setCurrentMagazineSize(20);
        wep.setAttackSpeed(20);
        wep.setReloadTimeLeft(0);
        wep.setAttackCooldown(0);
        wep.setAttacking(true);

        ent.add(wep);
    }

}
