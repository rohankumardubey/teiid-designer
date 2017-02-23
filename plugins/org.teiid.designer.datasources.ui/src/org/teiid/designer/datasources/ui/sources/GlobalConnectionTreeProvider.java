/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.datasources.ui.sources;

import org.eclipse.datatools.connectivity.ICategory;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.teiid.designer.datasources.ui.UiConstants;
import org.teiid.designer.datasources.ui.UiPlugin;
import org.teiid.designer.datasources.ui.panels.DataSourceItem;
import org.teiid.designer.datatools.ui.dialogs.ConnectionProfileTreeProvider;

public class GlobalConnectionTreeProvider extends ConnectionProfileTreeProvider {
	GlobalConnectionManager manager;
	/**
    * This provider provides content and label information for a combination of connection profiles and
    * default server connections (data sources & resource adapters)
    */
   public GlobalConnectionTreeProvider(GlobalConnectionManager manager) {
       super();
       this.manager = manager;
   }

	@Override
	public Object[] getElements(Object inputElement) {
		if( inputElement instanceof GlobalConnectionManager ) {
			return ((GlobalConnectionManager)inputElement).getRootNodes();
		}
		
		return super.getElements(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof RootConnectionNode ) {
			RootConnectionNode node = (RootConnectionNode)parentElement;
			if( node.isProfile() ) {
				return super.getChildren(ProfileManager.getInstance());
			} else {
				return manager.getDataSources();
			}
		}
		return super.getChildren(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return super.getParent(element);
	}

	@Override
	public boolean hasChildren(Object element) {
		if( element instanceof RootConnectionNode ) {
			RootConnectionNode node = (RootConnectionNode)element;
			if( node.isProfile() ) return super.hasChildren(element);
			else {
				return manager.getDataSources().length > 0;
			}
		}
		return super.hasChildren(element);
	}

	@Override
	public String getText(Object element) {
		String text = null;
		if (element instanceof ProfileManager) {
			text = "Profile Manager"; //$NON-NLS-1$
		} else if (element instanceof IConnectionProfile) {
			IConnectionProfile profile = (IConnectionProfile) element;
			text = profile.getName();
		} else if (element instanceof ICategory) {
			text = ((ICategory)element).getName();
			if( text.equals(UiConstants.DATABASE_CONNECTIONS) ) {
				text = "JDBC";
			} else if( text.equals(UiConstants.ODA_CONNECTIONS) ) {
				text = "ODA";
			} else if( text.equals(UiConstants.TEIID_CONNECTIONS) ) {
				text = "Teiid";
			}
		} if( element instanceof RootConnectionNode ) {
			RootConnectionNode node = (RootConnectionNode)element;
			return node.getName();
		} else if( element instanceof DataSourceItem ) {
			DataSourceItem item = (DataSourceItem)element; 
			return item.getName();
		} else {
			text = super.getText(element);
		}
		
		if (text != null && text.trim().length() > 0) {
			text = TextProcessor.process(text);
		}
		return text;
	}

	@Override
	public Image getImage(Object element) {
		Image image;
		if( element instanceof RootConnectionNode ) {
			RootConnectionNode node = (RootConnectionNode)element;
			if( node.isDataSource() )  {
				image = UiPlugin.getDefault().getImage(UiConstants.ImageIds.TEIID_SERVER);
			} else {
				image = UiPlugin.getDefault().getImage(UiConstants.ImageIds.PROFILES);
			}
		} else if (element instanceof ICategory) {
			image = PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_FOLDER);
		} else if (element instanceof IConnectionProfile) {
			image = UiPlugin.getDefault().getImage(UiConstants.ImageIds.CONNECTION);
		} else if( element instanceof DataSourceItem ) {
			image = UiPlugin.getDefault().getImage(UiConstants.ImageIds.TEIID_JDBC_SOURCE);
		} else {
			image = null;
		}
		return image;
	}
   
   
   
   
}