/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.aifov;

import dk.gruppeseks.bodtrd.common.data.Entity;
import dk.gruppeseks.bodtrd.common.data.entityelements.AIData;
import org.openide.modules.ModuleInstall;

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
        if (AIFoVProvider._world != null)
        {
            for (Entity zombie : AIFoVProvider._world.entities())
            {
                AIData dat = zombie.get(AIData.class);
                if (dat != null)
                {
                    synchronized (dat)
                    {
                        dat.setFoVShape(null);
                        dat.setLatestKnownPosition(null);
                        dat.setPath(null);
                        dat.setRotateSpeed(0);
                        uninstalled = true;
                        System.out.println("removed " + zombie.getID());
                    }
                }
            }
        }
    }
}
