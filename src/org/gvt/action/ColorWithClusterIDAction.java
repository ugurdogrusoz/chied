package org.gvt.action;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.gvt.ChisioMain;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.model.CompoundModel;
import org.gvt.model.ECluster;
import org.gvt.model.EdgeModel;
import org.gvt.model.NodeModel;
import org.ivis.layout.Cluster;

/**
 * This actions paints the nodes according to their cluster informations.
 * The nodes in the same cluster are painted with the same color. Color is
 * choosen randomly.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ColorWithClusterIDAction extends Action
{
	ChisioMain main;

	/**
	 * Constructor
	 */
	public ColorWithClusterIDAction(ChisioMain main)
	{
		super("Color using Cluster IDs");
		this.setToolTipText("Color using Cluster IDs");
		setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/color-cluster.png"));

		this.main = main;
	}

	public void run()
	{
		Random rnd = new Random();
		List clusterIDs = main.getRootGraph().getClusterManager().getClusterIDs();
		Color [] colors = new Color[clusterIDs.size()];
		Color [] palette = {
			new Color(null, 250, 247, 67),
			new Color(null, 67, 250, 156),
			new Color(null, 67, 69, 250),
			new Color(null, 250, 67, 160),
			new Color(null, 212, 161, 144),
			new Color(null, 161, 212, 144),
			new Color(null, 195, 144, 212),
			new Color(null, 144, 195, 212),
			new Color(null, 0, 255, 255)};

		// In case we do not have enough colors, use some random ones

		for (int i = 0 ; i < clusterIDs.size(); i++)
		{
			if (i < palette.length)
			{
				colors[i] = palette[i];
			}
			else
			{
				colors[i] = new Color(null, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
			}
		}

		// Color region of each cluster
		ECluster cluster;

		for (int i = 0 ; i < clusterIDs.size(); i++)
		{
			cluster = (ECluster) (this.main.getRootGraph().getClusterManager().
					getClusterByID((Integer) clusterIDs.get(i)));

			cluster.setHighlightColor(colors[i]);
		}

		// Color the nodes of each cluster to be the same as cluster region
		// color. Fill color is based on cluster color (if in a single cluster;
		// use white for nodes in multiple clusters); border color is black if
		// node is not inside the bounds of any cluster it doesn't belong to,
		// otherwise red. Nodes not in any cluster colored with default color.

		CompoundModel root =
			(CompoundModel) ((ChsRootEditPart) this.main.getViewer().
				getRootEditPart().getChildren().get(0)).getModel();
		Iterator nodeIter = root.getNodes().iterator();

		while (nodeIter.hasNext())
		{
			NodeModel node = (NodeModel) nodeIter.next();

			// fill color

			if (node.getClusters().isEmpty())
			{
				if (node instanceof CompoundModel)
				{
					node.setColor(CompoundModel.DEFAULT_COLOR);
				}
				else
				{
					node.setColor(NodeModel.DEFAULT_COLOR);
				}
			}
			if (node.getClusters().size() == 1)
			{
				// fill color same as cluster color
				cluster = (ECluster)(node.getClusters().get(0));
				node.setColor(cluster.getHighlightColor());
			}
			else
			{
				// white if in multiple clusters
				node.setColor(new Color(null, 255, 255, 255));
			}

			// border color

			Color borderColor = new Color(null, 0, 0, 0);

			for (Cluster c : this.main.getRootGraph().getClusterManager().getClusters())
			{
				if (!c.getNodes().contains(node))
				{
					if (c.isPartiallyInsideClusterBounds(node))
					{
						borderColor = new Color(null, 255, 0, 0);
						break;
					}
				}
			}

			node.setBorderColor(borderColor);
		}

		// Color intra-graph edges lighter then inter-graph edges

		for (Object edgeObject : root.getEdges())
		{
			EdgeModel edge = (EdgeModel) edgeObject;

			NodeModel src = edge.getSource();
			NodeModel trgt = edge.getTarget();

			if (src.hasCommonCluster(trgt))
			{
				edge.setColor(ColorConstants.gray);
			}
			else
			{
				edge.setColor(EdgeModel.DEFAULT_COLOR);
			}
		}
	}
}