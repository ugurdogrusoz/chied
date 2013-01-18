package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.inspector.LayoutInspector;

/**
 * This class creates the action for opening layout properties window.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class LayoutInspectorAction extends Action
{
	ChisioMain main;
	private static LayoutInspector layoutInspector = null;

	/**
	 * Constructor
	 */
	public LayoutInspectorAction(ChisioMain main)
	{
		super("Layout Properties");
		this.main = main;
		this.setImageDescriptor(ImageDescriptor.createFromFile(
				ChisioMain.class,
				"icon/blank.gif"));
		this.setToolTipText("Layout Properties");
	}

	public LayoutInspector getLayoutInspector()
	{
		if (LayoutInspectorAction.layoutInspector == null)
		{
			LayoutInspectorAction.layoutInspector = new LayoutInspector(this.main);

			// Make any changes to default layout options here
//			LayoutOptionsPack layoutOptionsPack = LayoutOptionsPack.getInstance();
		}

		return LayoutInspectorAction.layoutInspector;
	}

	public void run()
	{
		this.getLayoutInspector().open();
	}
}
