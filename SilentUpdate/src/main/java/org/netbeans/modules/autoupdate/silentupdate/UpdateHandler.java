package org.netbeans.modules.autoupdate.silentupdate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.netbeans.api.autoupdate.InstallSupport;
import org.netbeans.api.autoupdate.InstallSupport.Installer;
import org.netbeans.api.autoupdate.InstallSupport.Validator;
import org.netbeans.api.autoupdate.OperationContainer;
import org.netbeans.api.autoupdate.OperationContainer.OperationInfo;
import org.netbeans.api.autoupdate.OperationException;
import org.netbeans.api.autoupdate.OperationSupport;
import org.netbeans.api.autoupdate.OperationSupport.Restarter;
import org.netbeans.api.autoupdate.UpdateElement;
import org.netbeans.api.autoupdate.UpdateManager;
import org.netbeans.api.autoupdate.UpdateUnit;
import org.netbeans.api.autoupdate.UpdateUnitProvider;
import org.netbeans.api.autoupdate.UpdateUnitProviderFactory;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * @author https://github.com/corfixen
 */
public final class UpdateHandler
{

    public static final String SILENT_UC_CODE_NAME = "org_netbeans_modules_autoupdate_silentupdate_update_center"; // NOI18N
    private static List<UpdateUnit> lastUpdateUnits = new ArrayList();

    public static boolean timeToCheck()
    {
        // every startup
        return true;
    }

    public static class UpdateHandlerException extends Exception
    {

        public UpdateHandlerException(String msg)
        {
            super(msg);
        }

        public UpdateHandlerException(String msg, Throwable th)
        {
            super(msg, th);
        }
    }

    public static void checkAndHandleUpdates()
    {
        // refresh silent update center first
        refreshSilentUpdateProvider();

        Collection<UpdateElement> updates = findUpdates();
        Collection<UpdateElement> available = Collections.emptySet();
        if (newModuleInstallationAllowed())
        {
            available = findNewModules();
        }
        if (updates.isEmpty() && available.isEmpty())
        {
            // none for install
            OutputLogger.log("None for install");
            return;
        }

        // create a container for install
        OperationContainer<InstallSupport> containerForInstall = feedContainer(available, false);
        if (containerForInstall != null)
        {
            try
            {
                handleInstall(containerForInstall);
                OutputLogger.log("Install new modules done.");
            }
            catch (UpdateHandlerException ex)
            {
                OutputLogger.log(ex.getLocalizedMessage(), ex.getCause());
                return;
            }
        }

        // create a container for update
        OperationContainer<InstallSupport> containerForUpdate = feedContainer(updates, true);
        if (containerForUpdate != null)
        {
            try
            {
                handleInstall(containerForUpdate);
                OutputLogger.log("Update done.");
            }
            catch (UpdateHandlerException ex)
            {
                OutputLogger.log(ex.getLocalizedMessage(), ex.getCause());
                return;
            }
        }

    }

    public static boolean isLicenseApproved(String license)
    {
        // place your code there
        return true;
    }

    // package private methods
    static Collection<UpdateElement> findUpdates()
    {
        // check updates
        Collection<UpdateElement> elements4update = new HashSet<UpdateElement>();
        List<UpdateUnit> updateUnits = UpdateManager.getDefault().getUpdateUnits();
        for (UpdateUnit unit : updateUnits)
        {
            // System.out.println("name: " + unit.getCodeName() + "   installed: " + (unit.getInstalled() != null) + "   available update: " + !unit.getAvailableUpdates().isEmpty());
            if (unit.getInstalled() != null)
            { // means the plugin already installed
                if (!unit.getAvailableUpdates().isEmpty())
                { // has updates
                    elements4update.add(unit.getAvailableUpdates().get(0)); // add plugin with highest version
                }
            }
        }
        return elements4update;
    }

    static void handleInstall(OperationContainer<InstallSupport> container) throws UpdateHandlerException
    {
        // check licenses
        if (!allLicensesApproved(container))
        {
            // have a problem => cannot continue
            throw new UpdateHandlerException("Cannot continue because license approval is missing for some updates.");
        }

        // download
        InstallSupport support = container.getSupport();

        Validator v = null;
        try
        {
            v = doDownload(support);
        }
        catch (OperationException ex)
        {
            // caught a exception
            throw new UpdateHandlerException("A problem caught while downloading, cause: ", ex);
        }
        if (v == null)
        {
            // have a problem => cannot continue
            throw new UpdateHandlerException("Missing Update Validator => cannot continue.");
        }

        // verify
        Installer i = null;
        try
        {
            i = doVerify(support, v);
        }
        catch (OperationException ex)
        {
            // caught a exception
            throw new UpdateHandlerException("A problem caught while verification of updates, cause: ", ex);
        }
        if (i == null)
        {
            // have a problem => cannot continue
            throw new UpdateHandlerException("Missing Update Installer => cannot continue.");
        }

        // install
        Restarter r = null;
        try
        {
            r = doInstall(support, i);
        }
        catch (OperationException ex)
        {
            // caught a exception
            throw new UpdateHandlerException("A problem caught while installation of updates, cause: ", ex);
        }

        // restart later
        support.doRestartLater(r);
        return;
    }

    static Collection<UpdateElement> findNewModules()
    {
        // check updates
        Collection<UpdateElement> elements4install = new HashSet<UpdateElement>();
        List<UpdateUnit> updateUnits = UpdateManager.getDefault().getUpdateUnits();
        for (UpdateUnit unit : updateUnits)
        {

            if (unit.getInstalled() == null)
            { // means the plugin is not installed yet
                if (!unit.getAvailableUpdates().isEmpty())
                { // is available
                    elements4install.add(unit.getAvailableUpdates().get(0)); // add plugin with highest version
                }
            }
        }
        return elements4install;
    }

    static void uninstallRemovedModules()
    {
        UpdateUnitProvider silentUpdateProvider = getSilentUpdateProvider();
        List<UpdateUnit> updateUnits = silentUpdateProvider.getUpdateUnits();
        List<String> unitsToUninstall = new ArrayList();

        for (UpdateUnit lastUnit : lastUpdateUnits)
        {
            if (!updateUnits.contains(lastUnit))
            {
                unitsToUninstall.add(lastUnit.getCodeName());
            }
        }
        if (unitsToUninstall.size() > 0)
        {
            doUninstall(unitsToUninstall);
        }

        lastUpdateUnits = updateUnits;
    }

    static void refreshSilentUpdateProvider()
    {

        UpdateUnitProvider silentUpdateProvider = getSilentUpdateProvider();
        if (silentUpdateProvider == null)
        {
            // have a problem => cannot continue
            OutputLogger.log("Missing Silent Update Provider => cannot continue.");
            return;
        }
        try
        {
            silentUpdateProvider.refresh(null, true);
        }
        catch (IOException ex)
        {
            // caught a exception
            OutputLogger.log("A problem caught while refreshing Update Centers, cause: ", ex);
        }
    }

    static UpdateUnitProvider getSilentUpdateProvider()
    {
        List<UpdateUnitProvider> providers = UpdateUnitProviderFactory.getDefault().getUpdateUnitProviders(true);
        for (UpdateUnitProvider p : providers)
        {
            if (SILENT_UC_CODE_NAME.equals(p.getName()))
            {
                try
                {
                    p.refresh(null, true);
                }
                catch (IOException ex)
                {
                    // caught a exception
                    OutputLogger.log("A problem caught while refreshing Update Centers, cause: ", ex);
                }
                return p;
            }
        }
        return null;
    }

    static OperationContainer<InstallSupport> feedContainer(Collection<UpdateElement> updates, boolean update)
    {
        if (updates == null || updates.isEmpty())
        {
            return null;
        }
        // create a container for update
        OperationContainer<InstallSupport> container;
        if (update)
        {
            container = OperationContainer.createForUpdate();
        }
        else
        {
            container = OperationContainer.createForInstall();
        }

        // loop all updates and add to container for update
        for (UpdateElement ue : updates)
        {
            if (container.canBeAdded(ue.getUpdateUnit(), ue))
            {
                OutputLogger.log("Update found: " + ue);
                OperationInfo<InstallSupport> operationInfo = container.add(ue);
                if (operationInfo == null)
                {
                    continue;
                }
                container.add(operationInfo.getRequiredElements());
                if (!operationInfo.getBrokenDependencies().isEmpty())
                {
                    // have a problem => cannot continue
                    OutputLogger.log("There are broken dependencies => cannot continue, broken deps: " + operationInfo.getBrokenDependencies());
                    return null;
                }
            }
        }
        return container;
    }

    static boolean allLicensesApproved(OperationContainer<InstallSupport> container)
    {
        if (!container.listInvalid().isEmpty())
        {
            return false;
        }
        for (OperationInfo<InstallSupport> info : container.listAll())
        {
            String license = info.getUpdateElement().getLicence();
            if (!isLicenseApproved(license))
            {
                return false;
            }
        }
        return true;
    }

    static Validator doDownload(InstallSupport support) throws OperationException
    {
        return support.doDownload(null, true);
    }

    static Installer doVerify(InstallSupport support, Validator validator) throws OperationException
    {

        Installer installer = support.doValidate(validator, null); // validates all plugins are correctly downloaded

        // XXX: use there methods to make sure updates are signed and trusted
        // installSupport.isSigned(installer, <an update element>);
        // installSupport.isTrusted(installer, <an update element>);
        return installer;
    }

    static Restarter doInstall(InstallSupport support, Installer installer) throws OperationException
    {
        OperationContainer<OperationSupport> con = OperationContainer.createForDirectUninstall();
        return support.doInstall(installer, null);
    }

    static void doDisable(Collection codeNames)
    { // codeName contains code name of modules for disable
        Collection<UpdateElement> toDisable = new HashSet();
        List<UpdateUnit> allUpdateUnits = UpdateManager.getDefault().getUpdateUnits(UpdateManager.TYPE.MODULE);
        for (UpdateUnit unit : allUpdateUnits)
        {
            if (unit.getInstalled() != null)
            { // filter all installed modules
                UpdateElement el = unit.getInstalled();
                if (el.isEnabled())
                { // filter all enabled modules
                    if (codeNames.contains(el.getCodeName()))
                    { // filter given module in the parameter
                        toDisable.add(el);
                    }
                }
            }
        }

        OperationContainer<OperationSupport> oc = OperationContainer.createForDirectDisable();

        for (UpdateElement module : toDisable)
        {
            if (oc.canBeAdded(module.getUpdateUnit(), module))
            { // check if module can be disabled
                OperationInfo operationInfo = oc.add(module);
                if (operationInfo == null)
                { // it means it's already planned to disable
                    continue;
                }
                // get all module depending on this module
                Set requiredElements = operationInfo.getRequiredElements();
                // add all of them between modules for disable
                oc.add(requiredElements);
            }
        }

        // check the container doesn't contain any invalid element
        assert oc.listInvalid().isEmpty();
        try
        {
            // get operation support for complete the disable operation
            Restarter restarter = oc.getSupport().doOperation(null);
            // no restart needed in this case
            assert restarter == null;
        }
        catch (OperationException ex)
        {
            Exceptions.printStackTrace(ex);
        }
    }

    static void doUninstall(Collection codeNames)
    { // codeName contains code name of modules for disable
        Collection<UpdateElement> toUninstall = new HashSet();
        List<UpdateUnit> allUpdateUnits = UpdateManager.getDefault().getUpdateUnits(UpdateManager.TYPE.MODULE);
        for (UpdateUnit unit : allUpdateUnits)
        {
            if (unit.getInstalled() != null)
            { // filter all installed modules
                UpdateElement el = unit.getInstalled();
                if (el.isEnabled())
                { // filter all enabled modules
                    if (codeNames.contains(el.getCodeName()))
                    { // filter given module in the parameter
                        toUninstall.add(el);
                    }
                }
            }
        }

        OperationContainer<OperationSupport> oc = OperationContainer.createForDirectUninstall();

        for (UpdateElement module : toUninstall)
        {
            if (oc.canBeAdded(module.getUpdateUnit(), module))
            { // check if module can be disabled
                OperationInfo operationInfo = oc.add(module);
                if (operationInfo == null)
                { // it means it's already planned to disable
                    continue;
                }
                // get all module depending on this module
                Set requiredElements = operationInfo.getRequiredElements();
                // add all of them between modules for disable
                oc.add(requiredElements);
            }
        }

        // check the container doesn't contain any invalid element
        assert oc.listInvalid().isEmpty();
        try
        {
            // get operation support for complete the disable operation
            Restarter restarter = oc.getSupport().doOperation(null);
            // no restart needed in this case
            assert restarter == null;
        }
        catch (OperationException ex)
        {
            Exceptions.printStackTrace(ex);
        }
    }

    private static boolean newModuleInstallationAllowed()
    {
        String s = NbBundle.getBundle("org.netbeans.modules.autoupdate.silentupdate.resources.Bundle").getString("UpdateHandler.NewModules");
        return Boolean.parseBoolean(s);
    }
}
