/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.zombie;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.EntityType;
import dk.gruppeseks.bodtrd.common.data.GameData;
import dk.gruppeseks.bodtrd.common.data.ViewManager;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.data.entityelements.Body;
import dk.gruppeseks.bodtrd.common.data.entityelements.Damage;
import dk.gruppeseks.bodtrd.common.data.entityelements.Health.Health;
import dk.gruppeseks.bodtrd.common.data.entityelements.Position;
import dk.gruppeseks.bodtrd.common.data.entityelements.Velocity;
import dk.gruppeseks.bodtrd.common.data.entityelements.Weapon;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.common.services.AISPI;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author frede
 */
@ServiceProvider(service = GamePluginSPI.class)
public class ZombiePlugin implements GamePluginSPI
{
    private static final String ZOMBIE_IMAGE_FILE_PATH = "../../../Zombie/src/main/java/dk/gruppeseks/bodtrd/zombie/assets/ball_zombie.png";
    private Map<Integer, Entity> _zombies;
    private IEntityProcessor _processor;
    private GameData _gameData;
    private World _world;
    private final int BASE_DIAMETER = 30;
    private final int DIAMETER_VARIABLE = 20;
    public static AISPI _ai;
    private final Lookup _lookup = Lookup.getDefault();

    @Override
    public void start(World world)
    {

        Installer.Plugin = this;

        ViewManager.createView(ZOMBIE_IMAGE_FILE_PATH, false);

        _world = world;
        _zombies = new HashMap();

        for (int i = 0; i < 2; i++)
        {
            Entity zombie = createZombieEntity();
            _zombies.put(zombie.getID(), zombie);
            world.addEntity(zombie);
        }

        _ai = _lookup.lookup(AISPI.class);

        _processor = new ZombieProcessor(_zombies, _ai);
        _world.addProcessor(1, _processor);

    }

    @Override
    public void stop()
    {
        ViewManager.removeView(ZOMBIE_IMAGE_FILE_PATH);

        _world.removeProcessor(_processor);
        for (Entity zombie : _zombies.values())
        {
            _world.removeEntity(zombie);
        }
    }

    private Entity createZombieEntity()
    {
        Entity entity = new Entity();
        entity.setType(EntityType.ENEMY);
        int diameter = (int)(BASE_DIAMETER + Math.random() * DIAMETER_VARIABLE);
        entity.add(new Position(diameter * 2 + Math.random() * (_world.getMap().getWidth() - diameter * 2), diameter * 2 + Math.random() * (_world.getMap().getHeight() - diameter * 2)));
        entity.add(new Body(diameter, diameter, Body.Geometry.CIRCLE));
        entity.add(new Velocity());
        entity.add(new Health(100, 0));
        entity.add(ViewManager.getView(ZOMBIE_IMAGE_FILE_PATH));
        Weapon wep = new Weapon();
        wep.setAttackSpeed(1.5f);
        wep.setAttackDamage(new Damage(3));
        entity.add(wep);

        return entity;
    }
}
