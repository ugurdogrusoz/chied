package org.gvt.inspector;

import java.util.Iterator;

import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.gvt.model.*;
import org.gvt.ChisioMain;
import org.ivis.layout.Cluster;

/**
 * This class maintains the node inspector window.
 * 
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class NodeInspector extends Inspector
{
	 NodeModel node;

	/**
	 * Constructor to open the inspector window
	 */
	private NodeInspector(GraphObject model, String title, ChisioMain main)
	{
		super(model, title, main);

		this.node = (NodeModel) model;

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

		item = addRow(table, "Border Color");
		item.setBackground(1, ((NodeModel) model).getBorderColor());

		item = addRow(table, "Shape");
		item.setText(1, ((NodeModel) model).getShape());

		item = addRow(table, "Cluster ID");
		// set clusters with commas
		String clusterText = "";
		Iterator<Cluster> itr = ((NodeModel) model).getClusters().iterator();
		// if there is no cluster
		if ( !itr.hasNext() )
		{
			item.setText(1, "0");
		}
		else 
		{
			clusterText += itr.next().getClusterID();
			while ( itr.hasNext() )
			{
				clusterText += ", " + itr.next().getClusterID();
			}
			item.setText(1, clusterText);
		}
		
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
			instances.add(new NodeInspector(model, title, main));
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
				NodeModel.DEFAULT_TEXT = item.getText(1);
			}
			else if (item.getText().equals("Text Font"))
			{
				NodeModel.DEFAULT_TEXT_FONT = newFont;
				NodeModel.DEFAULT_TEXT_COLOR = item.getForeground(1);
			}
			else if (item.getText().equals("Color"))
			{
				NodeModel.DEFAULT_COLOR = item.getBackground(1);
			}
			else if (item.getText().equals("Border Color"))
			{
				NodeModel.DEFAULT_BORDER_COLOR = item.getBackground(1);
			}
			else if (item.getText().equals("Shape"))
			{
				NodeModel.DEFAULT_SHAPE = item.getText(1);
			}
		}

		NodeModel.DEFAULT_SIZE = node.getSize();
	}
}