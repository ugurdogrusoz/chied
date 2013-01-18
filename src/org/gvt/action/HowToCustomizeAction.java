package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.gvt.ChisioMain;
import org.gvt.util.SystemBrowserDisplay;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class HowToCustomizeAction extends Action
{
	ChisioMain main;

	public HowToCustomizeAction(ChisioMain main)
	{
		super("How to Customize");

		this.main = main;
	}

	public void run()
	{
		SystemBrowserDisplay.openURL(
			"http://www.cs.bilkent.edu.tr/~ivis/chied/ChiEd-2.0-PG.pdf");
	}
}