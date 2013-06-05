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
 * This action sets the labels of each node as the cluster id that it
 * belongs.
 *
 * @author Can Cagdas Cengiz
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class SetNodeLabelsAsClusterIDsAction extends Action
{
	ChisioMain main;
	
	/**
	 * Constructor
	 */
	public SetNodeLabelsAsClusterIDsAction(ChisioMain main)
	{
		super("Set Node Labels As Cluster IDs");
		this.setToolTipText("Set Node Labels As Cluster IDs");
	
		this.main = main;
		//setChecked(this.main.isClusterBoundShown);	
	}
	
	public void run()
	{
		for (Object o : this.main.getRootGraph().getNodes())
		{
			NodeModel node = (NodeModel) o;
			String text = node.getClusterIDs();
			text = text.replace("|", "-");
			node.setText(text); 
		}
		
	}
	
}