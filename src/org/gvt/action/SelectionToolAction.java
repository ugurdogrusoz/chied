package org.gvt.action;

import org.eclipse.gef.*;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;

/**
 * Tool to select and manipulate figures.
 * A selection tool is in one of three states, e.g., background selection,
 * figure selection, handle manipulation.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SelectionToolAction extends AbstractGEFToolAction
{

	public SelectionToolAction(String text, EditDomain domain)
	{
		super(text, domain);
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/select.gif"));
	}

	protected Tool createTool()
	{
		SelectionTool tool = new SelectionTool()
		{
			public void activate()
			{
				setChecked(true);
				super.activate();
			}

			public void deactivate()
			{
				setChecked(false);
				super.deactivate();
			}
		};

		editDomain.setDefaultTool(tool);
		editDomain.setActiveTool(tool);

		return tool;
	}
}