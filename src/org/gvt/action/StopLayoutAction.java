package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;

/**
 * Action for stopping the layout in progress
 *
 * @author Cihan Kucukkececi
 * @author Selcuk Onur Sumer (modified by)
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class StopLayoutAction extends Action
{
	public StopLayoutAction()
	{
		super("Stop Layout");
		this.setToolTipText("Stop Layout");
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/stop.png"));
	}

	public void run()
	{
		/* TODO: does not seem to work properly! 
		 
		if (AbstractLayout.layoutThread != null)
		{
			AbstractLayout.layoutThread.stop();
		}
		
		*/
	}
}