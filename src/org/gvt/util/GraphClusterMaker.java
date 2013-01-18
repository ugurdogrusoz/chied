package org.gvt.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.gvt.ChisioMain;
import org.gvt.model.CompoundModel;
import org.gvt.model.NodeModel;

/**
 * This class is responsible for clustering the given graph. Any existing
 * cluster information will be overridden.
 * Here is how it works:
 * 1. Pick a random node.
 * 2. Collect un-clustered neighbors around this node in breadth-first manner.
 * 	a. If the randomly determined cluster size have not met yet, pick all the
 * 		nodes and continue with the ones in the next breadth-level
 *  b. If including all neighbors in current bredth level exceeds the desired
 *  	cluster size, then pick required number of nodes from this level
 *  	randomly.
 * 3. Repeat steps 1-2 until the desired number of clustered nodes is reached.
 *
 * @author Esat
 *
 */
public class GraphClusterMaker
{
	/**
	 * Constructor
	 * @param graph
	 * @param maxClusterSize
	 * @param unclusteredNodesPercentage
	 */
	public GraphClusterMaker(CompoundModel graph,
		int maxClusterSize,
		int unclusteredNodesPercentage)
	{
		this.maxClusterSize = maxClusterSize;
		this.unclusteredNodesPercentage = unclusteredNodesPercentage;
		this.clusteredNodes = new HashSet<NodeModel>();
		this.rootGraph = graph;
		this.currentClusterNo = 0;
	}

	/**
	 * Run the algorithm
	 */
	public void run()
	{
		// The list that holds the nodes that have not been clustered yet.
		ArrayList<NodeModel> nodes = new ArrayList<NodeModel>(
			this.rootGraph.getChildren());

		// Determine the total number of nodes to be clustered.
		int targetNumberOfClusteredNodes =
			(100 - this.unclusteredNodesPercentage) * nodes.size() / 100;

		// Each iteration of the following loop creates one cluster at most.
		while (this.clusteredNodes.size() < targetNumberOfClusteredNodes)
		{
			int remainingNodesToBeClustered =
				targetNumberOfClusteredNodes - this.clusteredNodes.size();

			// First of all, check whether we have 2 or more nodes. If not, then
			// stop the process.
			if (remainingNodesToBeClustered < 2)
			{
				break;
			}

			// Next determine whether we can find a node that is not clustered
			// and also have some unclustered neighbors.

			int notSuccesfulIterationCount = 0;

			// Pick a random node and iterate starting from this node until
			// the desired node is found.

			int randomIndex = (int) (Math.random() * nodes.size());
			NodeModel node = nodes.get(randomIndex);

			while (notSuccesfulIterationCount < nodes.size() &&
					(this.clusteredNodes.contains(node) ||
							this.getUnClusteredNeighbours(node).size() == 0))
			{
				notSuccesfulIterationCount++;

				node = nodes.get((++randomIndex) % nodes.size());
			}

			// If the loop above terminated because it couldn't find a suitable
			// node, that means we should terminate clustering.
			if (notSuccesfulIterationCount == nodes.size())
			{
				break;
			}

			// Randomly determine a size for this cluster.
			int clusterSize = (int)(Math.random()*(this.maxClusterSize-1)) + 2;

			// If we have already reached the limit, then decrease the cluster
			// size to make it fit to the limit.
			if (clusterSize > remainingNodesToBeClustered)
			{
				clusterSize = remainingNodesToBeClustered;
			}

			// Increase the cluster count and assign cluster to this center node
			this.currentClusterNo ++;
			this.assignClusterToNodes(Collections.singleton(node));

			int remainingNodesInTheCluster = clusterSize-1;

			// Start breadth-first iteration. Keep iteration until cluster
			// is filled or there are no more nodes in the iterate.
			Set<NodeModel> currentBreadth = Collections.singleton(node);

			while (remainingNodesInTheCluster > 0 && currentBreadth.size() > 0)
			{
				Set<NodeModel> allNeighbors = new HashSet<NodeModel>();

				// Collect all neighbors of the nodes in the current breath.
				for (NodeModel breadthNode : currentBreadth)
				{
					allNeighbors.addAll(
						this.getUnClusteredNeighbours(breadthNode));
				}

				if (allNeighbors.size() > remainingNodesInTheCluster)
				{
					// We have more nodes than we need to. Randomly remove
					// some of them, and set clusters for the remaining ones.

					this.randomlyRemoveNodes(
						allNeighbors, remainingNodesInTheCluster);
					this.assignClusterToNodes(allNeighbors);

					// This should stop the iteration for current cluster.
					remainingNodesInTheCluster = 0;
				}
				else
				{
					// We still need more breadths. Pick all the nodes in
					// neighborhood, continue with the next breadth
					this.assignClusterToNodes(allNeighbors);
					remainingNodesInTheCluster -= allNeighbors.size();

					// Next breadth should be current neighbors.
					currentBreadth = allNeighbors;
				}
			}
		}
	}

	/**
	 * This method randomly removes given number of nodes from the given
	 * collection
	 * @param nodes
	 * @param targetSize
	 */
	private void randomlyRemoveNodes(
		Collection<NodeModel> nodes, int targetSize)
	{
		Iterator<NodeModel> iterator = nodes.iterator();

		while (nodes.size() > targetSize)
		{
			if (!iterator.hasNext())
			{
				iterator = nodes.iterator();
			}

			// A node is removed from the iterator by the probability of
			// target size / current size
			if ((targetSize*1.0) / nodes.size() < Math.random())
			{
				iterator.next();
				iterator.remove();
			}
		}
	}

	/**
	 * This method sets cluster numbers of the given nodes to the current
	 * cluster number and adds them to processed nodes.
	 * @param nodes
	 */
	private void assignClusterToNodes(Collection<NodeModel> nodes)
	{
		for (NodeModel node: nodes)
		{
			this.clusteredNodes.add(node);
			node.addCluster(this.currentClusterNo);
		}
	}


	/**
	 * This method returns un-clustered neighbors of the given node.
	 * @param node
	 * @return
	 */
	private List<NodeModel> getUnClusteredNeighbours(NodeModel node)
	{
		List<NodeModel> neighborsList = node.getNeighborsList();

		Iterator<NodeModel> neighborsIterator =neighborsList.iterator();

		while (neighborsIterator.hasNext())
		{
			NodeModel neighbor = (NodeModel) neighborsIterator.next();

			if (this.clusteredNodes.contains(neighbor))
			{
				neighborsIterator.remove();
			}
		}

		return neighborsList;
	}


	/**
	 * Holds the nodes that have been clustered.
	 */
	private Set<NodeModel> clusteredNodes;

	/**
	 * The graph which will be clustered.
	 */
	private CompoundModel rootGraph;

	/**
	 * Holds max number of nodes in a cluster. Min number is 2.
	 */
	private int maxClusterSize;

	/**
	 * Holds the percentage of the number of un-clustered nodes over total
	 * number of nodes in the graph.
	 */
	private int unclusteredNodesPercentage;

	/**
	 * Holds the cluster number that is currently processed.
	 */
	private int currentClusterNo = 0;
}