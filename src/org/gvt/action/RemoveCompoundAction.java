package org.gvt.action;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.EditPart;
import org.gvt.model.CompoundModel;
import org.gvt.ChisioMain;
import org.gvt.command.*;
import org.gvt.editpart.*;

/**
 * Action for deleting the compound node without deleting the inner nodes.
 * Children are kept, they are taken out from compound node and stay in the same
 * absolute location.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class RemoveCompoundAction extends Action
{
	CompoundModel root;
	ChisioMain window = null;
	ChisioMain main;

	/**
	 * Constructor
	 */
	public RemoveCompoundAction(ChisioMain main)
	{
		super("Remove Compound");
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/delete-comp.gif"));
		this.setToolTipText("Remove Compound");
		this.main = main;
	}

	public void run()
	{
		ScrollingGraphicalViewer viewer = main.getViewer();

		// Iterates selected objects; for each of selected objects, delete
		// command is executed
		Iterator selectedObjects =
			((IStructuredSelection) viewer.getSelection()).iterator();

		while (selectedObjects.hasNext())
		{
			EditPart childEditPart = (EditPart) selectedObjects.next();

			// if selected one is a node or compound delete is called
			if (childEditPart instanceof ChsCompoundEditPart)
			{
				RemoveCompoundCommand command = new RemoveCompoundCommand();
				command.setCompound((CompoundModel) childEditPart.getModel());
				command.execute();
			}
		}
	}
}