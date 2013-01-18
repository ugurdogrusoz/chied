package org.gvt.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.gvt.ChisioMain;
import org.gvt.command.CreateCommand;
import org.gvt.command.CreateConnectionCommand;
import org.gvt.model.CompoundModel;
import org.gvt.model.ECluster;
import org.gvt.model.EdgeModel;
import org.gvt.model.NodeModel;

/**
 * Simple graph reader class for loading simple txt graph files
 *
 * @author Sinan Sonlu
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SimpleGraphReader {
	
	// graphml and chisio structure map
	private HashMap hashMapNodes = new HashMap();
	
	// cluster id's are mapped using this hashmap
	private HashMap hashMapClusters = new HashMap();
	
	// random generator for highlight colors
	private Random random = new Random(System.currentTimeMillis());
	
	// Reading Modes
	private final int FREE_MODE = 0;
	private final int READ_NODES_MODE = 1;
	private final int READ_EDGES_MODE = 2;
	private final int READ_CLUSTERS_MODE = 3;
	
	// Mode Change Strings
	private final String NODES = "NODES";
	private final String EDGES = "EDGES";
	private final String CLUSTERS = "CLUSTERS";
	
	/**
	 * This method reads the given txt file and creates
	 * and returns the corresponding compound model 
	 * 
	 * @param txtFile	txt file to be read
	 * 
	 * @return	The compound model that is created
	 */
	public CompoundModel readSimpleGraphFile (File txtFile)
	{
		CompoundModel root = null;
		
		try
		{
			// Initializes the buffered reader
			FileInputStream fstream = new FileInputStream(txtFile);
			DataInputStream inputStream = new DataInputStream(fstream);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(inputStream));
			
			// String and Tokenizer variables to read txt lines into
			String line = null;
			StringTokenizer tokenizer = null;
			
			// create root node
			root = new CompoundModel();
			root.setAsRoot();
			
			// initial reading mode is free mode
			int readingMode = FREE_MODE;
			
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();
				
				if (line.indexOf('#') == 0)
				{
					continue;
				}
				else if (line.indexOf('$') == 0)
				{
					// Reading mode changes start with '$'
					line = line.substring(1);
					tokenizer = new StringTokenizer( line);
					line = tokenizer.nextToken();
					
					if (line.equalsIgnoreCase(NODES))
					{
						readingMode = READ_NODES_MODE;
					}
					else if (line.equalsIgnoreCase(EDGES))
					{
						readingMode = READ_EDGES_MODE;
					}
					else if (line.equalsIgnoreCase(CLUSTERS))
					{
						readingMode = READ_CLUSTERS_MODE;
					}
				}
				else if (!line.isEmpty())
				{
					if (readingMode == READ_NODES_MODE)
					{
						tokenizer = new StringTokenizer(line);
						String nodeText = tokenizer.nextToken();
						
						addNode(nodeText, root);
					}
					else if (readingMode == READ_EDGES_MODE)
					{
						tokenizer = new StringTokenizer(line);
						String source = tokenizer.nextToken();
						String target= tokenizer.nextToken();
						
						addEdge(target, source, root);
					}
					else if (readingMode == READ_CLUSTERS_MODE)
					{
						tokenizer = new StringTokenizer(line,": ");
						String clusterString = tokenizer.nextToken();
						
						// ClusterID is incremented since cluster 0 is root
						int clusterID = hashMapClusters.size() + 1;					
						
						hashMapClusters.put(clusterString, 
							new Integer(clusterID));
												
						// For each node of this cluster add cluster info
						while (tokenizer.hasMoreTokens())
						{
							String nodeText = tokenizer.nextToken();

							// Add the node into cluster if it exists
							if (hashMapNodes.containsKey(nodeText))
							{
								NodeModel model = 
									(NodeModel) hashMapNodes.get(nodeText);
								model.addCluster(clusterID);
							}
						}
					}
				}
			}
			
			// Sets the highlight color of the clusters
			Iterator clusterIterator = 
				root.getClusterManager().getClusters().iterator();
			 
			while (clusterIterator.hasNext())
			{
				ECluster cluster = (ECluster) clusterIterator.next();
				
				Color clusterColor = new Color(null,
						this.random.nextInt(256),
						this.random.nextInt(256),
						this.random.nextInt(256));
				
				cluster.setHighlightColor(clusterColor);
			}
		}
		catch (Exception e)
		{
			MessageBox messageBox = new MessageBox(
				new Shell(),
				SWT.ERROR_UNSUPPORTED_FORMAT);
			messageBox.setMessage("File cannot be loaded!");
			messageBox.setText("Chisio");
			messageBox.open();

			return null;
		}
		
		return root;
	}
	
	/**
	 * This method creates and adds the given node into given graph
	 * 
	 * @param nodeText	The text of the node to be created
	 * @param graph	The graph where the created node is to be added
	 */
	private void addNode(String nodeText,
		CompoundModel graph)
	{
		
		if (!hashMapNodes.containsKey(nodeText))
		{										
			NodeModel model = new NodeModel();
			
			// Sets the text of this node
			model.setText(nodeText);
			
			// Sets the parent of this node
			model.setParentModel(graph);

			hashMapNodes.put(nodeText, model);

			CreateCommand command = new CreateCommand(graph,
				(NodeModel) model);
			command.execute();
		}
	}
	
	/**
	 * This method adds the given edge into the given graph
	 * 
	 * @param
	 */
	private void addEdge(String target,
		String source,
		CompoundModel graph)
	{
		// Checks if target and source nodes exist, crates them if they do not appear
		if (!hashMapNodes.containsKey(target))
		{
			addNode(target, graph);
		}
		
		if (!hashMapNodes.containsKey(source))
		{
			addNode(source, graph);
		}
		
		// adds the new edge
		EdgeModel model = new EdgeModel();

		CreateConnectionCommand command = new CreateConnectionCommand();
		command.setSource(hashMapNodes.get(source));
		command.setTarget(hashMapNodes.get(target));
		command.setConnection(model);
		command.execute();
	}

}
