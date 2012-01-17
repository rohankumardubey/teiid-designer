/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.advisor.ui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.teiid.designer.advisor.ui.AdvisorUiConstants;
import org.teiid.designer.advisor.ui.AdvisorUiPlugin;
import org.teiid.designer.advisor.ui.Messages;
import org.teiid.designer.advisor.ui.actions.AdvisorActionFactory;
import org.teiid.designer.advisor.ui.actions.AdvisorActionInfo;

import com.metamatrix.ui.internal.util.WidgetFactory;

public class AspectsSection implements AdvisorUiConstants{
	private FormToolkit toolkit;

	private Section section;
	Composite stackBodyPanel;
	StackLayout stackLayout;
	
	Map<String, Composite> stackedPanels;

	/**
	 * @param parent
	 * @param style
	 */
	public AspectsSection(FormToolkit toolkit, Composite parent) {
		super();
		this.toolkit = toolkit;
		stackedPanels = new HashMap<String, Composite>();
		createSection(parent);
	}
	
	private void createStackLayout(Composite parent) {
    	stackBodyPanel = new Composite(parent, SWT.NONE | SWT.FILL);
    	GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    	//gd.horizontalSpan = 2;
    	stackLayout = new StackLayout();
    	stackLayout.marginWidth = 0;
    	stackLayout.marginHeight = 0;
    	stackBodyPanel.setLayout(stackLayout);
    	stackBodyPanel.setLayoutData(gd);
    	stackBodyPanel.setData("name", "stackBodyPanel");  //$NON-NLS-1$//$NON-NLS-2$
	}

	@SuppressWarnings("unused")
	private void createSection(Composite theParent) {

        Section generalSection = this.toolkit.createSection(theParent, Section.TITLE_BAR | Section.EXPANDED );

        Color bkgdColor = this.toolkit.getColors().getBackground();
        generalSection.setText(Messages.ModelingAspectOptions);

//        generalSection.setDescription("Aspect Description....");
        
//        generalSection.getDescriptionControl().setForeground(this.toolkit.getColors().getColor(FormColors.TITLE));
        GridData gd = new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING);
        gd.horizontalSpan = 2;
        generalSection.setLayoutData(gd);

        Composite sectionBody = new Composite(generalSection, SWT.NONE);
        sectionBody.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        sectionBody.setBackground(bkgdColor);

        sectionBody.setLayout(new GridLayout());
        
        createStackLayout(sectionBody);

        Composite panel1 = createPanel_1(stackBodyPanel);
        stackLayout.topControl = panel1;
        stackedPanels.put(MODELING_ASPECT_IDS.MODEL_DATA_SOURCES, panel1);
        stackedPanels.put(MODELING_ASPECT_IDS.MANAGE_CONNECTIONS, createPanel_2(stackBodyPanel));
        stackedPanels.put(MODELING_ASPECT_IDS.MODEL_PROJECT_MANAGEMENT, createPanel_3(stackBodyPanel));
        stackedPanels.put(MODELING_ASPECT_IDS.MANAGE_VDBS, createPanel_4(stackBodyPanel));
        stackedPanels.put(MODELING_ASPECT_IDS.DEFINE_MODELS, createPanel_5(stackBodyPanel));

        generalSection.setClient(sectionBody);
	}
	
	private Composite createPanel_1(Composite parent) {
		Color bkgdColor = this.toolkit.getColors().getBackground();
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        panel.setLayout(new GridLayout());
        GridLayout gLayout = new GridLayout();
        gLayout.numColumns = 2;
        gLayout.marginWidth = 5;
        gLayout.horizontalSpacing = 5;
        gLayout.verticalSpacing = 5;
        panel.setLayout(gLayout);
        panel.setBackground(bkgdColor);
        
        //addHyperlink(panel, COMMAND_LABELS.IMPORT_JDBC, COMMAND_IDS.IMPORT_JDBC);
        addHyperlink(panel, COMMAND_IDS.IMPORT_JDBC, false);
        addHyperlink(panel, COMMAND_IDS.IMPORT_DDL, false);
        addHyperlink(panel, COMMAND_IDS.IMPORT_FLAT_FILE, false);
        addHyperlink(panel, COMMAND_IDS.IMPORT_XML_FILE, false);
        addHyperlink(panel, COMMAND_IDS.IMPORT_SALESFORCE, false);
        addHyperlink(panel, COMMAND_IDS.IMPORT_WSDL_TO_SOURCE, false);
        addHyperlink(panel, COMMAND_IDS.IMPORT_WSDL_TO_WS, false);
        
        return panel;
	}
	
	private Composite createPanel_2(Composite parent) {
		// 
		Color bkgdColor = this.toolkit.getColors().getBackground();
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        panel.setLayout(new GridLayout());
        GridLayout gLayout = new GridLayout();
        gLayout.numColumns = 2;
        gLayout.marginWidth = 5;
        gLayout.horizontalSpacing = 5;
        gLayout.verticalSpacing = 5;
        panel.setLayout(gLayout);
        panel.setBackground(bkgdColor);
        
        addHyperlink(panel, COMMAND_LABELS.OPEN_DATA_SOURCE_EXPLORER_PERSPECTIVE, COMMAND_IDS.OPEN_DATA_SOURCE_EXPLORER_PERSPECTIVE);
        
        Group group = WidgetFactory.createGroup(panel, "Create Connection", GridData.FILL_HORIZONTAL, 2, 2);
        
        addHyperlink(group, COMMAND_IDS.CREATE_CONNECTION_JDBC, true);
        addHyperlink(group, COMMAND_IDS.CREATE_CONNECTION_FLAT_FILE, true);
        addHyperlink(group, COMMAND_IDS.CREATE_CONNECTION_SALESFORCE, true);
        addHyperlink(group, COMMAND_IDS.CREATE_CONNECTION_XML_FILE_LOCAL, true);
        addHyperlink(group, COMMAND_IDS.CREATE_CONNECTION_XML_FILE_URL, true);

        return panel;
	}
	
	private Composite createPanel_3(Composite parent) {
		// 
		Color bkgdColor = this.toolkit.getColors().getBackground();
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        panel.setLayout(new GridLayout());
        GridLayout gLayout = new GridLayout();
        gLayout.numColumns = 2;
        gLayout.marginWidth = 5;
        gLayout.horizontalSpacing = 5;
        gLayout.verticalSpacing = 5;
        panel.setLayout(gLayout);
        panel.setBackground(bkgdColor);
        
        addHyperlink(panel, Messages.CreateTeiidModelProject, COMMAND_IDS.NEW_TEIID_MODEL_PROJECT);

        
        return panel;
	}
	
	private Composite createPanel_4(Composite parent) {
		// 
		Color bkgdColor = this.toolkit.getColors().getBackground();
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        panel.setLayout(new GridLayout());
        GridLayout gLayout = new GridLayout();
        gLayout.numColumns = 2;
        gLayout.marginWidth = 5;
        gLayout.horizontalSpacing = 5;
        gLayout.verticalSpacing = 5;
        panel.setLayout(gLayout);
        panel.setBackground(bkgdColor);
        
        addHyperlink(panel, Messages.CreateVdb, COMMAND_IDS.CREATE_VDB);
        addHyperlink(panel, Messages.ExecuteVdb, COMMAND_IDS.EXECUTE_VDB);
        
        return panel;
	}
	
	private Composite createPanel_5(Composite parent) {
		// 
		Color bkgdColor = this.toolkit.getColors().getBackground();
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        panel.setLayout(new GridLayout());
        GridLayout gLayout = new GridLayout();
        gLayout.numColumns = 2;
        gLayout.marginWidth = 5;
        gLayout.horizontalSpacing = 5;
        gLayout.verticalSpacing = 5;
        panel.setLayout(gLayout);
        panel.setBackground(bkgdColor);
        
        Group group = WidgetFactory.createGroup(panel, "Create", GridData.FILL_HORIZONTAL, 2, 2);
        addHyperlink(group, COMMAND_IDS.NEW_MODEL_RELATIONAL_SOURCE, true);
        addHyperlink(group, COMMAND_IDS.NEW_MODEL_RELATIONAL_VIEW, true);
        addHyperlink(group, COMMAND_IDS.NEW_MODEL_WS, true);
        addHyperlink(group, COMMAND_IDS.NEW_MODEL_XML_DOC, true);
        
        addHyperlink(group, COMMAND_IDS.NEW_MODEL_MED, false);
        
        return panel;
	}
	
	public void aspectChanged(String aspectId) {
		if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.MODEL_PROJECT_MANAGEMENT) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.MODEL_PROJECT_MANAGEMENT);
		} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.MANAGE_VDBS) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.MANAGE_VDBS);
		} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.MODEL_DATA_SOURCES) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.MODEL_DATA_SOURCES);
		//} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.MODEL_VIEWS) ) {
		//	this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.MODEL_VIEWS);
		} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.MANAGE_CONNECTIONS) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.MANAGE_CONNECTIONS);
		} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.CREATE_SOAP_WS) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.CREATE_SOAP_WS);
		} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.CREATE_REST_WS) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.CREATE_REST_WS);
		} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.CONSUME_SOAP_WS) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.CONSUME_SOAP_WS);
		} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.CONSUME_REST_WS) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.CONSUME_REST_WS);
		} else if( aspectId.equalsIgnoreCase(MODELING_ASPECT_LABELS.DEFINE_MODELS) ) {
			this.stackLayout.topControl = stackedPanels.get(MODELING_ASPECT_IDS.DEFINE_MODELS);
		} else {
			// Product Management
		}
		this.stackBodyPanel.layout();
		
	}
	
	private void addHyperlink(Composite parent, String actionId, boolean isSubMenu) {
		AdvisorActionInfo info = AdvisorActionFactory.getActionInfo(actionId);
		if( info != null ) {
			String text = isSubMenu ? info.getShortDisplayName() : info.getDisplayName();
			addHyperlink(parent, text, actionId);
		}
	}
	
	private void addHyperlink(Composite parent, String text, String actionId) {
		Label imageLabel = new Label(parent, SWT.NONE);
		imageLabel.setImage(AdvisorUiPlugin.getDefault().getImage(Images.NEW_MODEL_ACTION));
		Hyperlink link = this.toolkit.createHyperlink(parent, text, SWT.WRAP);
		this.toolkit.adapt(link, true, true);

		// create link action
		final IAction action = new LinkAction(actionId);
		link.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent theEvent) {
				action.run();
			}
		});
	}

	/**
	 * @return section
	 */
	public Section getSection() {
		return section;
	}

	private class LinkAction extends Action {
		String linkId;

		public LinkAction(String id) {
			this.linkId = id;
		}

		@Override
		public void run() {

			AbstractHandler action = AdvisorActionFactory.getActionHandler(linkId);
			if( action != null ) {
				try {
					action.execute(null);
				} catch (ExecutionException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
		}
	}
}