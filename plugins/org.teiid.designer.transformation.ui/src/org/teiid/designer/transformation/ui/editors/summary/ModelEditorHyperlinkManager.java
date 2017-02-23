package org.teiid.designer.transformation.ui.editors.summary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.teiid.designer.core.workspace.ModelResource;
import org.teiid.designer.metamodels.relational.BaseTable;
import org.teiid.designer.transformation.ui.actions.GenerateDependencyReportAction;
import org.teiid.designer.transformation.ui.actions.ShowDependencyDiagramAction;
import org.teiid.designer.ui.actions.ModelResourceActionManager;
import org.teiid.designer.ui.actions.SortableSelectionAction;
import org.teiid.designer.ui.common.actions.ModelActionConstants;

public class ModelEditorHyperlinkManager implements ModelActionConstants, ISelectionChangedListener {
    
	final ModelEditorMainPage editorPage;
	final FormToolkit toolkit;
    final ModelResource modelResource;
    final IResource iResource;
    final boolean isRelational;
    final boolean isVirtual;
    static ShowDependencyDiagramAction showDiagramAction = new ShowDependencyDiagramAction();
    static GenerateDependencyReportAction depReportAction = new GenerateDependencyReportAction();

	public ModelEditorHyperlinkManager(ModelEditorMainPage editorPage) {
		this.editorPage = editorPage;
		this.toolkit = editorPage.toolkit;
		this.modelResource = editorPage.modelResource;
		this.iResource = editorPage.iResource;
		
		isRelational = editorPage.isRelational;
		isVirtual = editorPage.isVirtual;
	}
	
	public void addConnectionHyperLinkActions(Composite parent) {
		
		if( isRelational && ! isVirtual ) {
			// Add Create Data Source and View PRofile actions
			createModelHyperLink(parent, "Create Data Source", Resource.CREATE_DATA_SOURCE);
			createModelHyperLink(parent, "View Connection Info", Resource.VIEW_CONNECTION_PROFILE);
			createModelHyperLink(parent, "Set Connection Profile", Resource.SET_CONNECTION_PROFILE);
			createModelHyperLink(parent, "Remove Connection Info", Resource.REMOVE_CONNECTION_INFO);
			createModelHyperLink(parent, "Set JBoss Data Source Name", Resource.SET_JBOSS_DATA_SOURCE_NAME);
			createModelHyperLink(parent, "Set Translator Name", Resource.SET_TRANSLATOR_NAME);
			createModelHyperLink(parent, "Edit Translator Overrides", Resource.EDIT_TRANSLATOR_OVERRIDES);
		}

	}
	
	public void addPrimaryHyperLinkActions(Composite parent) {
		createWizardHyperlink(parent,  "Export Teiid DDL", WizardsIDs.EXPORT_TEIID_DDL);
		createWizardHyperlink(parent,  "Create VDB", WizardsIDs.NEW_VDB);
		if( isRelational && isVirtual ) {
			createModelHyperLink(parent, "Edit Transformation", Resource.EDIT_TRANSFORMATION);
		}
	}
	
	public void addGeneralHyperLinkActions(Composite parent) {
		createModelHyperLink(parent, "Manage Extensions", Resource.MANAGE_MODEL_EXTENSIONS);
		createModelHyperLink(parent, "Show Model Statistics", Resource.SHOW_MODEL_STATISTICS_ACTION);
	}
	
	private void createWizardHyperlink(Composite parent, String label, String wizardId) {
		Hyperlink newHL = toolkit.createHyperlink(parent, label, SWT.NONE); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).applyTo(newHL);

		newHL.addHyperlinkListener(new LaunchWizardHyperlinkHandler(iResource, wizardId));
	}
	
	private void createModelHyperLink(Composite parent, String label, String actionId) {
		SortableSelectionAction action = ModelResourceActionManager.getAction(actionId);
		
		if( action != null ) {
			Hyperlink newHL = toolkit.createHyperlink(parent, label, SWT.NONE); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(newHL);
	
			newHL.addHyperlinkListener(new IHyperlinkListener() {
				
				@Override
				public void linkExited(org.eclipse.ui.forms.events.HyperlinkEvent e) {
				}
				
				@Override
				public void linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent e) {
				}
				
				@Override
				public void linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent e) {
					action.selectionChanged(editorPage, new StructuredSelection(iResource));
					action.run();
				}
			});
		}
	}
	
	public boolean isVirtual() {
		return isVirtual;
	}
	
	public boolean isRelational() {
		return isRelational;
	}

	public static IAction getAction(String actionID) {
		if( actionID.equalsIgnoreCase(ModelActionConstants.Custom.GENERATE_DEPENDENCY_REPORT) ) {
			return depReportAction;
		} else if( actionID.equalsIgnoreCase(ModelActionConstants.Custom.SHOW_DEPENCENCY_DIAGRAM) ) {
			return showDiagramAction;
		}
		
		return null;
	}
	
	public static Collection<IAction> getCustomActions(Object eObject) {
		Collection<IAction> actions = new ArrayList<IAction>();
		if( depReportAction.isEnabled() ) {
			actions.add(depReportAction);
		}
		if( showDiagramAction.isEnabled() ) {
			actions.add(showDiagramAction);
		}
//		if( isVirtual && isRelational ) {
//			if( eObject instanceof BaseTable ) {
//				IAction action1 = getAction(ModelActionConstants.Custom.SHOW_DEPENCENCY_DIAGRAM);
//				if( action1 != null ) {
//					actions.add(action1);
//				}
//				IAction action2 = getAction(ModelActionConstants.Custom.GENERATE_DEPENDENCY_REPORT);
//				if( action2 != null ) {
//					actions.add(action2);
//				}
//			}
//		}
		
		return actions;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		depReportAction.selectionChanged(event);
		showDiagramAction.selectionChanged(event);
	}

}