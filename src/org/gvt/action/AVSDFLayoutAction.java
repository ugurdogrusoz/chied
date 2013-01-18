package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.command.LayoutCommand;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.model.CompoundModel;
import org.ivis.layout.avsdf.AVSDFLayout;

/** Action for AVSDF layout operation.
 *
 * @author Cihan Kucukkececi
 * @author Selcuk Onur Sumer (modified by)
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class AVSDFLayoutAction extends Action
{
	ChisioMain main = null;

	/**
	 * Constructor
	 */
	public AVSDFLayoutAction(ChisioMain main)
	{
		super("Circular Layout");
		setToolTipText("Circular Layout");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/layout-avsdf.gif"));
		this.main = main;
	}

	public void run()
	{
		CompoundModel root = (CompoundModel)((ChsRootEditPart) main.getViewer().
				getRootEditPart().getChildren().get(0)).getModel();

		LayoutCommand command = new LayoutCommand(main,
			root,
			new AVSDFLayout());
		
		command.execute();
	}
}
