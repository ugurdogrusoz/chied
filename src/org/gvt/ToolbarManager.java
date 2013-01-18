package org.gvt;

import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.gvt.action.AVSDFLayoutAction;
import org.gvt.action.CiSELayoutAction;
import org.gvt.action.ClusterLayoutAction;
import org.gvt.action.CoSELayoutAction;
import org.gvt.action.CompoundCreationToolAction;
import org.gvt.action.ConnectionCreationAction;
import org.gvt.action.DeleteAction;
import org.gvt.action.LoadAction;
import org.gvt.action.MarqueeZoomToolAction;
import org.gvt.action.NewAction;
import org.gvt.action.NodeCreationToolAction;
import org.gvt.action.PrintAction;
import org.gvt.action.RemoveCompoundAction;
import org.gvt.action.SaveAction;
import org.gvt.action.SelectionToolAction;
import org.gvt.action.SpringLayoutAction;
import org.gvt.action.StopLayoutAction;
import org.gvt.action.SugiyamaLayoutAction;
import org.gvt.action.ZoomAction;

/**
 * This class implements the Chisio toolbar.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ToolbarManager extends ToolBarManager
{
	public ToolbarManager(int style, ChisioMain main)
	{
		super(style);
		this.add(new NewAction("New", main));
		this.add(new LoadAction(main));
		this.add(new SaveAction(main));
		this.add(new PrintAction(main));
		this.add(new Separator());
		this.add(new SelectionToolAction("Select Tool",
			main.getEditDomain()));
		this.add(new MarqueeZoomToolAction("Marquee Zoom Tool",
			main.getEditDomain()));
		this.add(new Separator());
		this.add(new ZoomAction(main, -1 , null));
		this.add(new ZoomAction(main, +1 , null));
		this.add(new ZoomAction(main, 0 , null));
		this.add(new Separator());
		this.add(new NodeCreationToolAction("Create Simple Node",
			main.getEditDomain()));
		this.add(new CompoundCreationToolAction("Create Compound Node",
			main.getEditDomain()));
		this.add(new ConnectionCreationAction("Create Edge",
			main.getEditDomain()));
		this.add(new Separator());
		this.add(new RemoveCompoundAction(main));
		this.add(new DeleteAction(main));
		this.add(new Separator());
		this.add(new CoSELayoutAction(main));
		this.add(new ClusterLayoutAction(main));
		this.add(new CiSELayoutAction(main));
		this.add(new AVSDFLayoutAction(main));
		this.add(new SpringLayoutAction(main));
		this.add(new SugiyamaLayoutAction(main));
		this.add(new Separator());
		this.add(new StopLayoutAction());

		this.update(true);
	}
}