package org.gvt.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.graphdrawing.graphml.xmlns.EdgeType;
import org.gvt.model.CompoundModel;
import org.gvt.model.EdgeModel;
import org.gvt.model.NodeModel;
import org.ivis.layout.Cluster;

/**
 * Simple graph writer class for saving simple txt graph files
 *
 * @author Sinan Sonlu
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SimpleGraphWriter {
	
	// Mode Change Commands
	private final String NODES = "$NODES";
	private final String EDGES = "$EDGES";
	private final String CLUSTERS = "$CLUSTERS";
	
	/**
	 * This method saves the given compound model into given txt in simple form
	 * @param root	Compound model that is to be saved
	 * @param fileName	File name of the save output
	 * @throws IOException
	 */
	public void writeSimpleGraphFile(CompoundModel root,
		String fileName)
	throws IOException
	{
		FileWriter fileWriter = new FileWriter(fileName);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		// start writing the nodes data
		bufferedWriter.write(NODES);
		bufferedWriter.newLine();
		
		// create child nodes for this graph
		Iterator iter = root.getChildren().iterator();
		
		while (iter.hasNext())
		{
			NodeModel model = (NodeModel) iter.next();
			bufferedWriter.write(model.getText());
			bufferedWriter.newLine();
		}
		
		// start writing the edges data
		bufferedWriter.newLine();
		bufferedWriter.write(EDGES);
		bufferedWriter.newLine();
		
		Iterator edgeIter =
			root.getEdgeIterator(CompoundModel.ALL_EDGES, true, false);
		
		while (edgeIter.hasNext())
		{
			EdgeModel model = (EdgeModel) edgeIter.next();
			
			bufferedWriter.write ( model.getSource().getText() + " " +
					model.getTarget().getText());
			bufferedWriter.newLine();
		}
		
		// start writing the clusters data
		bufferedWriter.newLine();
		bufferedWriter.write(CLUSTERS);
		bufferedWriter.newLine();
		
		Iterator clusterIter = 
			root.getClusterManager().getClusters().iterator();
		
		while (clusterIter.hasNext())
		{
			Cluster cluster = (Cluster) clusterIter.next();

			if (cluster.getClusterID() != 0)
			{
				// write cluster id 
				bufferedWriter.write( cluster.getClusterID() 
					+ ": ");
				
				// get iterator for the nodes of this clusters
				Iterator nodeIter = cluster.getNodes().iterator();
				
				while (nodeIter.hasNext())
				{
					NodeModel node = (NodeModel) nodeIter.next();
					bufferedWriter.write( node.getText() + " ");
				}
				
				bufferedWriter.newLine();
			}
		}
		bufferedWriter.close();
	}
}
