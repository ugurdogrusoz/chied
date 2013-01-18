package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.model.CompoundModel;
import org.gvt.command.LayoutCommand;
import org.gvt.editpart.ChsRootEditPart;
import org.ivis.layout.cise.CiSELayout;

/**
 * Action for CiSE layout operation.
 *
 * @author Cihan Kucukkececi
 * @author Selcuk Onur Sumer (modified by)
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CiSELayoutAction extends Action
{
	ChisioMain main = null;

	/**
	 * Constructor
	 */
	public CiSELayoutAction(ChisioMain main)
	{
		super("CiSE Layout");
		setToolTipText("CiSE Layout");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/layout-cise.gif"));
		this.main = main;
	}

	public void run()
	{
		CompoundModel root = (CompoundModel)((ChsRootEditPart) main.getViewer().
				getRootEditPart().getChildren().get(0)).getModel();

		LayoutCommand command = new LayoutCommand(main,
			root,
			new CiSELayout());
		
		command.execute();
	}
}