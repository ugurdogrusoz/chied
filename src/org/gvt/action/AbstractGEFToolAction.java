package org.gvt.action;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Tool;
import org.eclipse.jface.action.Action;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public abstract class AbstractGEFToolAction extends Action
{
	protected EditDomain editDomain;

	private Tool tool;

	public AbstractGEFToolAction(String text, EditDomain domain)
	{
		super(text, AS_CHECK_BOX);
		setToolTipText(text);
		editDomain = domain;
		tool = createTool();
	}

	abstract protected Tool createTool();

	public void run()
	{
		editDomain.setActiveTool(tool);
	}
}