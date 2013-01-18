package org.gvt.action;

import java.util.Collections;

import org.eclipse.jface.action.Action;
import org.eclipse.gef.EditPart;
import org.gvt.ChisioMain;
import org.gvt.editpart.ChsRootEditPart;

/**
 * Action for selecting the nodes in graph.
 * Selection type can be : All nodes, simple nodes, compound nodes
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SelectNodesAction extends Action
{
	ChisioMain main;

	int selectionType;

	/**
	 * Constructor
	 */
	public SelectNodesAction(ChisioMain main, int selectionType)
	{
		this.selectionType = selectionType;
		this.main = main;

		if (selectionType == ALL_NODES)
		{
			setText("All");
			setToolTipText("All");
		}
		else if (selectionType == SIMPLE_NODES)
		{
			setText("Simple Nodes");
			setToolTipText("Simple Nodes");
		}
		else if (selectionType == COMPOUND_NODES)
		{
			setText("Compound Nodes");
			setToolTipText("Compound Nodes");
		}
	}

	public void run()
	{
		ChsRootEditPart root = (ChsRootEditPart) main.getViewer().
			getRootEditPart().getChildren().get(0);
		main.getViewer().deselectAll();
		selectNodes(root);
	}

	public void selectNodes(EditPart parent)
	{
		for (int i = 0; i < parent.getChildren().size(); i++)
		{
			EditPart node = (EditPart) parent.getChildren().get(i);

			if (node.getChildren() != Collections.EMPTY_LIST)
			{
				if (selectionType != SIMPLE_NODES)
				{
					main.getViewer().appendSelection(node);
				}

				selectNodes(node);
			}
			else
			{
				if (selectionType != COMPOUND_NODES)
				{
					main.getViewer().appendSelection(node);
				}
			}
		}
	}

	public static final int ALL_NODES = 0;
	public static final int SIMPLE_NODES = 1;
	public static final int COMPOUND_NODES = 2;
}