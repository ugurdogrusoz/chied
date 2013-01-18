package org.gvt.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.model.CompoundModel;
import org.gvt.util.GDToolkitXMLReader;
import org.gvt.util.GraphMLWriter;
import org.gvt.util.XMLReader;
import org.gvt.util.XMLWriter;

/**
 * This action converts a set of files given in GDToolkit format to graphml
 * format.
 * The directories should be specified by the inputDirName, oututDirName.
 *
 * @author Esat Belviranli
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class LoadGDToolkitXMLAction extends Action
{
	ChisioMain main;

	/**
	 * Constructor
	 *
	 * @param chisio
	 */
	public LoadGDToolkitXMLAction(ChisioMain chisio)
	{
		super("Open");
		this.setToolTipText("Open");
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/open.gif"));

		this.main = chisio;
	}

	public void run()
	{
		try
		{
			// reset highlight
			this.main.getHighlightLayer().removeAll();
			this.main.getHighlightLayer().highlighted.clear();
			this.main.getHighlightLayer().clusterHighlights.clear();

			String inputDirName =
				"e:/research/cise/rome graphs/GDT-testsuite-BUP.tgz";

			String outputDirName = "E:/research/cise/rome graphs/bup";

			ArrayList<File> files = this.getFiles(new File(inputDirName));

			for (File file : files)
			{
				XMLReader reader = new GDToolkitXMLReader();
				CompoundModel root = reader.readXMLFile(file);

				if (root != null)
				{
					this.main.getViewer().setContents(root);

					String fileName = file.getName();
					int orderNo = Integer.parseInt(
						fileName.substring(2, fileName.indexOf('.')));
					int noOfNodes = Integer.parseInt(
						fileName.substring(fileName.indexOf('.')+1));

					fileName = "bup."+noOfNodes+"."+(orderNo < 10?"0":"")+orderNo+".graphml";

					File outputFile = new File(outputDirName+"/"+ fileName);
					BufferedWriter xmlFile =
						new BufferedWriter(new FileWriter(outputFile));
					XMLWriter writer = new GraphMLWriter();
					xmlFile.write(writer.writeXMLFile(root).toString());
					xmlFile.close();
				}
				System.out.println(file);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method recursiverly finds all files under given directory.
	 * @param dir
	 * @return
	 */
	private ArrayList<File> getFiles(File dir)
	{
		ArrayList<File> files = new ArrayList<File>();

		String[] children = dir.list();

		for (int i=0; i<children.length; i++)
		{
			File child = new File(dir.getAbsolutePath()+"/"+children[i]);

			if (child.isDirectory())
			{
				files.addAll(this.getFiles(child));
			}
			else
			{
				files.add(child);
			}
		}

		return files;
	}
}