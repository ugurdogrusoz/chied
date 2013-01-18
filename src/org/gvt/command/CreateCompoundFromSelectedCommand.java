package org.gvt.command;

import java.util.*;

import org.eclipse.gef.commands.Command;
import org.gvt.model.*;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CreateCompoundFromSelectedCommand extends Command
{
	List nodes;

	public CreateCompoundFromSelectedCommand(List nodes)
	{
		super("Create Compound From Selected Command");
		this.nodes = nodes;
	}

	public void execute()
	{
		CompoundModel parent = ((NodeModel) nodes.get(0)).getParentModel();
		CompoundModel compound = new CompoundModel();
		compound.setClusterManager(parent.getClusterManager());
		List<EdgeModel> highlightedEdges = new ArrayList();

		for (int i = 0; i < nodes.size(); i++)
		{
			NodeModel node = (NodeModel) nodes.get(i);
			// remove node highlight
			boolean highlight = node.isHighlight();
			node.setHighlight(false);

			// remove edge highlights
			List edges = node.getSourceConnections();
			Iterator<EdgeModel> iter = edges.iterator();

			while (iter.hasNext())
			{
				EdgeModel edge = iter.next();

				if (edge.isHighlight() && nodes.contains(edge.getTarget()))
				{
					highlightedEdges.add(edge);
					edge.setHighlight(false);
				}
			}

			OrphanChildCommand orphan = new OrphanChildCommand();
			orphan.setParent(node.getParentModel());
			orphan.setChild(node);
			orphan.execute();

			AddCommand add = new AddCommand();
			add.setParent(compound);
			add.setChild(node);
			add.execute();

			if (highlight)
			// restore node highlights
			{
				node.setHighlight(true);
			}
		}

		// restore edge highlights
		for (int i = 0; i < highlightedEdges.size(); i++)
		{
			EdgeModel edge = highlightedEdges.get(i);
			edge.setHighlight(true);
		}

		CreateCommand cmd = new CreateCommand(parent, compound);
		cmd.execute();
		compound.calculateSizeUp();
	}

	public void redo()
	{
		execute();
	}

	public void undo()
	{

	}
}