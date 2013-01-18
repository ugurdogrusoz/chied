package org.gvt.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.draw2d.*;
import org.gvt.ChisioMain;
import org.gvt.model.CompoundModel;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.editpart.ChsScalableRootEditPart;

/**
 * Action for saving the graph or view as an image.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SaveAsImageAction extends Action
{
	ChisioMain main;
	boolean saveWholeGraph;

	public SaveAsImageAction(ChisioMain chisio, boolean saveGraph)
	{
		this.main = chisio;
		this.saveWholeGraph = saveGraph;

		if (saveWholeGraph)
		{
			setText("Save Graph As Image");
			setToolTipText("Save Graph As Image");
		}
		else
		{
			setText("Save View As Image");
			setToolTipText("Save View As Image");
		}
	}

	public void run()
	{
		final Shell shell = main.getShell();
		Figure rootFigure;
		Rectangle bounds;
		double scale = 1.0;
		ScalableLayeredPane layer = null;

		rootFigure = (Figure) ((ChsScalableRootEditPart) main.getViewer().
			getRootEditPart()).getFigure();

		if (!saveWholeGraph)
		{
			bounds = getBounds(rootFigure);
		}
		else
		{
			rootFigure = (Figure) rootFigure.getChildren().get(0);
			layer = (ScalableLayeredPane)rootFigure.getChildren().get(0);
			scale = layer.getScale();
			layer.setScale(1.0);
			bounds = getBounds(main.getViewer(), rootFigure);
		}

		final Image image = new Image(shell.getDisplay(), bounds);

		GC gc = new GC(image);
		
		if (ChisioMain.runningOnWindows)
		{
			gc.setAntialias(SWT.ON);
			gc.setTextAntialias(SWT.ON);
		}
		else
		{
			gc.setAntialias(SWT.OFF);
			gc.setTextAntialias(SWT.OFF);
		}
		
		Graphics graphics = new SWTGraphics(gc);


	//	Point p = new Point(-50, -50);
	//	graphics.translate(p);

		rootFigure.paint(graphics);
		graphics.dispose();

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[]{image.getImageData()};
		// Get the user to choose a file name and type to save.
		FileDialog fileChooser = new FileDialog(main.getShell(), SWT.SAVE);
		String tmpfilename = main.getCurrentFilename();
		int ind = tmpfilename.lastIndexOf('.');
		tmpfilename = tmpfilename.substring(0, ind);

		fileChooser.setFileName(tmpfilename);
		fileChooser.setFilterExtensions(new String[]{"*.bmp", "*.jpeg"});
		fileChooser.setFilterNames(
			new String[]{"BMP (*.bmp)", "JPEG (*.jpeg)"});
		String filename = fileChooser.open();

		if (filename == null)
		{
			return;
		}

		if (filename.contains(".bmp"))
		{
			loader.save(filename, SWT.IMAGE_BMP);
		}
		else
		{
			loader.save(filename, SWT.IMAGE_JPEG);
		}

		if (saveWholeGraph)
		{
			layer.setScale(scale);
		}
	}

	public Rectangle getBounds(ScrollingGraphicalViewer viewer, Figure f)
	{
		CompoundModel rootModel = (CompoundModel) ((ChsRootEditPart) viewer.
			getRootEditPart().getChildren().get(0)).getModel();

		org.eclipse.draw2d.geometry.Rectangle bounds
			= rootModel.calculateBounds();
		org.eclipse.draw2d.geometry.Rectangle boundsRoot = f.getBounds();

		boundsRoot.setSize(bounds.x + bounds.width + CompoundModel.MARGIN_SIZE,
			bounds.y + bounds.height + CompoundModel.MARGIN_SIZE);

		return new Rectangle(boundsRoot.x,
			boundsRoot.y,
			boundsRoot.width,
			boundsRoot.height);
	}

	public Rectangle getBounds(Figure f)
	{
		org.eclipse.draw2d.geometry.Rectangle bounds = f.getBounds();

		return new Rectangle(bounds.x ,
			bounds.y ,
			bounds.width ,
			bounds.height);
	}
}
