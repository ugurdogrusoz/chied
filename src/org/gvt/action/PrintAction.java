package org.gvt.action;

import java.io.*;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import org.eclipse.draw2d.*;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Shell;
import org.gvt.ChisioMain;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.editpart.ChsScalableRootEditPart;
import org.gvt.model.CompoundModel;

/**
 * Action for printing the graph from default printer.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class PrintAction extends Action
{
	ChisioMain main;

	public PrintAction(ChisioMain chisio)
	{
		super("Print Graph");
		setToolTipText("Print Graph");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/print.gif"));
		this.main = chisio;		
	}

	public void run()
	{
		final Shell shell = main.getShell();
		Figure rootFigure;
		Rectangle bounds;

		rootFigure = (Figure) ((ChsScalableRootEditPart) main.getViewer().
			getRootEditPart()).getFigure().getChildren().get(0);
		ScalableLayeredPane layer =
			(ScalableLayeredPane) rootFigure.getChildren().get(0);
		double scale = layer.getScale();
		layer.setScale(1.0);
		bounds = getBounds(main.getViewer(), rootFigure);

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
		rootFigure.paint(graphics);
		graphics.dispose();

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[]{image.getImageData()};

		String tmpfilename = main.getCurrentFilename();
		int ind = tmpfilename.lastIndexOf('.');
		tmpfilename = tmpfilename.substring(0, ind);

		ByteArrayOutputStream result = new ByteArrayOutputStream();
		loader.save(result, SWT.IMAGE_BMP);

		layer.setScale(scale);

		try
		{
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			pras.add(new Copies(1));
			PrintService ps = PrintServiceLookup.lookupDefaultPrintService();

			if (ps == null)
			{
				throw new RuntimeException("No printer services available.");
			}

			System.out.println("Printing to " + ps);

			DocPrintJob job = ps.createPrintJob();
			ByteArrayInputStream bis =
				new ByteArrayInputStream(result.toByteArray());
			Doc doc = new SimpleDoc(bis, DocFlavor.INPUT_STREAM.JPEG, null);
			job.print(doc, pras);
		}
		catch (PrintException pe)
		{
			pe.printStackTrace();
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
}