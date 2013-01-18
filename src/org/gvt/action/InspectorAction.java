package org.gvt.action;

import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.inspector.CompoundInspector;
import org.gvt.inspector.EdgeInspector;
import org.gvt.inspector.GraphInspector;
import org.gvt.inspector.NodeInspector;
import org.gvt.model.GraphObject;
import org.gvt.model.CompoundModel;
import org.gvt.model.EdgeModel;
import org.gvt.model.NodeModel;
import org.gvt.ChisioMain;

/**
 * This class maintains the Action of inspector window. For edges, nodes,
 * compound nodes and graphs, different inspectors are opened.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class InspectorAction extends Action
{
	ChisioMain main;

	/**
	 * Constructor
	 */
	public InspectorAction(ChisioMain main, boolean isGraph)
	{
		if (isGraph)
		{
			setText("Properties");
			setToolTipText("Properties");
		}
		else
		{
			setText("Object Properties");
			setToolTipText("Object Properties");
		}

		this.main = main;
	}

	public void run()
	{
		// get the object that user wants to open inspector for.
		Object obj = ((IStructuredSelection) main.getViewer().getSelection()).
			getFirstElement();

		if (!(obj instanceof ScalableRootEditPart))
		{
			GraphObject model =
				(GraphObject) ((AbstractEditPart) obj).getModel();

			if (model instanceof EdgeModel)
			{
				EdgeInspector.getInstance(model, "Edge", main);
			}
			else if (model instanceof CompoundModel)
			{
				if (!((CompoundModel)model).isRoot())
				{
					CompoundInspector.getInstance(model, "Compound", main);
				}
			}
			else if (model instanceof NodeModel)
			{
				NodeInspector.getInstance(model, "Node", main);
			}
		}
		else
		{
			ChsRootEditPart edit = (ChsRootEditPart)
				((ScalableRootEditPart) obj).getChildren().get(0);

			GraphInspector.
				getInstance((GraphObject) edit.getModel(), "Graph", main);
		}
	}
}