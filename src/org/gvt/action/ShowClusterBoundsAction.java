package org.gvt.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
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
 * This action shows/hides bounds of clusters.
 *
 * @author Shatlyk Ashyralyyev
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ShowClusterBoundsAction extends Action
{
	ChisioMain main;
	
	/**
	 * Constructor
	 */
	public ShowClusterBoundsAction(ChisioMain main)
	{
		super("Show Cluster Bounds");
		this.setToolTipText("Show Cluster Bounds");
	
		this.main = main;
		setChecked(this.main.isClusterBoundShown);	
	}
	
	public void run()
	{
		if(this.main.isClusterBoundShown)
		{	
			Iterator<Cluster> iter = this.main.getRootGraph().
				getClusterManager().getClusters().iterator();
		
			while(iter.hasNext())
			{
				this.main.getHighlightLayer().
					removeHighlightFromCluster(iter.next());
			}
			
			setChecked(false);
			this.main.isClusterBoundShown = false;
		}
		else
		{
			Iterator<Cluster> iter = this.main.getRootGraph().
				getClusterManager().getClusters().iterator();
		
			while(iter.hasNext())
			{
				this.main.getHighlightLayer().
					addHighlightToCluster(iter.next());
			}
			
			setChecked(true);
			this.main.isClusterBoundShown = true;
		}
	}
}