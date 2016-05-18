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
import dk.gruppeseks.bodtrd.common.data.entityelements.LifeTime;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.lifestate.LifeStateProcessor;
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
public class LifeStateProcessorTest
{

    private IEntityProcessor _processor;
    private World _world;
    private Entity _e1;
    private GameData _gameData;

    public LifeStateProcessorTest()
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
        _processor = new LifeStateProcessor();
        _e1 = new Entity();
        _e1.setType(EntityType.PLAYER);
        _gameData = new GameData();
        _gameData.setDeltaTime(0.03f);
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that an entity with state DESTROYED is removed fromt the world
     */
    @Test
    public void testDestroyed()
    {
        _world = new World(_gameData);
        _e1.setState(EntityState.DESTROYED);
        _world.addEntity(_e1);
        _processor.process(_world);

        boolean containsEntity = false;
        for (Entity e : _world.entities())
        {
            if (e.equals(_e1))
            {
                containsEntity = true;
            }
        }
        assertFalse(containsEntity);
    }

    /**
     * Test that an entity with state alive and lifestame below zero IS removed
     */
    @Test
    public void testLifeTimeBelowZero()
    {
        _world = new World(_gameData);
        LifeTime lifeTime = new LifeTime(0);
        _e1.add(lifeTime);
        _e1.setType(EntityType.PROJECTILE);
        _e1.setState(EntityState.ALIVE);

        _world.addEntity(_e1);
        _processor.process(_world);

        boolean containsEntity = false;
        for (Entity e : _world.entities())
        {
            if (e.equals(_e1))
            {
                containsEntity = true;
            }
        }
        assertFalse(containsEntity);
    }

    /**
     * Test that an entity with state alive and lifetime above zero is NOT
     * removed
     */
    @Test
    public void testLifeTimeAboveZero()
    {
        _world = new World(_gameData);
        LifeTime lifeTime = new LifeTime(10);
        _e1.add(lifeTime);
        _e1.setType(EntityType.PROJECTILE);
        _e1.setState(EntityState.ALIVE);

        _world.addEntity(_e1);
        _processor.process(_world);

        boolean containsEntity = false;
        for (Entity e : _world.entities())
        {
            if (e.equals(_e1))
            {
                containsEntity = true;
            }
        }
        assertTrue(containsEntity);
    }
}
