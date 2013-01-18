package org.gvt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.gvt.model.CompoundModel;
import org.gvt.model.EdgeModel;
import org.gvt.model.NodeModel;

/**
 * GraphML reader class for loading GDToolkit formatted files
 *
 * @author Esat Belviranli
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class GDToolkitXMLReader extends XMLReader
{
	public CompoundModel readXMLFile(File xmlFile)
	{
		CompoundModel root = new CompoundModel();
		root.setAsRoot();

		boolean fileValid = false;

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(xmlFile));

			HashMap<String, EdgeModel> idToEdgeMap =
				new HashMap<String, EdgeModel>();

			String line = reader.readLine();

			while (line != null)
			{
				if (line.indexOf("<UNDISECTION>") > -1 ||
						line.indexOf("<NODELIST>") > -1 ||
						line.indexOf("</NODELIST>") > -1 ||
						line.trim().equals(""))
				{
					// Do nothing
				}
				else if (line.indexOf("</UNDISECTION>") > -1)
				{
					fileValid = true;
					break;
				}
				else if (line.indexOf("<NODE>") > -1)
				{
					// Start reading the node

					String nodeId = reader.readLine().trim();

					NodeModel node = new NodeModel();
					node.setText(nodeId);

					root.addChild(node);
					line = reader.readLine();

					while (line.indexOf("</NODE>") < 0)
					{
						line = line.trim();
						StringTokenizer tokenizer = new StringTokenizer(line);
						tokenizer.nextToken();

						String edgeId = tokenizer.nextToken();

						if (idToEdgeMap.containsKey(edgeId))
						{
							EdgeModel edge = idToEdgeMap.get(edgeId);

							if (edge.getTarget() != null)
							{
								System.out.println("Target already specified");
							}

							edge.setTarget(node);
							node.addTargetConnection(edge);
						}
						else
						{
							EdgeModel edge = new EdgeModel();

							edge.setSource(node);
							node.addSourceConnection(edge);

							idToEdgeMap.put(edgeId, edge);
						}

						line = reader.readLine();
					}
				}
				else
				{
					System.out.println("Unexpected line: " + line);
				}

				line = reader.readLine();
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fileValid)
		{
			return root;
		}
		else
		{
			return null;
		}
	}
}
