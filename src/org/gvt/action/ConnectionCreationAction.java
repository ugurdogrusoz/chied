package org.gvt.action;

import java.io.ByteArrayInputStream;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.SWT;
import org.gvt.model.EdgeModel;
import org.gvt.ChisioMain;

/**
 * Action to create an edge.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ConnectionCreationAction extends AbstractGEFToolAction
{
	public ConnectionCreationAction(String text, EditDomain domain)
	{
		super(text, domain);
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/edge.gif"));
	}

	protected Tool createTool()
	{
		ConnectionCreationTool tool = new ConnectionCreationTool()
		{
			public void activate()
			{
				setChecked(true);
				super.activate();
			}

			public void deactivate()
			{
				setCursor(new Cursor(null, SWT.CURSOR_HAND));
				setChecked(false);
				super.deactivate();
			}
		};

		// Set cursors
		ImageData image = ImageDescriptor.createFromFile(ChisioMain.class,
			"icon/edge-cursor.gif").createImage().getImageData();
		tool.setDisabledCursor(new Cursor(null, image, 0, 0));
		tool.setDefaultCursor(new Cursor(null, SWT.CURSOR_CROSS));

		tool.setUnloadWhenFinished(true);
		tool.setFactory(new SimpleFactory(EdgeModel.class));

		return tool;
	}
}