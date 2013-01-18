package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.model.CompoundModel;
import org.gvt.command.LayoutCommand;
import org.gvt.editpart.ChsRootEditPart;
import org.ivis.layout.cose.CoSELayout;

/**
 * Action for CoSE layout operation.
 *
 * @author Cihan Kucukkececi
 * @author Selcuk Onur Sumer (modified by)
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CoSELayoutAction extends Action
{
	ChisioMain main = null;

	/**
	 * Constructor
	 */
	public CoSELayoutAction(ChisioMain main)
	{
		super("CoSE Layout");
		setToolTipText("CoSE Layout");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/layout-cose.gif"));
		this.main = main;
	}

	public void run()
	{
		CompoundModel root = (CompoundModel)((ChsRootEditPart) main.getViewer().
				getRootEditPart().getChildren().get(0)).getModel();
		
		LayoutCommand command = new LayoutCommand(main,
			root,
			new CoSELayout());
		
		command.execute();
	}
}