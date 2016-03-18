package org.netbeans.modules.autoupdate.silentupdate;

/**
 *
 */
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static org.netbeans.modules.autoupdate.silentupdate.UpdateHandler.uninstallRemovedModules;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Activator extends ModuleInstall
{

    private final ScheduledExecutorService exector = Executors.newScheduledThreadPool(1);

    @Override
    public void restored()
    {
        exector.scheduleAtFixedRate(doCheck, 10000, 10000, TimeUnit.MILLISECONDS);
    }

    private static final Runnable doCheck = ()
            ->
            {
                if (UpdateHandler.timeToCheck())
                {
                    UpdateHandler.checkAndHandleUpdates();
                    uninstallRemovedModules();
//                    ArrayList<String> hej = new ArrayList();
//                    hej.add("dk.gruppeseks.Movement");
//                    try
//                    {
//                        Thread.sleep(3000);
//                    }
//                    catch (Exception ex)
//                    {
//
//                    }
//
//                    UpdateHandler.doUninstall(hej);
                }
    };

}
