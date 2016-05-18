/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityState;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Damage;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.DamageInstance;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.Health;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.health.HealthProcessor;
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
public class HealthProcessorTest
{

    IEntityProcessor _processor;
    GameData _gameData;
    World _world;
    Entity _e1, _e2;
    Health _h1;
    DamageInstance _di1;

    public HealthProcessorTest()
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
        _e1 = new Entity();
        _e2 = new Entity();

        _gameData = new GameData();
        _gameData.setDeltaTime(0.03f);
        _processor = new HealthProcessor();
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that health declines when player is hit
     */
    @Test
    public void testPlayerTakeDamage()
    {
        _world = new World(_gameData);
        _e1.setType(EntityType.PLAYER);
        _world.addEntity(_e1);
        _h1 = new Health(100, 10);
        _e1.add(_h1);

        int originalHp = 10;
        _h1.setHp(originalHp);
        _di1 = new DamageInstance(new Damage(5), _e2.getID());
        _h1.addDamageInstance(_di1);

        _processor.process(_world);
        assertTrue(_e1.get(Health.class).getHp() < originalHp);
    }

    /**
     * Test that player is destroyed when hp reaches 0
     */
    @Test
    public void testPlayerDestroyed()
    {
        _world = new World(_gameData);
        _e1.setType(EntityType.PLAYER);
        _world.addEntity(_e1);
        _h1 = new Health(100, 10);
        _e1.add(_h1);

        _h1.setHp(100);
        _di1 = new DamageInstance(new Damage(110), _e2.getID());
        _h1.addDamageInstance(_di1);

        _processor.process(_world);
        assertTrue(_e1.getState() == EntityState.DESTROYED);
    }
}
