package org.gvt.action;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.EditPart;
import org.gvt.model.*;
import org.gvt.ChisioMain;

/**
 * Action for highlighting the selected objects in the graph.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class HighlightSelectedAction extends Action
{
	CompoundModel root;
	ChisioMain main;

	/**
	 * Constructor
	 */
	public HighlightSelectedAction(ChisioMain main)
	{
		super("Highlight Selected");
		this.setToolTipText("Highlight Selected");
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/highlight.png"));		
		this.main = main;
	}

	public void run()
	{
		ScrollingGraphicalViewer viewer = main.getViewer();
		// Iterates selected objects; for each selected objects, highlights them
		Iterator selectedObjects =
			((IStructuredSelection) viewer.getSelection()).iterator();

		while (selectedObjects.hasNext())
		{
			Object model = ((EditPart)selectedObjects.next()).getModel();

			if (model instanceof GraphObject)
			{
				((GraphObject) model).setHighlightColor(main.higlightColor);
				((GraphObject) model).setHighlight(true);
			}
		}
	}
}