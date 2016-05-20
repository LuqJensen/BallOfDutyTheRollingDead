/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.ai;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.entityelements.AIData;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author frede
 */
public class Installer extends ModuleInstall
{
    public static boolean uninstalled = false;

    @Override
    public void restored()
    {
        uninstalled = false;
    }

    @Override
    public void uninstalled()
    {
        if (AIProvider._world != null)
        {
            for (Entity zombie : AIProvider._world.entities())
            {
                AIData dat = zombie.get(AIData.class);
                if (dat != null)
                {
                    synchronized (dat)
                    {
                        uninstalled = true;
                        dat.setPath(null);
                        dat.setUpdateTime(0);
                    }
                }
            }
        }
    }
}
