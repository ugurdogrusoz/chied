package org.gvt.action;

import org.eclipse.gef.*;
import org.gvt.FileMoveTool;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class FileMoveToolAction extends AbstractGEFToolAction
{

	public FileMoveToolAction(String text, EditDomain domain)
	{
		super(text, domain);
		setImageDescriptor(SharedImages.DESC_SELECTION_TOOL_16);
	}

	protected Tool createTool()
	{
		FileMoveTool tool = new FileMoveTool()
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

		return tool;
	}
}