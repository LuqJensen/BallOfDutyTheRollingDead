/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.weapon;

import dk.gruppeseks.bodtrd.common.data.Entity;
import static dk.gruppeseks.bodtrd.common.data.EntityType.PLAYER;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Dzenita Hasic
 */
@ServiceProvider(service = GamePluginSPI.class)
public class WeaponPlugin implements GamePluginSPI
{
    public static final String BULLET_IMAGE_FILE_PATH = "../../../Weapon/src/main/java/dk/gruppeseks/bodtrd/weapon/assets/ballblack.png";
    private IEntityProcessor _processor;
    private World _world;

    @Override
    public void start(World world)
    {
        Installer.Plugin = this;

        ViewManager.createView(BULLET_IMAGE_FILE_PATH, false);

        _world = world;

        _processor = new WeaponProcessor();
        _world.addProcessor(3, _processor);
        _world.addEnthusiast(PLAYER, _processor);

        for (Entity e : _world.entities())
        {
            if (e.getType() == PLAYER)
            {
                Weapon wep = new Weapon();
                wep.setAttackSpeed(0.4f);
                wep.setMaxAmmunition(300);
                wep.setCurrentAmmunition(wep.getMaxAmmunition());
                wep.setMaxMagazineSize(30);
                wep.setCurrentMagazineSize(wep.getMaxMagazineSize());
                wep.setReloadSpeed(2);

                e.add(wep);
            }
        }
    }

    @Override
    public void stop()
    {
        ViewManager.removeView(BULLET_IMAGE_FILE_PATH);

        _world.removeProcessor(_processor);
        _world.removeEnthusiast(PLAYER, _processor);

        for (Entity e : _world.entities())
        {
            if (e.getType() == PLAYER)
            {
                e.remove(Weapon.class);
            }
        }
    }

}
