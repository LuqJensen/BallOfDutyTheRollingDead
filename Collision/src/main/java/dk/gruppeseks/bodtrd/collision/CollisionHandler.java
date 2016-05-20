/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.collision;

import dk.gruppeseks.bodtrd.common.data.Entity;
import static dk.gruppeseks.bodtrd.common.data.entityelements.Body.Geometry.CIRCLE;
import static dk.gruppeseks.bodtrd.common.data.entityelements.Body.Geometry.RECTANGLE;
import dk.gruppeseks.bodtrd.common.data.entityelements.Owner;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;

/**
 *
 * @author Chris & Morten
 */
public class CollisionHandler
{
    private static boolean isConcrete(int flag)
    {
        return (flag & CollisionFlags.CONCRETE) == CollisionFlags.CONCRETE;
    }

    private static boolean isElusive(int flag)
    {
        return (flag & CollisionFlags.ELUSIVE) == CollisionFlags.ELUSIVE;
    }

    private static boolean canCollide(int flag1, int flag2)
    {
        boolean c1 = isConcrete(flag1);
        boolean c2 = isConcrete(flag2);
        return (c1 && c2) || (c1 && isElusive(flag2)) || (c2 && isElusive(flag1));
    }

    public static void addCollisionData(Entity e)
    {
        CollisionData data = new CollisionData();

        switch (e.getType())
        {
            case PLAYER:
            {
                data.setCollisionFlag(CollisionFlags.CONCRETE);
                break;
            }
            case PROJECTILE:
            {
                data.setCollisionFlag(CollisionFlags.ELUSIVE);
                break;
            }
            case WALL:
            {
                data.setCollisionFlag(CollisionFlags.CONCRETE);
                break;
            }
            case ENEMY:
            {
                data.setCollisionFlag(CollisionFlags.CONCRETE);
                break;
            }
        }

        e.add(data);
    }

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
        CollisionData cd1 = e1.get(CollisionData.class);
        CollisionData cd2 = e2.get(CollisionData.class);

        if (!canCollide(cd1.getCollisionFlag(), cd2.getCollisionFlag()))
        {
            return false;
        }

        Owner ow1 = e1.get(Owner.class);
        Owner ow2 = e2.get(Owner.class);

        // TODO find out if objects owned by the same owner can collide, atm they can.
        if ((ow1 != null && ow1.getId() == e2.getID()) || (ow2 != null && ow2.getId() == e1.getID()))
        {
            return false;
        }

        CollisionDAO o1 = new CollisionDAO(e1);
        CollisionDAO o2 = new CollisionDAO(e2);

        if (o1.geometry == CIRCLE)
        {
            if (o2.geometry == CIRCLE)
            {
                return collisionCircleCircle(o1, o2);
            }
            else
            {
                return collisionCircleRectangle(o1, o2);
            }
        }
        if (o1.geometry == RECTANGLE)
        {
            if (o2.geometry == CIRCLE)
            {
                return collisionCircleRectangle(o1, o2);
            }
            else
            {
                return collisionRectangleRectangle(o1, o2);
            }
        }
        return false;
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
        double dx = responding.centerX - other.centerX;
        double dy = responding.centerY - other.centerY;
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
        if (circleDistanceY < rect.height / 2)
        {
            return true;
        }
        if (circleDistanceX < rect.width / 2)
        {
            return true;
        }

        double cornerX = rect.x;
        double cornerY = rect.y;
        if (circ.centerX > rect.x) // Means the circle center is on the right side of the square.
        {
            cornerX += rect.width; // We know the corner is on the right side.
            if (circ.centerY > rect.y) // Means the circle center is below (Above in libgdx)
            {
                cornerY += rect.height;  // We know the corner is on the buttom.
            }

        }
        else if (circ.centerX < rect.x) // Means the circle center is on the left side of the square.
        {
            if (circ.centerY > rect.y) // Means the circle center is below (Above in libgdx)
            {
                cornerY += rect.height; // We know the corner is on the buttom.
            }
        }

        Vector2 cornerToCircCenter = new Vector2(new Position(cornerX, cornerY), new Position(circ.centerX, circ.centerY));

        return (cornerToCircCenter.getMagnitude() < circ.height / 2);
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
        throw new UnsupportedOperationException("Only geometry types rectangle and circle have implemented collision logic.");
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
        Position centerResponding = new Position(responding.centerX, responding.centerY);
        Position centerOther = new Position(other.centerX, other.centerY);
        // Creates a vector from the respondings center to the others center.
        Vector2 respondingToOther = new Vector2(centerResponding, centerOther);

        return new Position(other.centerX + respondingToOther.getX() - responding.width / 2, other.centerY + respondingToOther.getY() - responding.height / 2);
    }

    /**
     * Provides a proper response where all kinetic energy is lost for rectangle circle collision, where the circle is the object acting on
     * collision.
     *
     * @param collided
     * The object that will be acting to the collision.
     * @param rect
     * The object the acting object collided with.
     * @return Returns the new suggested position of the collided object.
     */
    public static Position collisionResponseCircleRectangle(CollisionDAO responding, CollisionDAO rect)
    {
        //Keep in mind, every logic in this method is under the assumption that the 2 objects are colliding.

        // If checked objects center is within the width of the square.
        if (responding.centerX > rect.x && responding.centerX < rect.x + rect.width)
        {
            if (responding.centerY < rect.centerY) // Means its closer to the top side (bottom side in libgdx)
            {
                return new Position(responding.x, rect.y - responding.height);
            }
            else // Means its closer to the bottom side (top side in libgdx)
            {
                return new Position(responding.x, rect.y + rect.height);
            }

        }
        else if (responding.centerY > rect.y && responding.centerY < rect.y + rect.height) // If checked objects center is within the height of the square.
        {
            if (responding.centerX < rect.centerX) // Means its closer to the left side(
            {
                return new Position(rect.x - responding.width, responding.y);
            }
            else // Means its closer to the right side.
            {
                return new Position(rect.x + rect.width, responding.y);
            }
        }
        // If the method has returned before now, the object has been moved to the nearest side of the square, relative to the circle center.
        // If not, it means the circles center is closer to a corner than it is to a side of the square.

        // The following code is trying to find the corner closest to the objects center. The object that needs a new position.
        double cornerX = rect.x;
        double cornerY = rect.y;
        if (responding.centerX > rect.x) // Means the circle center is on the right side of the square.
        {
            cornerX += rect.width; // We know the corner is on the right side.
            if (responding.centerY > rect.y) // Means the circle center is below (Above in libgdx)
            {
                cornerY += rect.height;  // We know the corner is on the buttom.
            }

        }
        else if (responding.centerX < rect.x) // Means the circle center is on the left side of the square.
        {
            if (responding.centerY > rect.y) // Means the circle center is below (Above in libgdx)
            {
                cornerY += rect.height; // We know the corner is on the buttom.
            }
        }
        // http://math.stackexchange.com/questions/356792/how-to-find-nearest-point-on-line-of-rectangle-from-anywhere
        // A vector from nearest corner to circle center.
        Vector2 vectorBetweenObjects = new Vector2(responding.centerX - cornerX, responding.centerY - cornerY);
        vectorBetweenObjects.setMagnitude(responding.height / 2);
        return new Position(cornerX + vectorBetweenObjects.getX() - responding.width / 2,
                cornerY + vectorBetweenObjects.getY() - responding.height / 2);
    }

}
