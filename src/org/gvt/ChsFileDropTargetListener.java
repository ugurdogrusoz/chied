package org.gvt;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.dnd.FileTransfer;
import org.gvt.action.LoadAction;

/**
 * This class handles the file dropping and loads the dropped file.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ChsFileDropTargetListener extends AbstractTransferDropTargetListener
{
	private ChisioMain main;

	public ChsFileDropTargetListener(EditPartViewer viewer, ChisioMain main)
	{
		super(viewer, FileTransfer.getInstance());
		this.main = main;
	}

	protected Request createTargetRequest()
	{
		CreateRequest request = new CreateRequest();
		return request;
	}

	protected void handleDrop()
	{
		String path = ((String[]) getCurrentEvent().data)[0];
		new LoadAction(main, path).run();
	}

	protected void updateTargetRequest()
	{
		 ((CreateRequest) getTargetRequest()).setLocation(getDropLocation());
	}
}