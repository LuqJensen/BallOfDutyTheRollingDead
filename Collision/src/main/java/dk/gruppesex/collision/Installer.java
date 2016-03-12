/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppesex.collision;

import dk.gruppesex.bodtrd.common.services.GamePluginSPI;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall
{

    public static GamePluginSPI Plugin = null;

    @Override
    public void restored()
    {
        // TODO
    }

    @Override
    public void uninstalled()
    {
        if (Plugin != null)
        {
            Plugin.stop();
        }
    }

}
