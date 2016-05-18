/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dk.gruppeseks.bodtrd.collision.CollisionData;
import dk.gruppeseks.bodtrd.collision.CollisionFlags;
import dk.gruppeseks.bodtrd.collision.CollisionHandler;
import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
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
public class CollisionTest
{

    Entity e1, e2;
    Position p1, p2;
    Body b1, b2;
    CollisionData cd1, cd2;

    public CollisionTest()
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
        //Setyp entities
        b1 = new Body(10, 10, Body.Geometry.CIRCLE);
        b2 = new Body(10, 10, Body.Geometry.CIRCLE);
        p1 = new Position(10, 10);
        p2 = new Position(15, 15);
        cd1 = new CollisionData();
        cd1.setCollisionFlag(CollisionFlags.CONCRETE);
        cd2 = new CollisionData();
        cd2.setCollisionFlag(CollisionFlags.CONCRETE);
        //Initialize entities and add elements
        e1 = new Entity();
        e1.add(p1);
        e1.add(b1);
        e1.add(cd1);
        e2 = new Entity();
        e2.add(p2);
        e2.add(b2);
        e2.add(cd2);

    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test collision between a circle and a rectangle
     *
     * Expected result: true
     */
    @Test
    public void testCircleRectangleCollisionTrue()
    {
        b1.setGeometry(Body.Geometry.CIRCLE);
        b2.setGeometry(Body.Geometry.RECTANGLE);

        p1.setX(10);
        p1.setY(10);
        p2.setX(15);
        p2.setX(15);

        assertTrue(CollisionHandler.isColliding(e1, e2));
    }

    /**
     * Test collision between two circles
     *
     * Expected result: true
     */
    @Test
    public void testCircleCircleCollisionTrue()
    {
        b1.setGeometry(Body.Geometry.CIRCLE);
        b2.setGeometry(Body.Geometry.CIRCLE);

        p1.setX(10);
        p1.setY(10);
        p2.setX(15);
        p2.setY(15);

        assertTrue(CollisionHandler.isColliding(e1, e2));
    }

    /**
     * Test collision between two rectangles
     *
     * Expected result: true
     */
    @Test
    public void testRectangleRectangleCollisionTrue()
    {
        b1.setGeometry(Body.Geometry.RECTANGLE);
        b2.setGeometry(Body.Geometry.RECTANGLE);

        p1.setX(10);
        p1.setY(10);
        p2.setX(15);
        p2.setY(15);

        assertTrue(CollisionHandler.isColliding(e1, e2));
    }

    /**
     * Test collision between a circle and a rectangle
     *
     * Expected result: false
     */
    @Test
    public void testCircleRectangleCollisionFalse()
    {
        b1.setGeometry(Body.Geometry.CIRCLE);
        b2.setGeometry(Body.Geometry.RECTANGLE);

        p1.setX(10);
        p1.setY(30);
        p2.setX(21);
        p2.setY(30);

        assertFalse(CollisionHandler.isColliding(e1, e2));
    }

    /**
     * Test collision between two circles
     *
     * Expected result: false
     */
    @Test
    public void testCircleCircleCollisionFalse()
    {
        b1.setGeometry(Body.Geometry.CIRCLE);
        b2.setGeometry(Body.Geometry.CIRCLE);

        p1.setX(10);
        p1.setY(10);
        p2.setX(21);
        p2.setY(10);

        assertFalse(CollisionHandler.isColliding(e1, e2));
    }

    /**
     * Test collision between two rectangles
     *
     * Expected result: false
     */
    @Test
    public void testRectangleRectangleCollisionFalse()
    {
        b1.setGeometry(Body.Geometry.RECTANGLE);
        b2.setGeometry(Body.Geometry.RECTANGLE);

        p1.setX(10);
        p1.setY(10);
        p2.setX(50);
        p2.setY(50);

        assertFalse(CollisionHandler.isColliding(e1, e2));
    }

}
