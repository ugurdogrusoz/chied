package org.gvt;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.*;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.gvt.action.*;
import org.gvt.editpart.ChsEdgeEditPart;

/**
 * This class maintains Popup Menus creation.
 * 
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class PopupManager extends MenuManager
{
	// coordinates of the point that mouse was clicked
	private Point clickLocation;

	private ChisioMain main;

	/**
	 * Constructor
	 * 
	 * @param main
	 */
	public PopupManager(ChisioMain main)
	{
		this.main = main;
	}

	/**
	 * Creates the popup menus according to clicked object
	 * 
	 * @param manager
	 */
	public void createActions(IMenuManager manager)
	{
		EditPart ep = main.getViewer().findObjectAt(clickLocation);

		if (ep instanceof RootEditPart)
		{
			// GRAPH POPUP
			manager.add(new ZoomAction(main, 1, clickLocation));
			manager.add(new ZoomAction(main, -1, clickLocation));
			manager.add(new RemoveHighlightsAction(main));
			manager.add(new LayoutInspectorAction(main));
			manager.add(new InspectorAction(main, true));
			main.getViewer().select(ep);
		}
		else if (ep instanceof NodeEditPart)
		{
			// NODE-COMPOUND POPUP
			manager.add(new CreateCompoundFromSelectedAction(main));
			manager.add(new HighlightSelectedAction(main));
			manager.add(new RemoveHighlightFromSelectedAction(main));
			manager.add(new DeleteAction(main.getViewer()));
			manager.add(new InspectorAction(main, false));
		}
		else if (ep instanceof ChsEdgeEditPart)
		{
			// EDGE POPUP
			manager.add(new HighlightSelectedAction(main));
			manager.add(new RemoveHighlightFromSelectedAction(main));
			manager.add(new DeleteAction(main.getViewer()));
			manager.add(new InspectorAction(main, false));
		}
	}

	/**
	 * Setter method
	 * 
	 * @param clickLocation
	 */
	public void setClickLocation(Point clickLocation)
	{
		this.clickLocation = clickLocation;
	}
}
