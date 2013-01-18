package org.gvt.inspector;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Point;
import org.gvt.model.GraphObject;
import org.gvt.model.CompoundModel;
import org.gvt.ChisioMain;

/**
 * This class maintains the graph inspector window.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class GraphInspector extends Inspector
{
	/**
	 * Constructor to open the inspector window
	 */
	private GraphInspector(GraphObject model, String title, ChisioMain main)
	{
		super(model, title, main);

		TableItem item = addRow(table, "Margin");
		item.setText(1, String.valueOf(CompoundModel.MARGIN_SIZE));
	
		item = addRow(table, "Highlight Color");
		item.setBackground(1, ChisioMain.higlightColor);

		createContents(shell);

		// Display it in the middle
		Point loc = shell.getParent().getShell().getLocation();
		Point size = shell.getParent().getShell().getSize();
		Point s = shell.getSize();
		shell.setLocation(size.x/2 + loc.x - s.x/2, size.y/2 + loc.y -s.y/2);
		shell.open();
	}

	public void setAsDefault()
	{
		// Not enabled for this inspector
	}

	public static void getInstance(GraphObject model,
		String title,
		ChisioMain main)
	{
		if (isSingle(model))
		{
			instances.add(new GraphInspector(model, title, main));
		}
	}
}