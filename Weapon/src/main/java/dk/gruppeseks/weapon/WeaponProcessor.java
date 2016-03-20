/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.weapon;

import dk.gruppeseks.bodtrd.common.data.Entity;
import static dk.gruppeseks.bodtrd.common.data.EntityType.PROJECTILE;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.data.util.Vector2;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;

/**
 *
 * @author Dzenita Hasic
 */
public class WeaponProcessor implements IEntityProcessor
{
    @Override
    public void process(World world)
    {
        for (Entity e : world.entities())
        {
            Weapon wep = e.get(Weapon.class);

            if (wep == null)
            {
                continue;
            }
            if (wep.getReloadTimeLeft() > 0)
            {
                wep.setReloadTimeLeft((float)(wep.getReloadTimeLeft() - world.getGameData().getDeltaTime()));
                continue;
            }

            handleReloading(wep);

            if (!wep.isAttacking())
            {
                continue;
            }
            if (!(wep.getCurrentMagazineSize() > 0))
            {
                if (!(wep.getCurrentAmmunition() > 0))
                {
                    continue;
                }
                wep.setReloading(true);
                continue;
            }

            wep.setAttackCooldown((float)(wep.getAttackCooldown() - world.getGameData().getDeltaTime()));

            if (wep.getAttackCooldown() > 0)
            {
                continue;
            }

            attack(world, e, wep);
        }
    }

    private void attack(World world, Entity e, Weapon wep)
    {
        Position p = e.get(Position.class);
        Position mousePos = world.getGameData().getMousePosition();
        wep.setOrientation(new Vector2(mousePos.getX() - p.getX(), mousePos.getY() - p.getY()));
        System.out.println("Mouse x,y: " + mousePos.getX() + ", " + mousePos.getY() + "   entity x, y: " + p.getX() + ", " + p.getY());
        Vector2 orientation = wep.getOrientation().setMagnitude(e.get(Body.class).getWidth() / 2);
        Position position = new Position(p.getX() + orientation.getX(), p.getY() + orientation.getY());
        Velocity velocity = new Velocity(wep.getOrientation().setMagnitude(500));//;TODO: bulletSpeed

        Entity bullet = new Entity();
        bullet.setType(PROJECTILE);
        bullet.add(position);
        bullet.add(velocity);
        bullet.add(new Body(20, 20));
        bullet.add(ViewManager.getView(WeaponPlugin.BULLET_IMAGE_FILE_PATH));
        world.addEntity(bullet);

        wep.setCurrentMagazineSize(wep.getCurrentMagazineSize() - 1);
        wep.setAttackCooldown(wep.getAttackSpeed());
    }

    private void handleReloading(Weapon wep)
    {
        if (wep.isReloading())
        {
            int currentAmmunition = wep.getCurrentAmmunition();
            int removedAmmunition = Math.min(currentAmmunition, wep.getMaxMagazineSize());
            wep.setCurrentAmmunition(currentAmmunition - removedAmmunition);
            wep.setCurrentMagazineSize(removedAmmunition);
            wep.setReloading(false);
        }
    }

    @Override
    public void notifyEntitiesAdded(Entity entity)
    {
        Weapon wep = new Weapon();
        wep.setAttackSpeed(0.4f);
        wep.setMaxAmmunition(300);
        wep.setCurrentAmmunition(wep.getMaxAmmunition());
        wep.setMaxMagazineSize(30);
        wep.setCurrentMagazineSize(wep.getMaxMagazineSize());
        wep.setReloadSpeed(2);

        entity.add(wep);
    }

    @Override
    public void notifyEntitiesRemoved(Entity entity)
    {
    }

}
