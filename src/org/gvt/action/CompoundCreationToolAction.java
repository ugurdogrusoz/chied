package org.gvt.action;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.gvt.model.CompoundModel;
import org.gvt.ChisioMain;

/**
 * Action to create a compound node.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CompoundCreationToolAction extends AbstractGEFToolAction
{
	public CompoundCreationToolAction(String text, EditDomain domain)
	{
		super(text, domain);
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/comp-node.gif"));
	}

	protected Tool createTool()
	{
		CreationTool tool = new CreationTool()
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

		// Set cursor
		ImageData image = ImageDescriptor.createFromFile(ChisioMain.class,
			"icon/comp-node-cursor.gif").createImage().getImageData();
		tool.setDefaultCursor(new Cursor(null, image, 0, 0));

		tool.setUnloadWhenFinished(true);
		tool.setFactory(new SimpleFactory(CompoundModel.class));

		return tool;
	}
}