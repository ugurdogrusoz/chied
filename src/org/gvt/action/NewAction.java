package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.model.CompoundModel;

/**
 * Action for openning a new graph.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class NewAction extends Action
{
	ChisioMain main;

	/**
	 * Constructor
	 */
	public NewAction(String text, ChisioMain chisio)
	{
		super(text);
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/new.gif"));
		setToolTipText(text);
		main = chisio;
	}

	public void run()
	{
		if (LoadAction.saveChangesBeforeDiscard(main))
		{
			CompoundModel root = new CompoundModel();
			root.setAsRoot();
			main.getViewer().setContents(root);
			main.setCurrentFilename(null);
			main.getEditDomain().getCommandStack().flush();
			// reset highlight
			main.getHighlightLayer().removeAll();
			main.getHighlightLayer().highlighted.clear();
			main.getHighlightLayer().clusterHighlights.clear();
		}
	}
}