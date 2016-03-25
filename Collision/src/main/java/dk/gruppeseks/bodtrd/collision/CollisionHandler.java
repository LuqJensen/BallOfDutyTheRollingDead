/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.collision;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import static dk.gruppeseks.bodtrd.common.data.entityelements.Body.Geometry.CIRCLE;
import static dk.gruppeseks.bodtrd.common.data.entityelements.Body.Geometry.RECTANGLE;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;

/**
 *
 * @author Chris & Morten
 */
public class CollisionHandler
{

    /**
     * Check if 2 game objects are colliding.
     *
     * @param e1
     * game object 1.
     * @param e2
     * game object 2.
     * @return Returns true if the objects are colliding.
     */
    public static boolean isColliding(Entity e1, Entity e2)
    {
        CollisionDAO o1 = new CollisionDAO(e1);
        CollisionDAO o2 = new CollisionDAO(e2);

        boolean retval = false;
        if (o1.geometry == CIRCLE && o2.geometry == CIRCLE)
        {
            retval = collisionCircleCircle(o1, o2);
        }
        else if (o1.geometry == CIRCLE && o2.geometry == RECTANGLE)
        {
            retval = collisionCircleRectangle(o1, o2);;
        }
        else if (o1.geometry == RECTANGLE && o2.geometry == CIRCLE)
        {
            retval = collisionCircleRectangle(o1, o2);;
        }
        else if (o1.geometry == RECTANGLE && o2.geometry == RECTANGLE)
        {
            retval = collisionRectangleRectangle(o1, o2);;
        }
        return retval;
    }

    /**
     * Checks if 2 circles are colliding.
     *
     * @param o1
     * Circle 1
     * @param o2
     * Circle 2
     * @return Returns true if the 2 circles are colliding.
     */
    private static boolean collisionCircleCircle(CollisionDAO responding, CollisionDAO other)
    {
        double dx = responding.x - other.x;
        double dy = responding.y - other.y;
        double respondingRadius = responding.height / 2;
        double otherRadius = other.height / 2;

        return Math.sqrt((dx * dx) + (dy * dy)) <= (respondingRadius + otherRadius);
    }

    /**
     * Checks if a circle and rectangle is colliding.
     *
     * @param circle
     * The circle
     * @param rect
     * The rectangle.
     * @return Returns true if the rectangle and the circle is colliding.
     */
    public static boolean collisionCircleRectangle(CollisionDAO circ, CollisionDAO rect)
    {

        double circleDistanceX = Math.abs(rect.centerX - circ.centerX);
        double circleDistanceY = Math.abs(rect.centerY - circ.centerY);

        if (circleDistanceY >= (rect.height / 2 + circ.height / 2))
        {
            return false;
        }
        if (circleDistanceX >= (rect.width / 2 + circ.width / 2))
        {
            return false;
        }
        if (circleDistanceY < (rect.height / 2))
        {
            return true;
        }
        if (circleDistanceX < (rect.width) / 2)
        {
            return true;
        }
        double cornerDistanceSq = Math.sqrt(
                Math.pow((circleDistanceX - (rect.width / 2)), 2) + Math.pow((circleDistanceY - (rect.height / 2)), 2));

        return (cornerDistanceSq < circ.height / 2);
    }

    /**
     * Checks if 2 rectangles are colliding.
     *
     * @param rect1
     * Rectangle 1
     * @param rect2
     * Rectangle 2
     * @return Returns true if the 2 rectangles are colliding
     */
    public static boolean collisionRectangleRectangle(CollisionDAO o1, CollisionDAO o2)
    {
        boolean xOverlap = valueInRange(o1.x, o2.x, o2.x + o2.width) || valueInRange(o2.x, o1.x, o1.x + o1.width);
        boolean yOverlap = valueInRange(o1.y, o2.y, o2.y + o2.height) || valueInRange(o2.y, o1.y, o1.y + o1.height);
        return xOverlap && yOverlap;
    }

    /**
     * Checks if a value is within a range.
     *
     * @param value
     * The value to be checked if its within a range.
     * @param start
     * The start of the range.
     * @param end
     * The end of the range.
     * @return Returns true if the value is bigger than or equal to the start value and smaller than or equal to the end value.
     */
    private static boolean valueInRange(double value, double start, double end)
    {
        // FIXME if more methods like these pop up around the application it should be put in a new Math class.
        return (value >= start) && (value <= end);
    }

    private static Position getCenter(Position pos, Body body)
    {
        return new Position((pos.getX() + (body.getWidth() / 2)), pos.getY() + (body.getHeight() / 2));
    }

    /**
     * Provides a proper response where all kinetic energy is lost.
     *
     * @param collided
     * The object that will be acting to the collision.
     * @param other
     * The object the acting object collided with.
     * @return Returns the new suggested position of the collided object.
     */
    public static Position collisionResponse(Entity e1, Entity e2)
    {
        CollisionDAO responding = new CollisionDAO(e1);
        CollisionDAO other = new CollisionDAO(e2);

        if (responding.geometry == CIRCLE && other.geometry == CIRCLE)
        {
            return collisionResponseCircleCircle(responding, other);
        }
        else if (responding.geometry == CIRCLE && other.geometry == RECTANGLE)
        {
            return collisionResponseCircleRectangle(responding, other);
        }
        else if (responding.geometry == RECTANGLE && other.geometry == CIRCLE)
        {
            return collisionResponseRectangleCircle(responding, other);
        }
        else if (responding.geometry == RECTANGLE && other.geometry == RECTANGLE)
        {
            return collisionResponseRectangleRectangle(responding, other);
        }
        return null;
    }

    /**
     * Provides a proper response where all kinetic energy is lost for rectangle rectangle collision.
     *
     * @param collided
     * The object that will be acting to the collision.
     * @param other
     * The object the acting object collided with.
     * @return Returns the new suggested position of the collided object.
     */
    private static Position collisionResponseRectangleRectangle(CollisionDAO responding, CollisionDAO other)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Provides a proper response where all kinetic energy is lost for rectangle circle collision, where the rectangle is the object acting
     * on collision.
     *
     * @param collided
     * The object that will be acting to the collision.
     * @param other
     * The object the acting object collided with.
     * @return Returns the new suggested position of the collided object.
     */
    private static Position collisionResponseRectangleCircle(CollisionDAO responding, CollisionDAO other)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Provides a proper response where all kinetic energy is lost for circle circle collision.
     *
     * @param collided
     * The object that will be acting to the collision.
     * @param other
     * The object the acting object collided with.
     * @return Returns the new suggested position of the collided object.
     */
    public static Position collisionResponseCircleCircle(CollisionDAO responding, CollisionDAO other)
    {
        // http://ericleong.me/research/circle-circle/ Need this link for bullet bounce or similar.
        Vector2 distanceBetweenObjects = new Vector2(responding.x - other.x, responding.y - other.y);
        distanceBetweenObjects.setMagnitude(responding.height / 2 + other.height / 2);
        return new Position(other.x + distanceBetweenObjects.getX(), other.y + distanceBetweenObjects.getY());
    }

    /**
     * Provides a proper response where all kinetic energy is lost for rectangle circle collision, where the circle is the object acting on
     * collision.
     *
     * @param collided
     * The object that will be acting to the collision.
     * @param other
     * The object the acting object collided with.
     * @return Returns the new suggested position of the collided object.
     */
    public static Position collisionResponseCircleRectangle(CollisionDAO responding, CollisionDAO other)
    {
        //Keep in mind, every logic in this method is under the assumption that the 2 objects are colliding.

        // If checked objects center is within the width of the square.
        if (responding.centerX > other.x && responding.centerX < other.x + other.width)
        {
            if (responding.centerY < other.centerY) // Means its closer to the top side (bottom side in libgdx)
            {
                return new Position(responding.x, other.y - responding.height);
            }
            else // Means its closer to the bottom side (top side in libgdx)
            {
                return new Position(responding.x, other.y + other.height);
            }

        }
        else if (responding.centerY > other.y && responding.centerY < other.y + other.height) // If checked objects center is within the height of the square.
        {
            if (responding.centerX < other.centerX) // Means its closer to the left side(
            {
                return new Position(other.x - responding.width, responding.y);
            }
            else // Means its closer to the right side.
            {
                return new Position(other.x + other.width, responding.y);
            }
        }
        // If the method has returned before now, the object has been moved to the nearest side of the square, relative to the circle center.
        // If not, it means the circles center is closer to a corner than it is to a side of the square.

        // The following code is trying to find the corner closest to the objects center. The object that needs a new position.
        double cornerX = other.x;
        double cornerY = other.y;
        if (responding.centerX > other.x) // Means the circle center is on the right side of the square.
        {
            cornerX += other.width; // We know the corner is on the right side.
            if (responding.centerY > other.y) // Means the circle center is below (Above in libgdx)
            {
                cornerY += other.height;  // We know the corner is on the buttom.
            }

        }
        else if (responding.centerX < other.x) // Means the circle center is on the left side of the square.
        {
            if (responding.centerY > other.y) // Means the circle center is below (Above in libgdx)
            {
                cornerY += other.height; // We know the corner is on the buttom.
            }
        }
        // http://math.stackexchange.com/questions/356792/how-to-find-nearest-point-on-line-of-rectangle-from-anywhere
        Vector2 distanceBetweenObjects = new Vector2(responding.centerX - cornerX, responding.centerY - cornerY);
        distanceBetweenObjects.setMagnitude(other.height / 2);
        return new Position(cornerX + distanceBetweenObjects.getX() - other.width / 2,
                cornerY + distanceBetweenObjects.getY() - other.height / 2);
    }

}
