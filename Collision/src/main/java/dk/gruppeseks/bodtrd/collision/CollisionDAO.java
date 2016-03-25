/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.collision;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body.Geometry;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;

/**
 *
 * @author Frederik
 */
public class CollisionDAO
{
    public double x;
    public double y;
    public double centerX;
    public double centerY;
    public int height;
    public int width;
    public Geometry geometry;

    public CollisionDAO(Entity e)
    {
        Position pos = e.get(Position.class);
        Body body = e.get(Body.class);
        x = pos.getX();
        y = pos.getY();
        height = body.getHeight();
        width = body.getWidth();
        geometry = body.getGeometry();
        centerX = x + width / 2;
        centerY = y + height / 2;
    }

}
