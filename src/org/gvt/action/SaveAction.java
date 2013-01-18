package org.gvt.action;

import java.io.*;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.gvt.ChisioMain;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.model.CompoundModel;
import org.gvt.util.GraphMLWriter;
import org.gvt.util.SimpleGraphWriter;
import org.gvt.util.XMLWriter;

/**
 * This class saves the visualized graph to an XML file which is GraphML based.
 * It is called from Menubar/File/Save item.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SaveAction extends Action
{
	ChisioMain main;

	public boolean isSaved = true;

	/**
	 * Constructor
	 *
	 * @param chisio
	 */
	public SaveAction(ChisioMain chisio)
	{
		super("Save");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/save.gif"));
		setToolTipText("Save");
		this.main = chisio;
	}

	public void run()
	{
		String fileName = null;
		boolean done = false;

		while (!done)
		{
			// Get the user to choose a file name and type to save.
			FileDialog fileChooser = new FileDialog(main.getShell(), SWT.SAVE);
			fileChooser.setFileName(main.getCurrentFilename());
			fileChooser.setFilterExtensions(new String[]{"*.xml", "*.graphml", "*.txt"});
			fileChooser.setFilterNames(
				new String[]{"XML (*.xml)", "GRAPHML (*.graphml)", "SIMPLE (*.txt)"});
			fileName = fileChooser.open();

			if (fileName == null)
			{
				// User has cancelled, so quit and return
				done = true;
			}
			else
			{
				// User has selected a file; see if it already exists
				File file = new File(fileName);

				if (file.exists())
				{
					// The file already exists; asks for confirmation
					MessageBox mb = new MessageBox(
						fileChooser.getParent(),
						SWT.ICON_WARNING | SWT.YES | SWT.NO);

					// We really should read this string from a
					// resource bundle
					mb.setMessage(fileName +
						" already exists. Do you want to replace it?");
					mb.setText("Confirm Replace File");
					// If they click Yes, we're done and we drop out. If
					// they click No, we redisplay the File Dialog
					done = mb.open() == SWT.YES;
				}
				else
				{
					// File does not exist, so drop out
					done = true;
				}
			}
		}

		if (fileName == null)
		{
			isSaved = false;
			return;
		}

		try
		{
			String ext = fileName.substring( fileName.lastIndexOf('.') + 1);
			
			// According to the extension choose save format
			if (ext.equalsIgnoreCase("graphml") || ext.equalsIgnoreCase("xml"))
			{
				// get the root of graph
				CompoundModel root = (CompoundModel)
					((ChsRootEditPart) main.getViewer().getRootEditPart().
						getChildren().get(0)).getModel();

				BufferedWriter xmlFile =
					new BufferedWriter(new FileWriter(fileName));
				XMLWriter writer = new GraphMLWriter();
				xmlFile.write(writer.writeXMLFile(root).toString());
				xmlFile.close();

				main.setCurrentFilename(fileName);
				// mark save location in commandstack
				main.getEditDomain().getCommandStack().markSaveLocation();
			}
			else if (ext.equalsIgnoreCase("txt"))
			{
				// get the root of graph
				CompoundModel root = (CompoundModel)
					((ChsRootEditPart) main.getViewer().getRootEditPart().
						getChildren().get(0)).getModel();
				
				SimpleGraphWriter simpleWriter = new SimpleGraphWriter();
				simpleWriter.writeSimpleGraphFile(root, fileName);
				
				main.setCurrentFilename(fileName);
				// mark save location in commandstack
				main.getEditDomain().getCommandStack().markSaveLocation();
			}
			
		}
		catch (Exception e)
		{
			// e.printStackTrace();
			MessageBox messageBox = new MessageBox(
				main.getShell(),
				SWT.ERROR_UNSUPPORTED_FORMAT);
			messageBox.setMessage("File cannot be saved!");
			messageBox.setText("Chisio");
			messageBox.open();
		}
	}
}