package org.gvt.action;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.gvt.command.DeleteCommand;
import org.gvt.command.DeleteConnectionCommand;
import org.gvt.editpart.*;
import org.gvt.model.*;
import org.gvt.ChisioMain;

/**
 * This class maintains action for deleting the graph objects.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class DeleteAction extends Action
{
	ScrollingGraphicalViewer viewer;

	private ChisioMain main;

	/**
	 * Constructor
	 */
	public DeleteAction(ScrollingGraphicalViewer view)
	{
		super("Delete");
		setToolTipText("Delete");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/delete.gif"));
		this.viewer = view;
	}

	public DeleteAction(ChisioMain main)
	{
		super("Delete Selected");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/delete.gif"));
		this.main = main;
		setToolTipText("Delete Selected");
	}

	public void run()
	{
		if(main != null)
		{
			viewer = main.getViewer();
		}
		// Iterates the selected objects to delete
		Iterator selectedObjects = ((IStructuredSelection) viewer
			.getSelection()).iterator();
		// for each of seleceted objects, delete command is executed
		while (selectedObjects.hasNext())
		{
			EditPart childEditPart = (EditPart) selectedObjects.next();
			// if selected one is a node or compound DeleteCommand is called
			if (childEditPart instanceof ChsNodeEditPart)
			{
				NodeModel node = (NodeModel) childEditPart.getModel();
				node.resetClusters();
				DeleteCommand command = new DeleteCommand();
				command.setChild(node);
				EditPart parent = childEditPart.getParent();
				command.setParent((CompoundModel) parent.getModel());
				command.execute();
			}
			// else if it is an edge, DeleteConnectionCommand is called
			else if (childEditPart instanceof ChsEdgeEditPart)
			{
				{
					DeleteConnectionCommand command
						= new DeleteConnectionCommand();
					command.setConnectionModel(childEditPart.getModel());
					command.execute();
				}

			}
		}
	}
}