package org.gvt.util;

import java.io.File;
import java.util.*;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.graphdrawing.graphml.xmlns.*;
import org.gvt.ChisioMain;
import org.gvt.command.CreateCommand;
import org.gvt.command.CreateConnectionCommand;
import org.gvt.model.*;

/**
 * GraphML reader class for loading graphml files
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class GraphMLReader extends XMLReader
{
	// graphml and chisio structure map
	public HashMap hashMap = new HashMap();

	// edges and their bendpoints map
	public HashMap bendpointMap = new HashMap();
	
	// random generator for highlight colors
	private Random random = new Random(System.currentTimeMillis());

	public CompoundModel readXMLFile(File xmlFile)
	{
		CompoundModel root = null;
		GraphmlDocument graphmlDoc = null;
		try
		{
			// Bind the incoming XML to an XMLBeans type.
			graphmlDoc = GraphmlDocument.Factory.parse(xmlFile);

			//create graphml structure from file
			GraphmlType graphml = graphmlDoc.getGraphml();

			//get the first graph. in Chisio, there is only one Root graph.
			GraphType graph = graphml.getGraphArray(0);
			// create root node
			root = new CompoundModel();
			root.setAsRoot();
			//start to create graph recursively
			createTree(graph, root);

			// calculate sizes of all compound node of the graph
			root.calculateSizeDown();

			if (marginSize >= 0)
			{
				CompoundModel.MARGIN_SIZE = marginSize;
			}

			// Set bendpoints
			Iterator edgeIter = bendpointMap.keySet().iterator();
			while (edgeIter.hasNext())
			{
				EdgeModel edge = (EdgeModel) edgeIter.next();
				List points = (List) bendpointMap.get(edge);
				for (int i = 0; i < points.size(); i++)
				{
					String point = (String) points.get(i);
					String[] pts = point.split(",");
					EdgeBendpoint bp = new EdgeBendpoint(
						Integer.parseInt(pts[0]),
						Integer.parseInt(pts[1]),
						Integer.parseInt(pts[2]),
						Integer.parseInt(pts[3]));
					edge.insertBendpoint(bp);
				}
			}
			
			// Sets the highlight color of the clusters
			Iterator clusterIterator = root.getClusterManager().getClusters().iterator();
			 
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
			// e.printStackTrace();
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
	 * create the Chisio models by reading graphml structure recursively
	 *
	 * @param root
	 * @param root
	 */
	public void createTree(Object root, CompoundModel rootModel)
	{
		GraphType rootGraph = (GraphType) root;

		if (rootGraph.getDataArray().length > 1)
		{
			// read the margin size from root graph
			String m = rootGraph.getDataArray(0).newCursor().getTextValue();
			marginSize = Integer.parseInt(m);
		}

		// create nodes
		NodeType[] nodes = rootGraph.getNodeArray();

		for (int n = 0; n < nodes.length; n++)
		{
			NodeType node = nodes[n];

			NodeModel model;

			// decide that node is simple or compound node
			if (node.getGraph() == null)
			{
				model = new NodeModel();
			}
			else
			{
				model = new CompoundModel();
			}

			// Add this node as child of the parent, 
			// and set the parent of the node
			rootModel.addChild(model);
			model.setParentModel(rootModel);

			hashMap.put(node.getId(), model);

			readNode(node, model);
		}

		// create edges
		EdgeType[] edges = rootGraph.getEdgeArray();

		for (int e = 0; e < edges.length; e++)
		{
			EdgeType edge = edges[e];
			EdgeModel model = new EdgeModel();

			readEdge(edge, model);

			CreateConnectionCommand command = new CreateConnectionCommand();
			command.setSource(hashMap.get(edge.getSource()));
			command.setTarget(hashMap.get(edge.getTarget()));
			command.setConnection(model);
			command.execute();
		}
	}

	public void readNode(NodeType node, NodeModel model)
	{
		int x = -1;
		int y = -1;
		int height = -1;
		int width = -1;

		String shape = null;
		String clusterID = null;
		String color = null;
		String borderColor = null;
		String text = null;
		String textFont = null;
		String textColor = null;
		String highlightColor = null;

		// read node's properties
		DataType[] datas = node.getDataArray();

		for (int i = 0; i < datas.length; i++)
		{
			DataType data = datas[i];

			if (data.getKey().equalsIgnoreCase("x"))
			{
				x = Integer.parseInt(data.newCursor().getTextValue());
			}
			else if (data.getKey().equalsIgnoreCase("y"))
			{
				y = Integer.parseInt(data.newCursor().getTextValue());
			}
			else if (data.getKey().equalsIgnoreCase("height"))
			{
				height = Integer.parseInt(data.newCursor().getTextValue());
			}
			else if (data.getKey().equalsIgnoreCase("width"))
			{
				width = Integer.parseInt(data.newCursor().getTextValue());
			}
			else if (data.getKey().equalsIgnoreCase("color"))
			{
				color = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("borderColor"))
			{
				borderColor = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("text"))
			{
				text = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("textFont"))
			{
				textFont = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("textColor"))
			{
				textColor = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("shape"))
			{
				shape = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("clusterID"))
			{
				clusterID = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("highlightColor"))
			{
				highlightColor = data.newCursor().getTextValue();
			}
		}

		// Assign the properties to Chisio model
		if (x >= 0 && y >= 0)
		{
			runLayout = false;
			model.setSize(new Dimension(width, height));
		}

		if (height >= 0 && width >= 0)
		{
			model.setLocation(new Point(x, y));
		}

		if (clusterID != null)
		{
			StringTokenizer st = new StringTokenizer( clusterID, "|");
			
			while (st.hasMoreElements()) 
			{
				String token = st.nextElement().toString();
	            model.addCluster(Integer.parseInt(token));
			}
			
		}

		if (color != null)
		{
			String[] rgb = color.split(" ");
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			model.setColor(new Color(null, r, g, b));
		}

		if (borderColor != null)
		{
			String[] rgb = borderColor.split(" ");
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			model.setBorderColor(new Color(null, r, g, b));
		}

		if (text != null)
		{
			model.setText(text);
		}

		if (textFont != null)
		{
			model.setTextFont(new Font(null, new FontData(textFont)));
		}

		if (textColor != null)
		{
			String[] rgb = textColor.split(" ");
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			model.setTextColor(new Color(null, r, g, b));
		}

		if (highlightColor != null)
		{
			String[] rgb = highlightColor.split(" ");
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			model.setHighlightColor(new Color(null, r, g, b));
			model.setHighlight(true);
		}

		if (model instanceof CompoundModel)
		{
			// if node is a compound node, then create that subgraph
			createTree(node.getGraph(), (CompoundModel) model);
		}
		else if (model instanceof NodeModel)
		{
			if (shape != null)
			{
				model.setShape(shape);
			}
		}
	}

	public void readEdge(EdgeType edge, EdgeModel model)
	{
		int width = -1;
		String style = null;
		String arrow = null;
		String color = null;
		String text = null;
		String textFont = null;
		String textColor = null;
		String highlightColor = null;
		ArrayList bendpoints = new ArrayList();

		// read edge's properties
		DataType[] datas = edge.getDataArray();

		for (int i = 0; i < datas.length; i++)
		{
			DataType data = datas[i];

			if (data.getKey().equalsIgnoreCase("color"))
			{
				color = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("text"))
			{
				text = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("textFont"))
			{
				textFont = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("textColor"))
			{
				textColor = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("arrow"))
			{
				arrow = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("style"))
			{
				style = data.newCursor().getTextValue();
			}
			else if (data.getKey().equalsIgnoreCase("bendpoint"))
			{
				bendpoints.add(data.newCursor().getTextValue());
			}
			else if (data.getKey().equalsIgnoreCase("width"))
			{
				width = Integer.parseInt(data.newCursor().getTextValue());
			}
			else if (data.getKey().equalsIgnoreCase("highlightColor"))
			{
				highlightColor = data.newCursor().getTextValue();
			}
		}

		if (color != null)
		{
			String[] rgb = color.split(" ");
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			model.setColor(new Color(null, r, g, b));
		}

		if (text != null)
		{
			model.setText(text);
		}

		if (textFont != null)
		{
			model.setTextFont(new Font(null, new FontData(textFont)));
		}

		if (textColor != null)
		{
			String[] rgb = textColor.split(" ");
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			model.setTextColor(new Color(null, r, g, b));
		}

		if (style != null)
		{
			model.setStyle(style);
		}

		if (arrow != null)
		{
			model.setArrow(arrow);
		}

		if (bendpoints.size() > 0)
		{
			bendpointMap.put(model, bendpoints);
		}

		if (width > 0)
		{
			model.setWidth(width);
		}

		if (highlightColor != null)
		{
			String[] rgb = highlightColor.split(" ");
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			model.setHighlightColor(new Color(null, r, g, b));
			model.setHighlight(true);
		}
	}
}
