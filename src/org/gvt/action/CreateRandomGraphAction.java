package org.gvt.action;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.gvt.ChisioMain;
import org.gvt.inspector.RandomGraphCreationDialog;
import org.gvt.model.CompoundModel;
import org.gvt.util.RandomGraphOptionsPack;
import org.ivis.layout.Cluster;

/**
 * Action for creating a random graph.
 *
 * @author Esat Belviranli
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CreateRandomGraphAction extends Action
{
	ChisioMain main;

	public CreateRandomGraphAction(ChisioMain chisio)
	{
		super("Create Random Graph");
		this.setToolTipText("Create Random Graph");
		this.setImageDescriptor(ImageDescriptor.createFromFile(
			ChisioMain.class,
			"icon/random.gif"));
		this.main = chisio;
	}

	public void run()
	{
		RandomGraphCreationDialog dialog = new RandomGraphCreationDialog(main);
		Object result = dialog.open();

		if (result instanceof CompoundModel)
		{
			CompoundModel graph = (CompoundModel) result;

			// reset highlight
			main.getHighlightLayer().removeAll();
			main.getHighlightLayer().highlighted.clear();
			main.getHighlightLayer().clusterHighlights.clear();
			
			graph.setAsRoot();
			this.main.getViewer().setContents(graph);
		}
		else if (result == Boolean.TRUE)
		{
			// OK clicked, nothing special needed.
		}
		
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