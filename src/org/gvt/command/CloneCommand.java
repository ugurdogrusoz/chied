package org.gvt.command;

import java.util.*;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.gvt.model.*;
import org.ivis.layout.Cluster;

/**
 * This class maintains clone command. while dragging a node, if CTRL key is
 * pressed then, dragged node is cloned.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CloneCommand extends Command
{
	private List parts, newTopLevelParts, newConnections;

	private CompoundModel parent;

	private Map bounds;
	
	private HashMap oldToNew;

	public CloneCommand()
	{
		super("Clone Command");
		parts = new LinkedList();
		oldToNew = new HashMap();
	}

	public void addPart(GraphObject part, Rectangle newBounds)
	{
		parts.add(part);

		if (bounds == null)
		{
			bounds = new HashMap();
		}

		bounds.put(part, newBounds);
	}

	public void addPart(GraphObject part, int index)
	{
		parts.add(part);
	}

	protected void clonePart(
		NodeModel oldPart,
		CompoundModel newParent,
		Rectangle newBounds,
		List newConnections
	)
	{
		NodeModel newPart = null;

		if (oldPart instanceof CompoundModel)
		{
			newPart = new CompoundModel();
		}
		else if (oldPart instanceof NodeModel)
		{
			newPart = new NodeModel();
		}

		newPart.setText(oldPart.getText());
		newPart.setTextFont(oldPart.getTextFont());
		newPart.setTextColor(oldPart.getTextColor());
		newPart.setShape(oldPart.getShape());
		newPart.setColor(oldPart.getColor());
		newPart.setBorderColor(oldPart.getBorderColor());

		oldToNew.put(oldPart, newPart);
		
		if (oldPart instanceof CompoundModel)
		{
			Iterator i = ((CompoundModel) oldPart).getChildren().iterator();

			while (i.hasNext())
			{
				// for children they will not need new bounds
				clonePart((NodeModel) i.next(), (CompoundModel) newPart, null,
					newConnections);
			}
		}

		newParent.addChild(newPart);
		newPart.setParentModel(newParent);
		newPart.setSize(oldPart.getSize());

		if (newBounds != null)
		{
			newPart.setLocation(newBounds.getTopLeft());
		}
		else
		{
			newPart.setLocation(oldPart.getLocation());
		}

		// keep track of the new parts so we can delete them in undo
		// keep track of the oldpart -> newpart map so that we can properly
		// attach all connections.
		if (newParent == parent)
		{
			newTopLevelParts.add(newPart);
		}
		
		// copy clusters
		for (Cluster cluster : oldPart.getClusters())
		{
			newPart.addCluster(cluster.getClusterID());
		}
	}

	public void execute()
	{
		newConnections = new LinkedList();
		newTopLevelParts = new LinkedList();
		Iterator i = parts.iterator();

		NodeModel part;

		while (i.hasNext())
		{
			part = (NodeModel) i.next();

			if (bounds != null && bounds.containsKey(part))
			{
				clonePart(part,
					parent,
					(Rectangle) bounds.get(part),
					newConnections);
			}
			else
			{
				clonePart(part, parent, null, newConnections);
			}
		}

		Object[] list = oldToNew.keySet().toArray();

		for (int k = 0; k < list.length; k++)
		{
			NodeModel node = (NodeModel) list[k];

			for (int m = 0; m < list.length; m++)
			{
				if (m != k)
				{
					NodeModel node2 = (NodeModel) list[m];
					i = node.getEdgeListToNode((NodeModel) list[m]).iterator();

					while (i.hasNext())
					{
						EdgeModel connection = (EdgeModel)i.next();
						EdgeModel newConnection = new EdgeModel();
						newConnection.setTarget(
							(NodeModel) oldToNew.get(node2));
						newConnection.setSource((NodeModel) oldToNew.get(node));
			            newConnection.attachSource();
						newConnection.attachTarget();

						Iterator b = connection.getBendpoints().iterator();
						Vector newBendPoints = new Vector();

						while (b.hasNext())
						{
							EdgeBendpoint bendPoint = (EdgeBendpoint)b.next();
							EdgeBendpoint newBendPoint = new EdgeBendpoint();
							newBendPoint.setRelativeDimensions(
								bendPoint.getFirstRelativeDimension(),
								bendPoint.getSecondRelativeDimension());
							newBendPoint.setWeight(bendPoint.getWeight());
							newBendPoints.add(newBendPoint);
						}

						newConnection.setBendpoints(newBendPoints);
					}
				}
			}
		}

		parent.calculateSizeUp();
	}

	public void setParent(CompoundModel parent)
	{
		this.parent = parent;
	}

	public void redo()
	{
		// CK: Clone operation is changed. So redo must be updated
	/*	for (Iterator iter = newTopLevelParts.iterator(); iter.hasNext();)
		{
			parent.addChild((GraphObject) iter.next());
		}
		for (Iterator iter = newConnections.iterator(); iter.hasNext();)
		{
			EdgeModel conn = (EdgeModel) iter.next();
			GraphObject source = conn.getSource();
			conn.attachSource();
			conn.attachTarget();

		}       */
	}

	public void undo()
	{
		// CK: Clone operation is changed. So undo must be updated
		/*
		for (Iterator iter = newTopLevelParts.iterator(); iter.hasNext();)
			parent.removeChild((GraphObject) iter.next());
			*/
	}
}