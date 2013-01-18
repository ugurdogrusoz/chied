package org.gvt.action;

import java.util.Collections;

import org.eclipse.jface.action.Action;
import org.eclipse.gef.EditPart;
import org.gvt.ChisioMain;
import org.gvt.editpart.*;

/**
 * Action for selecting the edges in graph.
 * Selection type can be : All edges, intra-edges, inter-edges
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SelectEdgesAction extends Action
{
	ChisioMain main;

	int selectionType;

	/**
	 * Constructor
	 */
	public SelectEdgesAction(ChisioMain main, int selectionType)
	{
		this.selectionType = selectionType;
		this.main = main;

		if (selectionType == SelectEdgesAction.ALL_EDGES)
		{
			setText("All");
			setToolTipText("All");
		}
		else if (selectionType == SelectEdgesAction.INTRA_GRAPH_EDGES)
		{
			setText("Intra-Graph Edges");
			setToolTipText("Intra-Graph Edges");
		}
		else if (selectionType == SelectEdgesAction.INTER_GRAPH_EDGES)
		{
			setText("Inter-Graph Edges");
			setToolTipText("Inter-Graph Edges");
		}
	}

	public void run()
	{
		ChsRootEditPart root = (ChsRootEditPart) main.getViewer().
			getRootEditPart().getChildren().get(0);
		main.getViewer().deselectAll();
		selectEdges(root);
	}

	public void selectEdges(EditPart parent)
	{
		for (int i = 0; i < parent.getChildren().size(); i++)
		{
			ChsNodeEditPart node = (ChsNodeEditPart) parent.getChildren().get(i);

			for (int s = 0; s < node.getSourceConnections().size(); s++)
			{
				ChsEdgeEditPart edge =
					(ChsEdgeEditPart) node.getSourceConnections().get(s);

				if (edge.getEdgeModel().isIntragraph())
				{
					if (selectionType != SelectEdgesAction.INTER_GRAPH_EDGES)
					{
						main.getViewer().appendSelection(edge);
					}
				}
				else
				{
					if (selectionType != SelectEdgesAction.INTRA_GRAPH_EDGES)
					{
						main.getViewer().appendSelection(edge);
					}
				}
			}

			for (int s = 0; s < node.getTargetConnections().size(); s++)
			{
				ChsEdgeEditPart edge =
					(ChsEdgeEditPart) node.getTargetConnections().get(s);

				if (edge.getEdgeModel().isIntragraph())
				{
					if (selectionType != SelectEdgesAction.INTER_GRAPH_EDGES)
					{
						main.getViewer().appendSelection(edge);
					}
				}
				else
				{
					if (selectionType != SelectEdgesAction.INTRA_GRAPH_EDGES)
					{
						main.getViewer().appendSelection(edge);
					}
				}
			}

			if (node.getChildren() != Collections.EMPTY_LIST)
			{
				selectEdges(node);
			}
		}
	}

	public static final int ALL_EDGES = 0;
	public static final int INTRA_GRAPH_EDGES = 1;
	public static final int INTER_GRAPH_EDGES = 2;
}