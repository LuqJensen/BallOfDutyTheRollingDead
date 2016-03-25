/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.collision;

import dk.gruppeseks.bodtrd.collision.CollisionProcessor;
import dk.gruppeseks.bodtrd.collision.Installer;
import dk.gruppeseks.bodtrd.common.data.World;
import dk.gruppeseks.bodtrd.common.interfaces.IEntityProcessor;
import dk.gruppeseks.bodtrd.common.services.GamePluginSPI;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Chris
 */
@ServiceProvider(service = GamePluginSPI.class)
public class CollisionPlugin implements GamePluginSPI
{
    private IEntityProcessor _processor;
    private World _world;

    @Override
    public void start(World world)
    {
        _world = world;
        Installer.Plugin = this;

        _processor = new CollisionProcessor();
        world.addProcessor(5, _processor);
    }

    @Override
    public void stop()
    {
        _world.removeProcessor(_processor);
    }
}
