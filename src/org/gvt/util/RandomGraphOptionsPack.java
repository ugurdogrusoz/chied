package org.gvt.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * This method gathers random graph options in a package
 *
 * @author Esat Belviranli
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class RandomGraphOptionsPack implements Serializable
{
	private static RandomGraphOptionsPack instance;

	private static String optionsFile = "options.dat";

	private Common common;
	private Compound compound;
	private Cluster cluster;
	private Flat flat;

	public class Common implements Serializable
	{
		private int noOfNodes;
		private int noOfEdges;
		private Dimension minNodeSize;
		private Dimension maxNodeSize;
		private boolean removeDisconnectedNodes;
		private int graphType;

		public int getNoOfNodes()
		{
			return this.noOfNodes;
		}
		public void setNoOfNodes(int noOfNodes)
		{
			this.noOfNodes = noOfNodes;
		}
		public int getNoOfEdges()
		{
			return this.noOfEdges;
		}
		public void setNoOfEdges(int noOfEdges)
		{
			this.noOfEdges = noOfEdges;
		}
		public Dimension getMinNodeSize()
		{
			return this.minNodeSize;
		}
		public void setMinNodeSize(Dimension minNodeSize)
		{
			this.minNodeSize = minNodeSize;
		}
		public Dimension getMaxNodeSize()
		{
			return this.maxNodeSize;
		}
		public void setMaxNodeSize(Dimension maxNodeSize)
		{
			this.maxNodeSize = maxNodeSize;
		}
		public int getGraphType()
		{
			return this.graphType;
		}
		public void setGraphType(int graphType)
		{
			this.graphType = graphType;
		}
		public boolean isRemoveDisconnectedNodes()
		{
			return this.removeDisconnectedNodes;
		}
		public void setRemoveDisconnectedNodes(boolean removeDisconnectedNodes)
		{
			this.removeDisconnectedNodes = removeDisconnectedNodes;
		}
	}

	public class Flat implements Serializable
	{
		private RandomGraphCreator.FlatGraphType graphType;
		
		// Tree related
		private double pruningProbability;
		private int minBranching;
		private int maxBranching;
		
		public void setGraphType(RandomGraphCreator.FlatGraphType graphType)
		{
			this.graphType = graphType;
		}
		public RandomGraphCreator.FlatGraphType getGraphType()
		{
			return graphType;
		}
		public void setPruningProbability(double pruningProbability)
		{
			this.pruningProbability = pruningProbability * 100.0;
		}
		public double getPruningProbability()
		{
			return pruningProbability;
		}
		public void setMinBranching(int minBranching)
		{
			this.minBranching = minBranching;
		}
		public int getMinBranching()
		{
			return minBranching;
		}
		public void setMaxBranching(int maxBranching)
		{
			this.maxBranching = maxBranching;
		}
		public int getMaxBranching()
		{
			return maxBranching;
		}
	}

	public class Compound implements Serializable
	{
		private int iGEdgePercentage;
		private int depth;
		private int noOfSplings;
		private double branchFactor;

		public int getIGEdgePercentage()
		{
			return this.iGEdgePercentage;
		}
		public void setIGEdgePercentage(int edgePercentage)
		{
			this.iGEdgePercentage = edgePercentage;
		}
		public int getDepth()
		{
			return this.depth;
		}
		public void setDepth(int depth)
		{
			this.depth = depth;
		}
		public int getNoOfSplings()
		{
			return this.noOfSplings;
		}
		public void setNoOfSiblings(int noOfSplings)
		{
			this.noOfSplings = noOfSplings;
		}
		public double getBranchFactor()
		{
			return this.branchFactor;
		}
		public void setBranchFactor(double branchFactor)
		{
			this.branchFactor = branchFactor;
		}
	}

	public class Cluster implements Serializable
	{
		private int iCEdgePercentage;
		private int maxClusterSize;
		private boolean allowOverlaps;
		private int clusterOverlapFrequency;
		private int clusterOverlapAmount;

		public int getICEdgePercentage()
		{
			return this.iCEdgePercentage;
		}
		public void setICEdgePercentage(int edgePercentage)
		{
			this.iCEdgePercentage = edgePercentage;
		}
		public int getMaxClusterSize()
		{
			return this.maxClusterSize;
		}
		public void setMaxClusterSize(int maxClusterSize)
		{
			this.maxClusterSize = maxClusterSize;
		}
		public boolean isAllowOverlaps()
		{
			return this.allowOverlaps;
		}
		public void setAllowOverlaps(boolean allowOverlaps)
		{
			this.allowOverlaps = allowOverlaps;
		}
		public int getClusterOverlapFrequency() 
		{
			return this.clusterOverlapFrequency;
		}
		public void setClusterOverlapFrequency(int clusterOverlapFrequency) 
		{
			this.clusterOverlapFrequency = clusterOverlapFrequency;
		}
		public int getClusterOverlapAmount() 
		{
			return this.clusterOverlapAmount;
		}
		public void setClusterOverlapAmount(int clusterOverlapAmount) 
		{
			this.clusterOverlapAmount = clusterOverlapAmount;
		}
	}

	private RandomGraphOptionsPack()
	{
		this.compound = new Compound();
		this.flat = new Flat();
		this.cluster = new Cluster();
		this.common = new Common();

		this.setDefaultLayoutProperties();
	}

	public void load()
	{
		try
		{
			ObjectInputStream ois =
				new ObjectInputStream(new FileInputStream(optionsFile));

			instance = (RandomGraphOptionsPack) ois.readObject();
		}
		catch (Exception e)
		{
		}
	}

	public void save()
	{
		try
		{
			ObjectOutputStream oos =
				new ObjectOutputStream(new FileOutputStream(optionsFile));

			oos.writeObject(instance);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setDefaultLayoutProperties()
	{
		this.common.setMaxNodeSize(RandomGraphCreator.MAX_NODE_SIZE);
		this.common.setMinNodeSize(RandomGraphCreator.MIN_NODE_SIZE);
		this.common.setNoOfEdges(RandomGraphCreator.NO_OF_EDGES);
		this.common.setNoOfNodes(RandomGraphCreator.NO_OF_NODES);
		this.common.setGraphType(RandomGraphCreator.FLAT_GRAPH);
		this.common.setRemoveDisconnectedNodes(
			RandomGraphCreator.REMOVE_DISCONNECTED_NODES);

		this.flat.setGraphType(RandomGraphCreator.FLAT_GRAPH_TYPE);
		this.flat.setPruningProbability(RandomGraphCreator.PRUNING_PROBABILITY);
		this.flat.setMinBranching(RandomGraphCreator.MIN_BRANCHING);
		this.flat.setMaxBranching(RandomGraphCreator.MAX_BRANCHING);
		
		this.cluster.setICEdgePercentage(RandomGraphCreator.IC_EDGE_PERCENTAGE);
		this.cluster.setMaxClusterSize(RandomGraphCreator.MAX_CLUSTER_SIZE);
		this.cluster.setClusterOverlapFrequency(RandomGraphCreator.CLUSTER_OVERLAP_FREQUENCY);
		this.cluster.setClusterOverlapAmount(RandomGraphCreator.CLUSTER_OVERLAP_AMOUNT);

		this.compound.setBranchFactor(RandomGraphCreator.BRANCH_FACTOR);
		this.compound.setDepth(RandomGraphCreator.COMPOUND_DEPTH);
		this.compound.setIGEdgePercentage(RandomGraphCreator.IG_EDGE_PERCENTAGE);
		this.compound.setNoOfSiblings(RandomGraphCreator.NO_OF_SIBLINGS);
	}

	public static RandomGraphOptionsPack getInstance()
	{
		if (instance == null)
		{
			instance = new RandomGraphOptionsPack();
		}

		return instance;
	}


	public Cluster getCluster()
	{
		return this.cluster;
	}

	public Compound getCompound()
	{
		return this.compound;
	}

	public Common getCommon()
	{
		return this.common;
	}

	public Flat getFlat()
	{
		return this.flat;
	}

	public void setFlat(Flat flat)
	{
		this.flat = flat;
	}
}