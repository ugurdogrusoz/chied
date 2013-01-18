package org.gvt.action;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.gef.EditPart;
import org.gvt.ChisioMain;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.model.*;

/**
 * Action for removing the highlight from all objects in the graph.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class RemoveHighlightsAction extends Action
{
	ChisioMain main;

	/**
	 * Constructor
	 */
	public RemoveHighlightsAction(ChisioMain main)
	{
		super("Unhighlight All");
		this.setToolTipText("Unhighlight All");
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/unhighlight-all.png"));
		this.main = main;
	}

	public void run()
	{
		CompoundModel root = (CompoundModel) ((ChsRootEditPart)main.getViewer().
			getRootEditPart().getChildren().get(0)).getModel();

		Iterator<NodeModel> nodeIter = root.getNodes().iterator();

		while (nodeIter.hasNext())
		{
			NodeModel node = nodeIter.next();
			node.setHighlight(false);

			List<EdgeModel> edges = node.getSourceConnections();

			for (int i = 0; i < edges.size(); i++)
			{
				edges.get(i).setHighlight(false);
			}
		}
	}
}
