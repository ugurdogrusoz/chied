package org.gvt.action;

import java.io.File;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.gvt.ChisioMain;
import org.gvt.model.CompoundModel;
import org.gvt.util.GraphMLReader;
import org.gvt.util.SimpleGraphReader;
import org.gvt.util.XMLReader;
import org.ivis.layout.Cluster;

/**
 * This class loads an XML file which is Graphml based and visualizes the graph.
 * It is called from Menubar/File/Open item.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class LoadAction extends Action
{
	ChisioMain main;

	String filename;

	/**
	 * Determines the default location in load operation and remembers
	 * the last used directory.
	 */
	static String lastLocation = "graphs";

	/**
	 * Constructor without filename. opens an FileChooser for filename
	 *
	 * @param chisio
	 */
	public LoadAction(ChisioMain chisio)
	{
		super("Open");
		setToolTipText("Open");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/open.gif"));

		this.main = chisio;
	}

	/**
	 * Constructor with filename. opens the xml with file with the given
	 * filename.
	 *
	 * @param chisio
	 * @param filename
	 */
	public LoadAction(ChisioMain chisio, String filename)
	{
		this(chisio);
		this.filename = filename;
	}

	/**
	 * Hands unsaved changes before the text is discarded.
	 *
	 * @return whether furthur action should be carried on.
	 */
	public static boolean saveChangesBeforeDiscard(ChisioMain main)
	{
		if (main.getEditDomain().getCommandStack().isDirty())
		{
			MessageBox messageBox = new MessageBox(
				main.getShell(),
				SWT.ICON_WARNING | SWT.YES | SWT.NO | SWT.CANCEL);
			messageBox.setMessage("Do you want to save the changes to a file?");
			messageBox.setText("Chisio");
			int ret = messageBox.open();

			if (ret == SWT.YES)
			{
				SaveAction save = new SaveAction(main);
				save.run();

				return save.isSaved;
			}
			else if (ret == SWT.NO)
			{
				return true;
			}

			return false;
		}

		return true;
	}

	/**
	 * opens a FileChooser for loading an xml file
	 *
	 * @return chosen filename
	 */
	public String openFileChooser()
	{
		// choose an input file.
		FileDialog fileChooser = new FileDialog(main.getShell(), SWT.OPEN);
		fileChooser.setFilterExtensions(new String[]{
			"*.xml; *.graphml; *.txt;", "*.xml", "*.graphml", "*.txt"});
		fileChooser.setFilterNames(new String[]{
			"All" + " (xml, graphml, txt)", "XML (*.xml)",
			"GRAPHML (*.graphml)", "SIMPLE (*.txt)"});

		// Set the directory to the last used or the default
		fileChooser.setFilterPath(lastLocation);

		String filename = fileChooser.open();

		// Remember the location for later use

		if (filename != null)
		{
			String sep = filename.contains("/") ? "/" :
				filename.contains("\\") ? "\\" : null;

			if (sep != null)
			{
				lastLocation = filename.substring(0, filename.lastIndexOf(sep));
			}
		}

		return filename;
	}

	public void run()
	{
		if (saveChangesBeforeDiscard(main))
		{
			if (filename == null)
			{
				filename = openFileChooser();

				if (filename == null)
				{
					return;
				}
			}

			// reset highlight
			main.getHighlightLayer().removeAll();
			main.getHighlightLayer().highlighted.clear();
			main.getHighlightLayer().clusterHighlights.clear();

			File xmlfile = new File(filename);
			
			String ext = filename.substring( filename.lastIndexOf('.') + 1);
			
			CompoundModel root = null;
			
			// According to the extension choose load format
			if( ext.equalsIgnoreCase("graphml") || ext.equalsIgnoreCase("xml"))
			{
				XMLReader reader = new GraphMLReader();
				root = reader.readXMLFile(xmlfile);
			}
			else if( ext.equalsIgnoreCase("txt"))
			{
				SimpleGraphReader reader = new SimpleGraphReader();
				root = reader.readSimpleGraphFile(xmlfile);
			}


			if (root != null)
			{
				main.getViewer().setContents(root);

				/*if (reader.runLayout)
				{
					new CoSELayoutAction(main).run();
				}
				else
				{
					new ZoomAction(main, 0, null).run();
				}*/

				main.setCurrentFilename(filename);
				main.getEditDomain().getCommandStack().flush();
			}
			// reset filename for future loadings.
			// otherwise always opens the same file
			filename = null;
			
			// add highlight figures
			if (this.main.isClusterBoundShown)
			{		
				Iterator<Cluster> iter = this.main.getRootGraph().
					getClusterManager().getClusters().iterator();
	
				while(iter.hasNext())
				{
					this.main.getHighlightLayer().
						addHighlightToCluster(iter.next());
				}
			}
		}
	}
}