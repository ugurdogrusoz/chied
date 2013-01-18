package org.gvt.action;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.EditPart;
import org.gvt.model.CompoundModel;
import org.gvt.model.GraphObject;
import org.gvt.ChisioMain;

/**
 * Action for removing the highlight from selected objects in the graph.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class RemoveHighlightFromSelectedAction extends Action
{
	CompoundModel root;
	ChisioMain main;

	/**
	 * Constructor
	 */
	public RemoveHighlightFromSelectedAction(ChisioMain main)
	{
		super("Unhighlight Selected");
		this.setToolTipText("Unhighlight Selected");
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/unhighlight.png"));
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
				((GraphObject) model).setHighlight(false);
			}
		}
	}
}
