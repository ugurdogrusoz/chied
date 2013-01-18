package org.gvt.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.gvt.ChisioMain;
import org.gvt.command.CreateCommand;
import org.gvt.command.CreateConnectionCommand;
import org.gvt.model.CompoundModel;
import org.gvt.model.ECluster;
import org.gvt.model.EdgeModel;
import org.gvt.model.NodeModel;
import org.ivis.layout.Cluster;
import org.ivis.layout.ClusterManager;
import org.ivis.layout.Clustered;

/**
 * This method creates a random compound graph w/ properties specified by
 * parameters to its constructor. When a depth of zero is provided, a simple
 * graph (no multi-edges) w/out any compound nodes is created. In the latter
 * case,
 *
 * @author Esat Belviranli
 * @author Erhan Giral
 * @author Ugur Dogrusoz
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class RandomGraphCreator
{
// -----------------------------------------------------------------------------
// Section: Instance Variables
// -----------------------------------------------------------------------------
	public enum FlatGraphType {MESH, TREE, GENERAL}
	
	/**
	 * Various parameters for the random graph to be created
	 */
	private int noOfNodes;
	private int noOfEdges;
	private int maxDepth;
	private int noOfSiblings;
	private int maxClusterSize;
	private int minClusterSize;
	private boolean allowOverlaps;
	private int clusterOverlapFrequency;
	private int clusterOverlapAmount;
	private double percentageOfIGEs;
	private double probabilityOfBranchPrunning;
	private Dimension minNodeSize;
	private Dimension maxNodeSize;
	private boolean removeDisconnectedNodes;
	private FlatGraphType graphType;
	private int minNumberOfChildren;
	private int maxNumberOfChildren;
	private double prunningProbability;

	/**
	 * The root chisio model that will hold all nodes and edges.
	 */
	private CompoundModel rootModel= null;

	/**
	 * Intermediate data used for construction
	 */
	private int noOfCreatedNodes;
	private int noOfcreatedEdges;
	private int noOfGraphs = 0;
	private int[][] adjacencyMatrix;
	private Random random = new Random(System.currentTimeMillis());
	private List<CompoundModel> graphs;
	private List<NodeModel> nodes;
	private List<EdgeModel> interGraphEdges;
	private List<EdgeModel> edges;

	/**
	 * Needed to get the current size of the canvas window
	 */
	private ChisioMain main;

// -----------------------------------------------------------------------------
// Section: Constructors and Initialization
// -----------------------------------------------------------------------------
	/**
	 * This public constructor accepts the parameters of the random compound
	 * graph to be created.
	 *
	 * @param noOfNodes total number of nodes
	 * @param noOfEdges total number of edges
	 * @param depth maximum depth of the compound graph to be created
	 * @param percentageOfIGEs proportion of the inter-graph edges to all edges
	 * @param noOfSiblings maximum branching (no of children of a node) in the
	 * 			nesting tree
	 * @param probabilityOfBranchPruning probability of pruning a child in the
	 * 			nesting tree to avoid nesting trees too uniform in structure
	 * @param maxClusterSize Used when depth = 0 for randomly clustering the
	 * 			nodes.
	 * @param minNodeSize minimum node size for random node creation.
	 * @param maxNodeSize maximum node size for random node creation.
	 */
	public RandomGraphCreator(int noOfNodes,
		int noOfEdges,
		int depth,
		double percentageOfIGEs,
		int noOfSiblings,
		double probabilityOfBranchPruning,
		int maxClusterSize,
		int minClusterSize,
		boolean allowOverlaps,
		int clusterOverlapFrequency,
		int clusterOverlapAmount,
		Dimension minNodeSize,
		Dimension maxNodeSize,
		boolean removeDisconnectedNodes,
		FlatGraphType graphType,
		int minNumberOfChildren,
		int maxNumberOfChildren,
		double prunningProbability)
	{
		this (null,
			noOfNodes,
			noOfEdges,
			depth,
			percentageOfIGEs,
			noOfSiblings,
			probabilityOfBranchPruning,
			maxClusterSize,
			minClusterSize,
			allowOverlaps,
			clusterOverlapFrequency,
			clusterOverlapAmount,
			minNodeSize,
			maxNodeSize,
			removeDisconnectedNodes,
			graphType,
			minNumberOfChildren,
			maxNumberOfChildren,
			prunningProbability);
	}

	/**
	 * This public constructor accepts the parameters of the random compound
	 * graph to be created.
	 *
	 * @param noOfNodes total number of nodes
	 * @param noOfEdges total number of edges
	 * @param depth maximum depth of the compound graph to be created
	 * @param percentageOfIGEs proportion of the inter-graph edges to all edges
	 * @param noOfSiblings maximum branching (no of children of a node) in the
	 * 			nesting tree
	 * @param probabilityOfBranchPruning probability of pruning a child in the
	 * 			nesting tree to avoid nesting trees too uniform in structure
	 * @param maxClusterSize Used when depth = 0 for randomly clustering the
	 * 			nodes.
	 * @param minNodeSize minimum node size for random node creation.
	 * @param maxNodeSize maximum node size for random node creation.
	 */
	public RandomGraphCreator(ChisioMain main,
		int noOfNodes,
		int noOfEdges,
		int depth,
		double percentageOfIGEs,
		int noOfSiblings,
		double probabilityOfBranchPruning,
		int maxClusterSize,
		int minClusterSize,
		boolean allowOverlaps,
		int clusterOverlapFrequency,
		int clusterOverlapAmount,
		Dimension minNodeSize,
		Dimension maxNodeSize,
		boolean removeDisconnectedNodes,
		FlatGraphType graphType,
		int minNumberOfChildren,
		int maxNumberOfChildren,
		double prunningProbability)
	{
		this.main = main;
		this.noOfNodes = noOfNodes;
		this.noOfEdges = noOfEdges;
		this.maxDepth = depth;
		this.graphs = new ArrayList<CompoundModel>();
		this.nodes = new ArrayList<NodeModel>();
		this.noOfGraphs = 0;
		this.interGraphEdges = new LinkedList<EdgeModel>();
		this.edges = new LinkedList<EdgeModel>();
		this.removeDisconnectedNodes = removeDisconnectedNodes;

		this.graphType = graphType;
		this.minNumberOfChildren = minNumberOfChildren;
		this.maxNumberOfChildren = Math.max(maxNumberOfChildren, minNumberOfChildren);
		this.prunningProbability = prunningProbability;
		
		this.percentageOfIGEs = percentageOfIGEs;
		this.noOfSiblings = noOfSiblings;
		this.probabilityOfBranchPrunning = probabilityOfBranchPruning;

		this.maxClusterSize = maxClusterSize;
		this.minClusterSize = minClusterSize;
		this.allowOverlaps = allowOverlaps;
		this.clusterOverlapFrequency = clusterOverlapFrequency;
		this.clusterOverlapAmount = clusterOverlapAmount;
		this.minNodeSize = minNodeSize;
		this.maxNodeSize = maxNodeSize;

		// Used for quick accessing currently created edges.
		this.adjacencyMatrix = new int[noOfNodes][noOfNodes];
	}

	public static void main(String[] args)
	{
	}

// -----------------------------------------------------------------------------
// Section: Remaining Methods
// -----------------------------------------------------------------------------
	/**
	 * This method creates a random graph w/ specified parameters into its
	 * public constructor.
	 */
	public CompoundModel run()
	{
		// First create the root model.
		this.noOfCreatedNodes = 0;
		CompoundModel currentGraph = new CompoundModel();
		currentGraph.setAsRoot();
		this.rootModel = currentGraph;
		this.graphs.add(currentGraph);
		this.noOfGraphs++;

		if (this.maxDepth > 0)
		{
			this.growRoot();
			this.createEdges();
		}
		else // no compounds!
		{
			if (this.graphType == FlatGraphType.MESH)
			{
				this.generateMesh();
			}
			else if (this.graphType == FlatGraphType.TREE)
			{
				this.generateTree();
			}
			else
			{
				this.createNonCompoundRandomGraph();
			}
		}

		return this.rootModel;
	}

	private void createEdges()
	{
		int noOfIGEs =
			(int) Math.floor(this.noOfEdges * this.percentageOfIGEs);

		int createdIGEs = 0;
		int trial = 0;

		while (createdIGEs < noOfIGEs)
		{
			EdgeModel ige = this.createRandomIGE();
			trial++;

			if (trial > 1000)
			{
				break;
			}

			if (ige != null)
			{
				this.interGraphEdges.add(ige);
				createdIGEs++;
			}
		}

		this.noOfcreatedEdges = createdIGEs;

		this.createRandomEdges();
	}

	private void createRandomEdges()
	{
		Object[] graphArray = this.graphs.toArray();
		int trial = 0;
		boolean success;

		while (this.noOfcreatedEdges < this.noOfEdges)
		{
			trial++;

			if (trial > this.noOfEdges)
			{
				break;
			}

			success = false;

			for (int index = 0; index < graphArray.length; index++)
			{
				CompoundModel graph = (CompoundModel) graphArray[index];

				if (graph.getChildren().size() < 2)
				{
					continue;
				}

				boolean tmp = false;

				if (this.noOfcreatedEdges < this.noOfEdges)
				{
					tmp = this.createRandomEdge(graph);
				}

				success = success || tmp;
			}

			if (!success)
			{
				return;
			}
		}
	}

	private boolean createRandomEdge(CompoundModel compound)
	{
		Object[] nodes = compound.getChildren().toArray();
		HashSet<Integer> indexPot = new HashSet<Integer>();
		
		// create indexPot
		for (int i = 0; i < nodes.length; i++)
		{
			indexPot.add(new Integer(i));
		}
		
		while (!indexPot.isEmpty())
		{
			// randomly choose indexes of source and target nodes
			int firstNodeIndex = this.random.nextInt(nodes.length);
			int secondNodeIndex = -1;
			
			do {
				secondNodeIndex = this.random.nextInt(nodes.length);
			} while (firstNodeIndex == secondNodeIndex);
			
			// remove selected indexes
			indexPot.remove(firstNodeIndex);
			indexPot.remove(secondNodeIndex);
			
			NodeModel firstNode = (NodeModel) nodes[firstNodeIndex];
			NodeModel secondNode = (NodeModel) nodes[secondNodeIndex];

			if (!this.edgeExists(this.nodes.indexOf(firstNode),
				this.nodes.indexOf(secondNode)))
			{
				this.createEdge(firstNode, secondNode);
				return true;
			}
		}
		
		return false;
	}

	private EdgeModel createRandomIGE()
	{
		boolean success = false;
		EdgeModel edge = null;
		int trials = 0;

		while (!success)
		{
			trials++;

			if (trials > this.noOfGraphs * this.noOfGraphs)
			{
				break;
			}

			success = true;

			int firstNodeID = this.random.nextInt(this.noOfNodes);
			int secondNodeID = this.random.nextInt(this.noOfNodes);

			NodeModel firstNode = (NodeModel) this.nodes.get(firstNodeID);
			NodeModel secondNode = (NodeModel) this.nodes.get(secondNodeID);

			if (firstNode.getParentModel() == null ||
				secondNode.getParentModel() == null)
			{
				success = false;
				break;
			}
			// still, one might be an ancestor of the other (not desired)!
			else if (isOneAncestorOfOther(firstNode, secondNode))
			{
				success = false;
				break;
			}

			if (firstNode.getParentModel() == secondNode.getParentModel())
			{
				success = false;
				break;
			}

			Iterator<EdgeModel> iterator =
				firstNode.getTargetConnections().iterator();

			while (iterator.hasNext())
			{
				EdgeModel chisioEdge = (EdgeModel) iterator.next();
				NodeModel source = (NodeModel) chisioEdge.getSource();

				if (source == secondNode)
				{
					success = false;
					break;
				}
			}

			if (success == false)
			{
				break;
			}

			iterator = firstNode.getSourceConnections().iterator();

			while (iterator.hasNext())
			{
				EdgeModel chisioEdge = (EdgeModel) iterator.next();
				NodeModel target = (NodeModel) chisioEdge.getTarget();

				if (target == secondNode)
				{
					success = false;
					break;
				}
			}

			if (success == false)
			{
				break;
			}
			edge = this.createEdge(firstNode, secondNode);
		}

		return edge;
	}

	private void growRoot()
	{
		this.growBranch(this.rootModel, 1);
		this.fillTree();
	}

	private void fillTree()
	{
		// Detect the leaf compounds first. Those are the ones which have no
		// children.
		LinkedList<CompoundModel> leafCompounds =
			new LinkedList<CompoundModel>();

		for (CompoundModel graph : this.graphs)
		{
			if (graph.getChildren().isEmpty())
			{
				leafCompounds.add(graph);
			}
		}

		while (this.noOfCreatedNodes < this.noOfNodes)
		{
			int selectedGraphID = this.random.nextInt(this.noOfGraphs);

			CompoundModel selectedGraph =
				(CompoundModel) this.graphs.get(selectedGraphID);

			// Priority is for empty leaf compounds. If the graph is not empty
			// chose a leaf compound as parent to add a new node.
			if (!selectedGraph.getChildren().isEmpty() &&
				!leafCompounds.isEmpty())
			{
				selectedGraph = leafCompounds.remove();
			}

			this.addNode(selectedGraph,false);
		}
	}

	private void growBranch(CompoundModel currentGraph, int depth)
	{
		// Here we create a level of inclusion tree

		// Since we don't have a mechanism to convert a simple node to a
		// compound node, we directly create compound nodes. However, while
		// creating compounds, if max number of nodes are reached, they will
		// be empty. Therefore, we detect this condition before creating
		// compounds.
		if (this.noOfSiblings >= (this.noOfNodes - this.nodes.size()))
		{
			return;
		}

		for (int i = 0; i < this.noOfSiblings; i++)
		{
			if (this.probabilityOfBranchPrunning < this.random.nextDouble())
			{
				if (this.nodes.size() < this.noOfNodes)
				{
					this.addNode(currentGraph,true);
				}
				else
				{
					// this condition should not be reached.
					return;
				}
			}
		}


		Iterator<CompoundModel> iterator = currentGraph.getNodes().iterator();

		while (iterator.hasNext())
		{
			CompoundModel childGraph = (CompoundModel) iterator.next();

			this.graphs.add(childGraph);
			this.noOfGraphs++;

			if (depth < this.maxDepth)
			{
				this.growBranch(childGraph, depth + 1);
			}
		}
	}

	private void generateMesh()
	{
		int actualNoOfNodes = (int) (this.noOfNodes * NODES_FACTOR_FOR_MESH);

		int meshLength = (int) Math.sqrt(actualNoOfNodes);

		if (meshLength*meshLength < actualNoOfNodes)
		{
			meshLength++;
			actualNoOfNodes = meshLength * meshLength;
		}

		this.adjacencyMatrix = new int[actualNoOfNodes][actualNoOfNodes*3];

		NodeModel[][] mesh = new NodeModel[meshLength][meshLength];

		// i is for rows, j is for columns
		for (int i = 0; i < mesh.length; i++)
		{
			for (int j = 0; j < mesh[i].length; j++)
			{
				NodeModel node = this.addNode(this.rootModel, false);
				mesh[i][j] = node;

				node.setLocation(new Point(this.maxNodeSize.height * 2 * i,
					this.maxNodeSize.width * 2 * j));

				// Create the vertical edge
				if (i > 0)
				{
					this.createEdge(mesh[i][j], mesh[i-1][j]);
				}

				// Create the horizontal edge
				if (j > 0)
				{
					this.createEdge(mesh[i][j], mesh[i][j-1]);
				}

				// Create the diagonal, randomly determine the slope.
				if (i > 0 && j > 0)
				{
					if (Math.random() < 0.5)
					{
						this.createEdge(mesh[i][j], mesh[i-1][j-1]);
					}
					else
					{
						this.createEdge(mesh[i-1][j], mesh[i][j-1]);
					}
				}
			}
		}

//		System.out.println("No of edges before node removal: "+this.edges.size());
//		System.out.println("No of nodes before node removal: "+this.nodes.size());

		// Now randomly remove nodes, so that desired node count is satisfied.
		while (actualNoOfNodes > this.noOfNodes)
		{
			int i = (int) (Math.random() * meshLength);
			int j = (int) (Math.random() * meshLength);

			NodeModel node = mesh[i][j];
			if (node != null)
			{
				List<EdgeModel> sourceEdges = node.getSourceConnections();
				List<EdgeModel> targetEdges = node.getTargetConnections();

				for (EdgeModel edge : targetEdges)
				{
					edge.getSource().removeSourceConnection(edge);
				}

				for (EdgeModel edge : sourceEdges)
				{
					edge.getTarget().removeTargetConnection(edge);
				}

				this.rootModel.removeChild(node);
				this.nodes.remove(node);

				if (this.isGraphConnected())
				{
					// Graph is still connected, perform rest.

					this.edges.removeAll(sourceEdges);
					this.edges.removeAll(targetEdges);

					while (!sourceEdges.isEmpty())
					{
						node.removeSourceConnection(sourceEdges.get(0));
					}
					while (!targetEdges.isEmpty())
					{
						node.removeTargetConnection(targetEdges.get(0));
					}

					actualNoOfNodes--;
				}
				else
				{
					// Graph becomes disconnected, undo changes.

					this.rootModel.addChild(node);
					this.nodes.add(node);

					for (EdgeModel edge : targetEdges)
					{
						edge.getSource().addSourceConnection(edge);
					}

					for (EdgeModel edge : sourceEdges)
					{
						edge.getTarget().addTargetConnection(edge);
					}
				}
			}
		}

		// Keep a variable to prevent infinite loop. Who says "halting
		// problem" is a problem? Cheers to Alan Turing...
		int noOfStuckedIterations = 0;

//		System.out.println("No of edges after node removal: "+this.edges.size());
//		System.out.println("No of nodes after node removal: "+this.nodes.size());

		// Now randomly remove edges, but keep connectivity meanwhile.
		while(this.edges.size() > this.noOfEdges &&
			noOfStuckedIterations < this.edges.size()*this.edges.size())
		{
			int edgeIndex = (int) (Math.random() * this.edges.size());

			EdgeModel edge = this.edges.get(edgeIndex);
			edge.getTarget().removeTargetConnection(edge);
			edge.getSource().removeSourceConnection(edge);

			this.edges.remove(edgeIndex);

			if (! this.isGraphConnected())
			{
				this.edges.add(edge);
				edge.getTarget().addTargetConnection(edge);
				edge.getSource().addSourceConnection(edge);

				noOfStuckedIterations ++;
			}
			else
			{
				noOfStuckedIterations = 0;
			}
		}

//		System.out.println("No of edges after edge removal: "+this.edges.size());
	}

	private void generateTree()
	{
		
		Queue Q = new LinkedList<NodeModel>();
		LinkedList currentLevelOfTree = new LinkedList<NodeModel>();
		int numOfTries = 0;
		double prunProb = this.prunningProbability;
		int currIndex = 0;
		NodeModel childNode = null;
		
		this.adjacencyMatrix = 
			new int[this.noOfNodes+this.maxNumberOfChildren][this.noOfNodes+this.maxNumberOfChildren];
		
		// create root node
		Q.add(this.addNode(this.rootModel,false));
		
		System.out.println("Prun prob: " + prunProb);
		
		while (true)
		{
			NodeModel currNode = (NodeModel) Q.remove();
			currentLevelOfTree.add(currNode);
			prunProb = this.prunningProbability * (1-(0.1 * numOfTries));
			
			// check if current node will have children
			if ((double)(this.random.nextInt(100) + 1) / 100.0 >= prunProb)
			{
				int numberOfChildren = 
					this.random.nextInt(this.maxNumberOfChildren - this.minNumberOfChildren + 1)
					+ this.minNumberOfChildren;
				
				// add children
				for (int i = 0; i < numberOfChildren; i++)
				{
					// create node & edge
					childNode = this.addNode(rootModel, false);
					this.createEdge(currNode, childNode);
					
					// add to queue
					Q.add(childNode);
				}
				
				numOfTries = 0;
				currentLevelOfTree.clear();
			}
			
			if ( this.nodes.size() >= this.noOfNodes)
			{
				break;
			}
			else
			{
				if (Q.isEmpty())
				{
					Q.addAll(currentLevelOfTree);
					currentLevelOfTree.clear();
					numOfTries++;
				}
			}
		}
	}

	/**
	 * This method checks whether currently created root graph is
	 * connected or not. It simply traverses the graph in a DFS manner
	 * @return
	 */
	private boolean isGraphConnected()
	{
		Set<NodeModel> traversedNodes = new HashSet<NodeModel>();

		NodeModel node = this.nodes.get(0);

		this.traverse(node, traversedNodes);

		return this.nodes.size() == traversedNodes.size();
	}

	/**
	 * This recursive method traverses unvisited nodes, starting from
	 * the given node
	 * @param node
	 * @param traversed
	 */
	private void traverse(NodeModel node, Set<NodeModel> traversed)
	{
		traversed.add(node);

		for (Object neighbour : node.getNeighborsList())
		{
			if (!traversed.contains(neighbour))
			{
				this.traverse((NodeModel) neighbour, traversed);
			}
		}
	}

	/**
	 * This method creates a non-compound random graph with current parameters.
	 * It should be called when maximum depth is given as zero.
	 * The method creates a flat un-clustered graph when max cluster size is
	 * smaller than or equal to 1.
	 */
	private void createNonCompoundRandomGraph()
	{
		NodeModel [] nodes = new NodeModel[this.noOfNodes];

		for (int i = 0; i < this.noOfNodes; i++)
		{
			nodes[i] = this.addNode(this.rootModel,false);
		}

		// Variable for holding remaining number of nodes.
		int remainingNodes = this.noOfNodes;

		// A copy of the node list for cluster assignment
		LinkedList<NodeModel> nodeList = new LinkedList<NodeModel>();
		nodeList.addAll(this.nodes);

		// Determine a cluster size for currentClusterNo and assign this size of
		// nodes to this cluster.
		int currentClusterNo = 0;

		while (remainingNodes > 0)
		{
			int currentClusterSize;

			// If max cluster size is greater than 1, then there should be
			// some clusters
			if (this.maxClusterSize > 1)
			{
				if (remainingNodes > this.maxClusterSize)
				{
					currentClusterSize = this.random.nextInt(
						this.maxClusterSize - this.minClusterSize);
					currentClusterSize += this.minClusterSize;
				}
				else
				{
					currentClusterSize = remainingNodes;
				}
			}
			// If max cluster size is smaller than or equal to 1, then all
			// nodes should be unclustered
			else
			{
				currentClusterSize = this.noOfNodes;
			}

			Color clusterColor = new Color(null,
					this.random.nextInt(256),
					this.random.nextInt(256),
					this.random.nextInt(256));

			for (int i=0; i < currentClusterSize; i++)
			{
				NodeModel currentNode =
					nodeList.get(this.random.nextInt(nodeList.size()));
				
				if (currentClusterNo != 0)
				{
					currentNode.addCluster(currentClusterNo);
				
					((ECluster)currentNode.getParentModel().getClusterManager().
						getClusterByID(currentClusterNo)).
						setHighlightColor(clusterColor);

					currentNode.setColor(clusterColor);
				}

				nodeList.remove(currentNode);
			}

			remainingNodes -= currentClusterSize;

			// All clusters should have at least MINCLUSTERSIZE nodes.
			if (currentClusterSize >= this.minClusterSize)
			{
				currentClusterNo++;
			}
		}

		// merge some clusters to obtain overlapping clusters
		if (this.allowOverlaps)
		{
			mergeClusters(this.clusterOverlapFrequency, this.clusterOverlapAmount);
		}
		
		int iterationCount = 0;
		int remainingInterClusterEdges = (int) (this.percentageOfIGEs * this.noOfEdges);
		int remainingIntraClusterEdges =
			this.noOfEdges - remainingInterClusterEdges;

		while (remainingInterClusterEdges + remainingIntraClusterEdges > 0)
		{
			int firstNodeID = this.random.nextInt(this.noOfNodes);
			int secondNodeID = this.random.nextInt(this.noOfNodes);

			if (!this.edgeExists(firstNodeID, secondNodeID) &&
				firstNodeID != secondNodeID)
			{
				NodeModel srcNode = nodes[firstNodeID];
				NodeModel trgNode = nodes[secondNodeID];

				// Special handling for unclustered nodes
				if (srcNode.getClusters().isEmpty() ||
					trgNode.getClusters().isEmpty() )
				{
					if (remainingInterClusterEdges > 0)
					{
						this.createEdge(srcNode, trgNode);
						remainingInterClusterEdges--;
					}
				}
				else 
				{
					// if there is at least one common cluster, 
					// then edge is intraCluster
					if ( srcNode.hasCommonCluster(trgNode) )
					{
						if (remainingIntraClusterEdges > 0)
						{
							this.createEdge(srcNode, trgNode);
							remainingIntraClusterEdges--;
						}
					}
					else
					{
						if (remainingInterClusterEdges > 0)
						{
							this.createEdge(srcNode, trgNode);
							remainingInterClusterEdges--;
						}
					}
				}
			}

			if (iterationCount > (this.noOfNodes*this.noOfNodes))
			{
				break;
			}
			else
			{
				iterationCount++;
			}
		}

		if (this.removeDisconnectedNodes)
		{
			for (NodeModel node : this.nodes)
			{
				if (node.getSourceConnections().size() == 0 &&
						node.getTargetConnections().size() == 0)
				{
					node.getParentModel().removeChild(node);
				}
			}
		}
	}
	
	/**
	 * This method merges clusters of non-compound graphs
	 * 
	 * @param clusterOverlapFrequency : what amount of clusters should be merged
	 * @param clusterOverlapAmount : what amount of nodes should be merged
	 */
	private void mergeClusters(int clusterOverlapFrequency, 
			int clusterOverlapAmount)
	{
		// get ClusterManager of the graph
		ClusterManager cm = this.rootModel.getClusterManager();
		
		// get clusters of the graph
		ArrayList<Cluster> clusters = cm.getClusters();
		
		// number of cluster merges
		int clusterMergeLeft = 
			(clusters.size() * clusterOverlapFrequency) / 100;
		int noOfClusters = clusters.size();
		
		// start merging random clusters
		while (clusterMergeLeft > 0) 
		{
			// choose random two different clusters
			int clusterIDA = 1 + this.random.nextInt(noOfClusters - 1);
			int clusterIDB = 1 + this.random.nextInt(noOfClusters - 1);
			
			while (noOfClusters > 1 && clusterIDA == clusterIDB)
			{
				clusterIDB = 1 + this.random.nextInt(noOfClusters - 1);
			}
			
			// get 2 random clusters
			Cluster clusterA = clusters.get(clusterIDA);
			Cluster clusterB = clusters.get(clusterIDB);
			
			// get their sizes
			int sizeA = clusterA.getNodes().size();
			int sizeB = clusterB.getNodes().size();
			
			// decide how many nodes to merge
			int nodeMergeLeft = 
				(Math.min(sizeA, sizeB) * clusterOverlapAmount) / 100;
			
			while (nodeMergeLeft > 0)
			{
				int mergeNodeID = this.random.nextInt(sizeA + sizeB);
				
				int counter = 0;
				
				if (mergeNodeID < sizeA)
				{
					// get i'the element of the HashSet
					Iterator<Clustered> iter = clusterA.getNodes().iterator();
					
					while (counter < mergeNodeID)
					{
						iter.next();
						counter++;
					}
					Clustered mergeNode = (Clustered) iter.next();
					
					// add to clusterB
					mergeNode.addCluster(clusterB);
				}
				else
				{
					mergeNodeID -= sizeA;
					
					// get i'the element of the HashSet
					Iterator<Clustered> iter = clusterB.getNodes().iterator();
					while (counter < mergeNodeID)
					{
						iter.next();
						counter++;
					}
					Clustered mergeNode = (Clustered) iter.next();
					
					// add to clusterB
					mergeNode.addCluster(clusterA);
				}
				
				nodeMergeLeft--;
			}
			
			clusterMergeLeft--;
		}
	}
	
	private NodeModel addNode(CompoundModel parentCompound, boolean isCompound)
	{
		NodeModel chisioNode;

		if (isCompound)
		{
			chisioNode = new CompoundModel();
		}
		else
		{
			chisioNode = new NodeModel();
		}

		this.nodes.add(new Integer(this.noOfCreatedNodes), chisioNode);
		this.noOfCreatedNodes++;

		int width = this.random.nextInt(this.maxNodeSize.width -
			this.minNodeSize.width + 1) ;
		int height = this.random.nextInt(this.maxNodeSize.height -
			this.minNodeSize.height + 1);
		width += this.minNodeSize.width;
		height += this.minNodeSize.height;

		chisioNode.setSize(new Dimension(width,height));
		chisioNode.setText(Integer.toString(this.noOfCreatedNodes));

		if (this.main != null)
		{
			// Randomly distribute nodes on the canvas by looking at the current
			// size of the canvas window (minus some buffer for the scrollbars)

			Dimension canvasDim = new Dimension(
					this.main.getViewer().getControl().getSize().x,
					this.main.getViewer().getControl().getSize().y);
			chisioNode.setLocation(new Point(
					this.random.nextInt(canvasDim.width - this.maxNodeSize.width - 25),
					this.random.nextInt(canvasDim.height - this.maxNodeSize.height - 25)));
		}

		// Calculate text size according to digits in the label

		int nodeCount = this.noOfCreatedNodes;
		int noOfDigits = 0;

		while (nodeCount > 0)
		{
			nodeCount /= 10;
			noOfDigits++;
		}

		if (noOfDigits == 1)
		{
			noOfDigits++;
		}

		int textSize = width/noOfDigits;

		chisioNode.setTextFont(new Font(null, "Arial", textSize, 0));

		Color color = this.getColorAtDepth(chisioNode);
		chisioNode.setBorderColor(color);

		if (!isCompound)
		{
			chisioNode.setColor(color);
		}

		CreateCommand cmd = new CreateCommand(parentCompound, chisioNode);
		cmd.execute();

		return chisioNode;
	}

	/**
	 * This method maps the depth of this node to a color to color code depth
	 * of nodes.
	 */
	private Color getColorAtDepth(NodeModel node)
	{
		Color color;
		int depth = this.getDepth(node);

		switch (depth)
		{
			case 0:
				color = NodeModel.DEFAULT_COLOR;
				break;

			case 1:
				color = new Color(null, 14, 148, 157);
				break;

			case 2:
				color = new Color(null, 251, 152, 6);
				break;

			default:
				color = new Color(null, 130, 6, 56);
		}

		return color;
	}

	/**
	 * This method returns the depth of this node in the randomly created
	 * compound graph. It would have been nicer to store this during creation
	 * but this operation doesn't need to be fast anyway!
	 */
	private int getDepth(NodeModel node)
	{
		int depth = 0;
		CompoundModel parentCompound = node.getParentModel();

		while (parentCompound != null)
		{
			node = (NodeModel) parentCompound.getParentModel();

			if (node == null)
			{
				break;
			}

			depth++;
			parentCompound = node.getParentModel();
		};

		return depth;
	}

	/**
	 * This method checks whether one of the input nodes is an ancestor of the
	 * other one (and vice versa) in the nesting tree. Such pairs of nodes
	 * should not be allowed to be joined by edges.
	 */
	public static boolean isOneAncestorOfOther(NodeModel firstNode,
		NodeModel secondNode)
	{
		assert firstNode != null && secondNode != null;

		if (firstNode == secondNode)
		{
			return true;
		}

		// Is second node an ancestor of the first one?

		CompoundModel parentNode = firstNode.getParentModel();

		do
		{
			if (parentNode == null)
			{
				break;
			}

			if (parentNode == secondNode)
			{
				return true;
			}

			parentNode = parentNode.getParentModel();
		} while (true);

		// Is first node an ancestor of the second one?

		parentNode = secondNode.getParentModel();

		do
		{
			if (parentNode == null)
			{
				break;
			}

			if (parentNode == firstNode)
			{
				return true;
			}

			parentNode = parentNode.getParentModel();
		} while (true);

		return false;
	}

	/**
	 * Determines whether an edge is created before for the given node indices.
	 */
	private boolean edgeExists(int srcIndex, int trgIndex)
	{
		boolean result = false;

		if (this.adjacencyMatrix[srcIndex][trgIndex] == 1 ||
			this.adjacencyMatrix[trgIndex][srcIndex] == 1 )
		{
			result = true;
		}

		return result;
	}

	/**
	 * Creates an edge with given source and target nodes. All checkings are
	 * assumed to be done before call.
	 */
	private EdgeModel createEdge(NodeModel src, NodeModel trgt)
	{
		assert src != null && trgt != null;
		assert src != trgt;

		EdgeModel edge = new EdgeModel();
		CreateConnectionCommand command = new CreateConnectionCommand();
		command.setSource(src);
		command.setTarget(trgt);
		command.setConnection(edge);
		command.execute();

		this.edges.add(edge);
		this.noOfcreatedEdges++;
		int srcIndex = this.nodes.indexOf(src);
		int trgtIndex = this.nodes.indexOf(trgt);
		
		this.adjacencyMatrix[srcIndex][trgtIndex] = 1;
		this.adjacencyMatrix[trgtIndex][srcIndex] = 1;

		if (this.maxDepth == 0)
		{
			if ( src.hasCommonCluster(src) )
			{
				edge.setColor(ColorConstants.lightGray);
			}
		}
		else
		{
			if (edge.isIntragraph())
			{
				edge.setColor(ColorConstants.lightGray);
			}
		}

		return edge;
	}

// -----------------------------------------------------------------------------
// Section: Static Variables
// -----------------------------------------------------------------------------

	// Default Values for paramaters

	// Commmon parameters

	public static int NO_OF_NODES = 40;
	public static int NO_OF_EDGES = 50;
	public static Dimension MIN_NODE_SIZE = new Dimension(40, 40);
	public static Dimension MAX_NODE_SIZE = new Dimension(40, 40);
	public static boolean REMOVE_DISCONNECTED_NODES= true;

	public static int FLAT_GRAPH = 0;
	public static int CLUSTER_GRAPH = 1;
	public static int COMPOUND_GRAPH = 2;

	// Flat graph related parameters
	public static FlatGraphType FLAT_GRAPH_TYPE = FlatGraphType.GENERAL;

	// Tree graph related parameters
	public static double PRUNING_PROBABILITY = 0.5;
	public static int MIN_BRANCHING = 1;
	public static int MAX_BRANCHING = 4;
	
	// Cluster graph related parameters

	public static int IC_EDGE_PERCENTAGE = 10;
	public static int MAX_CLUSTER_SIZE = 10;
	public static int CLUSTER_OVERLAP_FREQUENCY = 30;
	public static int CLUSTER_OVERLAP_AMOUNT = 50;

	// Compound graph related parameters

	public static int IG_EDGE_PERCENTAGE = 25;
	public static int COMPOUND_DEPTH = 3;
	public static int NO_OF_SIBLINGS = 5;
	public static double BRANCH_FACTOR = 0.4;

	private static double NODES_FACTOR_FOR_MESH = 1.5;
}