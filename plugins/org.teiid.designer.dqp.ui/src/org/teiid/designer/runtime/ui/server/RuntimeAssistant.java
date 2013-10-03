/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.runtime.ui.server;

import static org.teiid.designer.runtime.ui.DqpUiConstants.UTIL;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;
import org.teiid.core.designer.util.I18nUtil;
import org.teiid.designer.runtime.DqpPlugin;
import org.teiid.designer.runtime.PreferenceConstants;
import org.teiid.designer.runtime.preview.PreviewManager;
import org.teiid.designer.runtime.spi.ITeiidServer;
import org.teiid.designer.runtime.spi.ITeiidServerManager;


/**
 * The <code>RuntimeAssistant<code> ensures that the preview preference is enabled and that a default Teiid Instance exists.
 *
 * @since 8.0
 */
public final class RuntimeAssistant {

    /**
     * The I18n properties prefix key used by this class.
     */
    private static final String PREFIX = I18nUtil.getPropertyPrefix(RuntimeAssistant.class);
    private static boolean selectServerCancelled = false;

    /**
     * If the preview preference is disabled or a Teiid Instance does not exist, a dialog is shown asking the user if they want to
     * enable preview and create a server.
     * 
     * @param shell the shell used to display dialog if necessary
     * @return <code>true</code> if preview is enabled, a Teiid Instance exists, and a connection to the server has been made
     */
    public static boolean ensurePreviewEnabled( Shell shell ) {
        boolean previewEnabled = isPreviewEnabled();
        boolean previewServerExists = previewServerExists();
        boolean serverCreatedNotStarted = false;

        // dialog message (null if preview preference enabled and server exists)
        String msg = null;

        if (!previewEnabled && !previewServerExists) {
            msg = UTIL.getString(PREFIX + "previewDisabledNoTeiidInstanceMsg"); //$NON-NLS-1$
        } else if (!previewEnabled) {
            msg = UTIL.getString(PREFIX + "previewDisabledMsg"); //$NON-NLS-1$
        } else if (!previewServerExists) {
            msg = UTIL.getString(PREFIX + "noTeiidInstanceMsg"); //$NON-NLS-1$
        }

        // if necessary open question dialog
        if ((msg != null) && MessageDialog.openQuestion(shell, UTIL.getString(PREFIX + "confirmEnablePreviewTitle"), msg)) { //$NON-NLS-1$
            // if necessary change preference
            if (!previewEnabled) {
                IEclipsePreferences prefs = DqpPlugin.getInstance().getPreferences();
                prefs.putBoolean(PreferenceConstants.PREVIEW_ENABLED, true);

                // save
                try {
                    prefs.flush();
                } catch (BackingStoreException e) {
                    UTIL.log(e);
                }
            }

            // if necessary create new server
            if (!previewServerExists) {
                runNewServerAction(shell);
                // if new server created it won't be started, so cache creation status
                serverCreatedNotStarted = previewServerExists();
            }
        }

        // if dialog was shown get values again
        if (msg != null) {
            previewEnabled = isPreviewEnabled();
            previewServerExists = previewServerExists();

            // if preview is not enabled or server does not exist then user canceled the dialog or the new server wizard
            if (!previewEnabled || !previewServerExists) {
                return false;
            }
        }

        // abort preview if server is not connected
        // If server was created and not started (see above) then this call will not try to connect and be a no-op call
        return serverConnectionExists(shell, serverCreatedNotStarted);
    }

    /**
     * If a Teiid Instance does not exist, a dialog is shown asking the user if they want to create a server.
     * 
     * @param shell the shell used to display dialog if necessary
     * @param dialogMessage the localized question to ask the user if they want to create a new Teiid instance in order to continue
     *            on with task
     * @param ignoreFailedConnection used to ignore a failed connection and not present a warning dialog.
     * @return <code>true</code> if a Teiid Instance exists and can be connected to
     */
    public static boolean ensureServerConnection( Shell shell,
                                                  String dialogMessage,
    											  boolean ignoreFailedConnection) {
        if (!previewServerExists()) {
            if (MessageDialog.openQuestion(shell, UTIL.getString(PREFIX + "confirmCreateTeiidInstanceTitle"), dialogMessage)) { //$NON-NLS-1$
                runNewServerAction(shell);
            }

            // if server does not exist then user canceled the dialog or the new server wizard
            if (!previewServerExists()) {
                return false;
            }
        }

        // abort preview if server is not connected
        return serverConnectionExists(shell, ignoreFailedConnection);
    }

    /**
     * @return the default Teiid Instance (can be <code>null</code>)
     */
    private static ITeiidServer getServer() {
        return getServerManager().getDefaultServer();
    }

    /**
     * @return the server manager (never <code>null</code>)
     */
    private static ITeiidServerManager getServerManager() {
        return DqpPlugin.getInstance().getServerManager();
    }

    /**
     * @return <code>true</code> if the preview preference is enabled
     */
    private static boolean isPreviewEnabled() {
        return PreviewManager.getInstance().isPreviewEnabled();
    }

    /**
     * @return <code>true</code> if a default Teiid Instance exists
     */
    private static boolean previewServerExists() {
        return (getServerManager().getDefaultServer() != null);
    }

    /**
     * @param shell the shell used to run the new server wizard
     */
    public static void runNewServerAction( Shell shell ) {
        NewServerAction action = new NewServerAction(shell);
        action.run();
    }
    
    /**
     * @param shell the shell used to run the new server wizard
     */
    public static void runEditServerAction( Shell shell ) {
        EditServerAction action = new EditServerAction();
        action.run();
    }
    
    /**
     * Run the set default teiid instance action
     */
    public static void runSetDefaultServerAction () {
        SetDefaultServerAction action = new SetDefaultServerAction();
        action.run();
    }

    /**
     * Run the refresh server action
     */
    public static void runRefreshServerAction () {
        RefreshServerAction action = new RefreshServerAction();
        action.run();
    }

    /**
     * @param shell the shell where the dialog is displayed if necessary
     * @param ignoreFailedConnection ignore the failed connection and do not display warning dialog.
     * @return <code>true</code> if connection exists
     */
    private static boolean serverConnectionExists( Shell shell, boolean ignoreFailedConnection) {
        assert (getServer() != null);

        if (!getServer().isConnected()) {
        	if( !ignoreFailedConnection ) {
	            MessageDialog.openWarning(shell, UTIL.getString(PREFIX + "teiidNotConnectedTitle"), //$NON-NLS-1$
	                                    UTIL.getString(PREFIX + "teiidNotConnectedMsg", getServer().getDisplayName())); // getServer().getHost())); //$NON-NLS-1$
        	}
            return false;
        }

        return true;
    }
    
    /**
     * Convenience function for adapting a given object to the given
     * class-type using eclipse's registered adapters
     * 
     * @param adaptableObject
     * @param klazz
     * @return adapted object or null
     */
    public static <T> T adapt(Object adaptableObject, Class<T> klazz) {
        return (T) Platform.getAdapterManager().getAdapter(adaptableObject, klazz);
    }
    
    /**
     * Get a {@link ITeiidServer} from the given selection if one can be adapted
     * 
     * @param selection
     * @return server or null
     */
    public static ITeiidServer getServerFromSelection(ISelection selection) {
        if (selection == null || ! (selection instanceof StructuredSelection))
            return null;
        
        if (selection.isEmpty()) {
            return null;
        }
        
        StructuredSelection ss = (StructuredSelection) selection;
        Object element = ss.getFirstElement();
        
        return adapt(element, ITeiidServer.class);
    }

    /**
     * Prevent instance construction.
     */
    private RuntimeAssistant() {
        // nothing to do
    }

    /**
     * Can display a dialog, allowing the user to select a server should there
     * be more than one or return the single server in the event of only one.
     * 
     * @param shell the shell
     * @param includeNoDefaultOption 'true' includes the 'No Default' option, 'false' does not.
     * 
     * @return a selected {@link ITeiidServer}
     */
    public static ITeiidServer selectServer(Shell shell, boolean includeNoDefaultOption) {
        ITeiidServer selectedServer = null;
        selectServerCancelled = false;
        
        if (getServerManager().getServers().size() == 1) {
            selectedServer = getServerManager().getServers().iterator().next();
        } else if (getServerManager().getServers().size() > 1) {
            ServerSelectionDialog dialog = new ServerSelectionDialog(shell,includeNoDefaultOption);
            dialog.open();

            if (dialog.getReturnCode() == Window.OK) {
                selectedServer = dialog.getServer();
            } else {
            	selectServerCancelled = true;
            }
        }

        return selectedServer;
    }
    
    /**
     * Determine if the last server selection was cancelled.
     * @return 'true' if the selection dialog was cancelled, 'false' if not.
     */
    public static boolean selectServerWasCancelled() {
    	return selectServerCancelled;
    }
    
    /**
     * Determine if at least one available server
     * @return 'true' if at least one server is available, 'false' if not.
     */
    public static boolean hasAvailableServers() {
    	if(getServerManager().getServers().size()>0) return true;
    	return false;
    }

}
