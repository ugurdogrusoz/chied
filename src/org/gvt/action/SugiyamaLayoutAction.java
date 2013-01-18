package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.command.LayoutCommand;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.model.CompoundModel;
import org.ivis.layout.sgym.SgymLayout;

/**
 * Action for Sugiyama Layout operation.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SugiyamaLayoutAction extends Action
{
	ChisioMain main = null;

	/**
	 * Constructor
	 */
	public SugiyamaLayoutAction(ChisioMain main)
	{
		setText("Sugiyama Layout");
		setToolTipText("Sugiyama Layout");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/layout-sgym.gif"));
		this.main = main;
	}

	public void run()
	{
		CompoundModel root = (CompoundModel)((ChsRootEditPart) main.getViewer().
				getRootEditPart().getChildren().get(0)).getModel();

		LayoutCommand command = new LayoutCommand(main,
			root,
			new SgymLayout());
		
		command.execute();
	}
}