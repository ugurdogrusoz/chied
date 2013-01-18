package org.gvt.inspector;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Font;
import org.gvt.model.*;
import org.gvt.ChisioMain;

/**
 * This class maintains the edge inspector window.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class EdgeInspector extends Inspector
{
	/**
	 * Constructor to open the inspector window
	 */
	private EdgeInspector(GraphObject model, String title, ChisioMain main)
	{
		super(model, title, main);

		TableItem item = addRow(table, "Name");
		item.setText(1, model.getText());

		item = addRow(table, "Text Font");
		Font font = model.getTextFont();
		String fontName = font.getFontData()[0].getName();
		int fontSize = font.getFontData()[0].getHeight();
		int fontStyle = font.getFontData()[0].getStyle();

		if (fontSize > 14)
		{
			fontSize = 14;
		}

		item.setText(1, fontName);
		item.setFont(1,	new Font(null, fontName, fontSize, fontStyle));
		item.setForeground(1, model.getTextColor());

		item = addRow(table, "Color");
		item.setBackground(1, model.getColor());

		item = addRow(table, "Style");
		item.setText(1, ((EdgeModel) model).getStyle());

		item = addRow(table, "Arrow");
		item.setText(1, ((EdgeModel) model).getArrow());

		item = addRow(table, "Width");
		item.setText(1, "" + ((EdgeModel) model).getWidth());

		createContents(shell);

		shell.setLocation(calculateInspectorLocation(main.clickLocation.x,
			main.clickLocation.y));
		shell.open();
	}

	public static void getInstance(GraphObject model,
		String title,
		ChisioMain main)
	{
		if (isSingle(model))
		{
			instances.add(new EdgeInspector(model, title, main));
		}
	}

	/**
	 * Default parameters for creation is changed with the current parameters.
	 */
	public void setAsDefault()
	{
		TableItem[] items = table.getItems();

		for (TableItem item : items)
		{
			if (item.getText().equals("Name"))
			{
				EdgeModel.DEFAULT_TEXT = item.getText(1);
			}
			else if (item.getText().equals("Text Font"))
			{
				EdgeModel.DEFAULT_TEXT_FONT = newFont;
				EdgeModel.DEFAULT_TEXT_COLOR = item.getForeground(1);
			}
			else if (item.getText().equals("Color"))
			{
				EdgeModel.DEFAULT_COLOR = item.getBackground(1);
			}
			else if (item.getText().equals("Style"))
			{
				EdgeModel.DEFAULT_STYLE = item.getText(1);
			}
			else if (item.getText().equals("Arrow"))
			{
				EdgeModel.DEFAULT_ARROW = item.getText(1);
			}
			else if (item.getText().equals("Width"))
			{
				EdgeModel.DEFAULT_WIDTH = Integer.parseInt(item.getText(1));
			}
		}
	}
}