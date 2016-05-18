/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.movement.MovementProcessor;
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
public class MovementTest
{

    private World _world;
    private GameData _gameData;
    private IEntityProcessor _processor;

    public MovementTest()
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
        _gameData.setDeltaTime(0.03f);
        _world = new World(_gameData);
        _processor = new MovementProcessor();
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test that an entity actually moves upon being processed by movement
     * processor
     */
    @Test
    public void testEntityMove()
    {
        Entity player = new Entity();
        Velocity v = new Velocity(20, 20);
        Position p = new Position(300, 300);

        player.add(v);
        player.add(p);
        player.setType(EntityType.PLAYER);
        _world.addEntity(player);

        Vector2 vec = v.getVector();
        float expectedX = (float)(p.getX() + (vec.getX() * _gameData.getDeltaTime()));
        float expectedY = (float)(p.getY() + (vec.getY() * _gameData.getDeltaTime()));

        _processor.process(_world);
        float actualX = (float)player.get(Position.class).getX();
        float actualY = (float)player.get(Position.class).getY();

        assertTrue(expectedX == actualX && expectedY == actualY);
    }

}
