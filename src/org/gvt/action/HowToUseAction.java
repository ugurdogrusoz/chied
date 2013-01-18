package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.gvt.ChisioMain;
import org.gvt.util.SystemBrowserDisplay;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class HowToUseAction extends Action
{
	ChisioMain main;

	public HowToUseAction(ChisioMain main)
	{
		super("How to Use");

		this.main = main;
	}

	public void run()
	{
		SystemBrowserDisplay.openURL(
			"http://www.cs.bilkent.edu.tr/~ivis/chied/ChiEd-2.0-UG.pdf");
	}
}