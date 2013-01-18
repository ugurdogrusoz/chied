package org.gvt.layout;

import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.gvt.ChisioMain;
import org.gvt.LayoutManager;
import org.gvt.command.CreateConnectionCommand;
import org.gvt.model.*;
import org.gvt.util.GraphMLReader;
import org.gvt.util.GraphMLWriter;
import org.gvt.util.RandomGraphCreator;
import org.gvt.util.XMLReader;
import org.gvt.util.XMLWriter;
import org.ivis.layout.cose.CoSELayout;
import org.ivis.layout.Layout;
import org.ivis.layout.Cluster;
import org.ivis.layout.six.SixCircularLayout;
import org.ivis.layout.cise.CiSELayout;

/**
 * This class analyzes the performance of our compound graph layout algorithm
 * through various execution time and drawing quality tests using randomly
 * created compound graphs.
 *
 * @author Erhan Giral
 * @author Ugur Dogrusoz
 * @author Esat Belviranli
 *
 * Copyright: I-Vis Research Group, Bilkent University, 2007 - present
 */
public class PerformanceAnalyzer
{
// -----------------------------------------------------------------------------
// Section: Constructors and Initialization
// -----------------------------------------------------------------------------
	public PerformanceAnalyzer()
	{
		this(null);
	}

	public PerformanceAnalyzer(ChisioMain main)
	{
		this.main = main;
		this.resetTestValues();
	}

	private void resetTestValues()
	{
		this.rootGraph = null;
		this.layoutStyle = LayoutStyle.CoSE;
		this.noOfNodes = 70;
		this.edgeNodeRatio = 1.5;
		this.noOfEdges = (int) (this.noOfNodes * this.edgeNodeRatio);
		this.depth = 0;
		this.igeEdgeRatio = 0.10;
		this.probabilityOfBranchPruning = 0.33;
		this.noOfBranches = 3;
		this.minNodeSize = new Dimension(40, 40);
		this.maxNodeSize = new Dimension(40, 40);
		this.generateMesh = false;
		this.flatGraphType = RandomGraphCreator.FlatGraphType.GENERAL;
		this.maxClusterSize = 15;
		this.clusterSizeDiscrepancy = 13;
		this.removeDisconnectedNodes = false;
		this.minNumberOfChildren = 2;
		this.maxNumberOfChildren = 4;
		this.pruningProbability = 0.35;
	}

	private Layout getLayout()
	{
		if (this.layout == null)
		{
			if (this.layoutStyle == LayoutStyle.CoSE)
			{
				this.layout = new CoSELayout();
			}
			else if (this.layoutStyle == LayoutStyle.CiSE)
			{
				this.layout = new CiSELayout();
			}
			else if (this.layoutStyle == LayoutStyle.Six)
			{
				this.layout = new SixCircularLayout();
			}
		}

		return this.layout;
	}

// -----------------------------------------------------------------------------
// Section: Remaining Methods
// -----------------------------------------------------------------------------
	private void runTests()
	{
		int start = 19;
		int noOfTries = 1;
		File xmlfile;
		XMLReader reader;
		Rectangle rect;

		for (int i = start; i < start + noOfTries; i++)
		{
			// update the node and edge count
			this.noOfNodes = 10 + 50 * i;
			this.noOfEdges = (int)(noOfNodes * 2.0);

			xmlfile = new File("E:\\cise" + i + ".graphml");
			reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			this.printNodeEdgeCounts();
			rect = this.rootGraph.calculateBounds();
			System.out.print("area=" + rect.width * rect.height);

			xmlfile = new File("E:\\six" + i + ".graphml");
			reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			rect = this.rootGraph.calculateBounds();
			System.out.println(" area=" + rect.width * rect.height);
		}

//		this.layoutStyle = LayoutStyle.CoSE;
//		this.performLayoutWithRandomGraph();

//		this.resetTestValues();
//		this.testNumberOfNodesForTime();

//		this.resetTestValues();
//		this.testICESForTime();
//
//		this.resetTestValues();
//		this.testMaxClusterSizeForTime();
//
//		this.resetTestValues();
//		this.testMinMaxClusterSizeDiscrepencyForTime();
//
//		this.resetTestValues();
//		this.testPercentageOfEdgesForTime();
//
//		this.resetTestValues();
//		this.testPercentageOfEdgesForTime();
//
//		this.resetTestValues();
//		this.testMaxClusterSizeForTime();
//
//		this.resetTestValues();
//		this.testPercentageOfEdgesForTime();
//
//		this.testSimulatedAnnealingvsEdgeCrossing();
//
//		this.resetTestValues();
//		this.testNumberOfNodesForConvergence();
//
//		this.resetTestValues();
//		this.testNumberOfNodesForUsedArea();
//
//		this.resetTestValues();
//		this.testNumberOfEdgesForEdgeCrossings();
//
//		this.resetTestValues();
//		this.testNumberOfNodesForEdgeCrossingsCiSESwaps();
//
//		this.resetTestValues();
//		this.testNumberOfNodesForEdgeCrossingsCiSERotations();
//
//		this.resetTestValues();
//		this.testNumberOfEdgesForEdgeCrossingsCiSERotations();
//
//		this.resetTestValues();
//		this.testNumberOfNodesForTime();
//
//		this.resetTestValues();
//		this.testNumberOfNodesForTimeCiSEvsSix();
//
//		this.resetTestValues();
//		this.testDepthForTime();
//
//		this.resetTestValues();
//		this.testIGEsForTime();
//
//		this.resetTestValues();
//		this.testNumberOfBranchesForTime();
	}

	/**
	 * This method tests number of nodes vs. convergence. The experiments are
	 * done with randomly created compound graphs to see if layout converges or
	 * stops at maximum iteration count.
	 */
	private void testNumberOfNodesForConvergence()
	{
		int noOfTries = 30;

		System.out.println("-------------------------------------------------");
		System.out.println("Number of nodes vs convergence...");
		System.out.println("-------------------------------------------------");

		for (int i = 0; i < noOfTries; i++)
		{
			// update the node and edge count
			this.noOfNodes = 10 + 20 * i;
			this.noOfEdges =
				(int)(noOfNodes * 1.1);

			// construct new graph with parameters specified earlier
			this.createRandomGraph();
			this.printNodeEdgeCounts();
			System.out.println();
			this.performLayout();
			System.out.println();

			// actual convergence can be checked for inside doPhase3 in
			// LayoutManager
		}

		System.out.println("Finished.");
	}

	/**
	 * This method tests number of nodes vs. used area (the area of the drawing
	 * after layout). The experiments are done once with compounds and once
	 * w/out compounds so the two can be compared.
	 */
	private void testNumberOfNodesForUsedArea()
	{
		double usedArea;
		int noOfTries = 30;
		this.edgeNodeRatio = 2.0;

		System.out.println("-------------------------------------------------");
		System.out.println("Number of nodes vs used area...");
		System.out.println("-------------------------------------------------");

		for (int i = 0; i < noOfTries; i++)
		{
			// update the node and edge count
			this.noOfNodes = 10 + 50 * i;
			this.noOfEdges =
				(int)(noOfNodes * edgeNodeRatio);

			this.createRandomGraph();

			this.layoutStyle = LayoutStyle.CiSE;
			this.performLayout();
			this.printNodeEdgeCounts();

			// Calculate the area used by the drawing after layout of this graph
			Rectangle size = this.rootGraph.calculateBounds();
			usedArea = size.width* size.height;

			System.out.println("#area=" + (long)usedArea);

			this.layoutStyle = LayoutStyle.Six;
			this.performLayout();
			this.printNodeEdgeCounts();

			// Calculate the area used by the drawing after layout of this graph
			size = this.rootGraph.calculateBounds();
			usedArea = size.width* size.height;

			System.out.println("#area=" + (long)usedArea);
		}

		System.out.println("Finished.");
	}

	/**
	 * This method tests number of nodes vs. time.
	 */
	private void testNumberOfNodesForTimeCiSEvsSix()
	{
		int noOfTries = 10;

		System.out.println("-------------------------------------------------");
		System.out.println("Number of nodes vs time (CiSE vs Six...)");
		System.out.println("-------------------------------------------------");

		for (int i = 10; i <= noOfTries; i++)
		{
			// update the node and edge count
			this.noOfNodes = 10 + 50 * i;
			this.noOfEdges = (int)(noOfNodes * 1.5);

			this.createRandomGraph();
			this.printNodeEdgeCounts();
			this.writeGraph("E:\\cise-six.graphml");

//			this.layout = null;
//			File xmlfile = new File("E:\\cise-six.graphml");
//			XMLReader reader = new GraphMLReader();
//			this.rootGraph = reader.readXMLFile(xmlfile);
//			this.layoutStyle = LayoutStyle.CiSE;
//			LayoutManager lm = new LayoutManager();
//			lm.setLayout(this.getLayout());
//			lm.setRoot(this.rootGraph);
//			lm.createTopology();
//			this.getLayout().allowRotations = true;
//			this.performLayoutSameStyle(lm);
//			this.writeGraph("E:\\cise.graphml");

			this.layout = null;
			File xmlfile = new File("E:\\cise-six.graphml");
			XMLReader reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			this.layoutStyle = LayoutStyle.Six;
			LayoutManager lm = new LayoutManager();
			lm.setLayout(this.getLayout());
			lm.setRoot(this.rootGraph);
			lm.createTopology();
			this.performLayoutSameStyle(lm);
			this.writeGraph("E:\\six.graphml");

			System.out.println();
		}

		System.out.println("Finished.");
	}

	/**
	 * This method tests simulated annealing versus edge crossing count.  
	 */
	private void testSimulatedAnnealingvsEdgeCrossing()
	{
		int noOfCrossings;

		TreeMap<Double, Integer> totalCrossingCounts = new TreeMap<Double, Integer>();

		LinkedList<Double> edgeNodeRatios = new LinkedList<Double>();
		edgeNodeRatios.add(1.4);
		edgeNodeRatios.add(1.6);

		final double probabilityMin = 0.00;
		final double probabilityIncrement = 0.03;
		final double probabilityMax = 0.30;
		final double precisionThreshold = 0.0001;

		while (!edgeNodeRatios.isEmpty())
		{
			double ratio = edgeNodeRatios.poll();

			System.out.println("Edge node ratio: "+ratio);
			System.out.print("N\tE\t");

			double tempProb = probabilityMin;

			while (tempProb <= probabilityMax + precisionThreshold)
			{
				System.out.printf("Prob(%2.2f)\t#SA(%2.2f)\t#NotSA(%2.2f)\t#Crs(%2.2f)\t", tempProb, tempProb, tempProb, tempProb);

				tempProb += probabilityIncrement;
			}

			System.out.println();

//			System.out.println("Prob.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs\tProb.\t#SA\t#NotSA\t#Crs");

			for (int i = 3; i <= 6; i++)
			{
				// update the node and edge count
				this.noOfNodes = 10 + 20 * i;
				this.noOfEdges =
					(int)(noOfNodes * edgeNodeRatio);

//			System.out.printf("Running tests for #nodes=%d #edges=%d\n",
//					PerformanceAnalyzer.noOfNodes,
//					PerformanceAnalyzer.noOfEdges);

				TreeMap<Double, Integer> currentCrossingCounts = new TreeMap<Double, Integer>();

				for (int j = 0; j < 10; j++)
				{
					//Construct new graph
					this.createRandomGraph();

//				this.printNodeEdgeCounts();

					System.out.printf("%d\t%d\t",
							this.rootGraph.getNodes().size(),
							this.rootGraph.getEdges().size());


					for (double annealingProb = probabilityMin;
							annealingProb <= probabilityMax+precisionThreshold;
							annealingProb += probabilityIncrement)
					{
						//TODO: Comment out these lines when simulated annealing is re-enabled.
//						if (annealingProb == 0)
//						{
//							CoSELayout.simulatedAnnealingOn = false;
//						}
//						else
//						{
//							CoSELayout.simulatedAnnealingOn = true;
//						}
//
//						CoSELayout.annealingConstant = Math.log(annealingProb);

						this.performLayout();

						// compute and print no of crossings
						noOfCrossings = this.computeEdgeCrossings();

//					System.out.printf(" (%2.2f,%d)", annealingProb, noOfCrossings);
						System.out.printf("\t%d\t", noOfCrossings);

						Integer prevCrossing =
							currentCrossingCounts.get(annealingProb);

						if (prevCrossing == null)
						{
							prevCrossing = 0;
						}

						currentCrossingCounts.put(annealingProb, prevCrossing + noOfCrossings);

						String fileName = this.noOfNodes + "." +
							this.noOfEdges + "." +
							"#"+j + "."+
							annealingProb + ".graphml";

//					this.writeGraph(fileName);
					}

					System.out.println();
				}

				for (double annealingProb: currentCrossingCounts.keySet())
				{
					int crossingCount = currentCrossingCounts.get(annealingProb);
//				System.out.printf("For annealingProb=%2.2f #crossings=%d\n",
//					annealingProb,
//					crossingCount);

					Integer prevCrossing = totalCrossingCounts.get(annealingProb);

					if (prevCrossing == null)
					{
						prevCrossing = 0;
					}

					totalCrossingCounts.put(annealingProb, prevCrossing + crossingCount);
				}
		}

//			System.out.println();
		}


		System.out.println("\nOverall Results");

		for (double annealingProb: totalCrossingCounts.keySet())
		{
			int crossingCount = totalCrossingCounts.get(annealingProb);
			System.out.printf("For annealingProb=%2.2f #crossings=%d\n",
				annealingProb,
				crossingCount);
		}
	}

	/**
	 * This method tests number of edges vs. number of edge crossings. The
	 * experiments are done once with compounds and once w/out compounds so the
	 * two can be compared.
	 */
	private void testNumberOfEdgesForEdgeCrossings()
	{
		long noOfCrossings;
		long noOfTries = 30;

		System.out.println("------------------------------------------------");
		System.out.println("Number of edges vs number of edge crossings... ");
		System.out.println("------------------------------------------------");

		for (int i = 1; i <= noOfTries; i++)
		{
			// update the node and edge count
			this.noOfNodes = 10 + 50 * i;
			this.noOfEdges = (int)(noOfNodes * 1.4); //edgeNodeRatio);

//			this.createRandomGraph();
//			this.layout = null;
//			this.layoutStyle = LayoutStyle.CoSE;
//			LayoutManager lm = new LayoutManager();
//			lm.setLayout(this.getLayout());
//			lm.setRoot(this.rootGraph);
//			lm.createTopology();
//			this.performLayoutSameStyle(lm);
//			this.writeGraph("E:\\cise-six" + this.noOfNodes + ".graphml");

//			this.layout = null;
//			this.layoutStyle = LayoutStyle.CiSE;
//			File xmlfile = new File("E:\\cise-six" + this.noOfNodes + ".graphml");
//			XMLReader reader = new GraphMLReader();
//			this.rootGraph = reader.readXMLFile(xmlfile);
//			LayoutManager lm = new LayoutManager();
//			lm.setLayout(this.getLayout());
//			lm.setRoot(this.rootGraph);
//			lm.createTopology();
//
//			this.performLayoutSameStyle(lm);
//			noOfCrossings = this.computeEdgeCrossings();
//			this.printNodeEdgeCounts();
//			System.out.println(" #crossings=" + noOfCrossings);
//			this.writeGraph("E:\\cise" + this.noOfNodes + ".graphml");

			this.layout = null;
			this.layoutStyle = LayoutStyle.Six;
			File xmlfile = new File("E:\\cise-six" + this.noOfNodes + ".graphml");
			XMLReader reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			LayoutManager lm = new LayoutManager();
			lm.setLayout(this.getLayout());
			lm.setRoot(this.rootGraph);
			lm.createTopology();

			this.performLayoutSameStyle(lm);
			noOfCrossings = this.computeEdgeCrossings();
			this.printNodeEdgeCounts();
			System.out.println(" #crossings=" + noOfCrossings);
			this.writeGraph("E:\\six" + this.noOfNodes + ".graphml");

			// TODO: Uncomment following block after remove compounds method is
			// revised
//			this.removeCompoundsFromGraph();
//			this.layout();
//
//			// compute and print no of crossings
//			noOfCrossings = this.computeEdgeCrossings();
//			System.out.println(" " + noOfCrossings);
		}

		System.out.println("Finished.");
	}

	private void testNumberOfNodesForEdgeCrossingsCiSESwaps()
	{
		long noOfCrossings;
		long noOfTries = 22;

		System.out.println("------------------------------------------------");
		System.out.println("Swaps vs no swaps............................... ");
		System.out.println("------------------------------------------------");

		for (int i = 16; i <= noOfTries; i++)
		{
			// update the node and edge count
			this.noOfNodes = 10 + 50 * i;
			this.noOfEdges = (int)(noOfNodes * 1.4); //edgeNodeRatio);

//			this.createRandomGraph();
//			this.layout = null;
//			this.layoutStyle = LayoutStyle.CoSE;
//			LayoutManager lm = new LayoutManager();
//			lm.setLayout(this.getLayout());
//			lm.setRoot(this.rootGraph);
//			lm.createTopology();
//			this.performLayoutSameStyle(lm);
//			this.writeGraph("E:\\cise-six" + this.noOfNodes + ".graphml");

			this.layout = null;
			this.layoutStyle = LayoutStyle.CiSE;
			File xmlfile = new File("E:\\cise-six" + this.noOfNodes + ".graphml");
			XMLReader reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			LayoutManager lm = new LayoutManager();
			lm.setLayout(this.getLayout());
			lm.setRoot(this.rootGraph);
			lm.createTopology();

			this.performLayoutSameStyle(lm);
			noOfCrossings = this.computeEdgeCrossings();
			this.printNodeEdgeCounts();
			System.out.println(" #crossings=" + noOfCrossings);
			this.writeGraph("E:\\cise-no-swap" + this.noOfNodes + ".graphml");
		}

		System.out.println("Finished.");
	}

	/**
	 * This method tests number of nodes vs. number of edge crossings in CoSE
	 * layout with and without rotations allowed.
	 */
	private void testNumberOfNodesForEdgeCrossingsCiSERotations()
	{
		long noOfCrossings;
		long noOfTries = 30;

		System.out.println("------------------------------------------------");
		System.out.println("Number of nodes vs number of edge crossings...  ");
		System.out.println("------------------------------------------------");

		for (int i = 18; i <= noOfTries; i++)
		{
			// update the node and edge count
			this.noOfNodes = 10 + 50 * i;
			this.noOfEdges = (int)(noOfNodes * 1.2); //edgeNodeRatio);

//			this.createRandomGraph();
//			this.layout = null;
//			this.layoutStyle = LayoutStyle.CoSE;
//			LayoutManager lm = new LayoutManager();
//			lm.setLayout(this.getLayout());
//			lm.setRoot(this.rootGraph);
//			lm.createTopology();
//			this.performLayoutSameStyle(lm);
//			this.writeGraph("E:\\cise_with_rotations_node_" + this.noOfNodes + ".graphml");

			File xmlfile = new File("E:\\cise_with_rotations_node_" + this.noOfNodes + ".graphml");
			XMLReader reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			this.printNodeEdgeCounts();
			this.layout = null;
			this.layoutStyle = LayoutStyle.CiSE;
			layout = this.getLayout();
			LayoutManager lm = new LayoutManager();
			lm.setLayout(this.getLayout());
			lm.setRoot(this.rootGraph);
			lm.createTopology();
//			layout.allowRotations = true;
			this.performLayoutSameStyle(lm);
			noOfCrossings = this.computeEdgeCrossings();
			System.out.print(" w/ rotations=" + noOfCrossings);

			xmlfile = new File("E:\\cise_with_rotations_node_" + this.noOfNodes + ".graphml");
			reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			this.layout = null;
			this.layoutStyle = LayoutStyle.CiSE;
			layout = this.getLayout();
			lm = new LayoutManager();
			lm.setLayout(this.getLayout());
			lm.setRoot(this.rootGraph);
			lm.createTopology();
//			layout.allowRotations = false;
			this.performLayoutSameStyle(lm);
			noOfCrossings = this.computeEdgeCrossings();
			System.out.println(" w/out rotations=" + noOfCrossings);
		}

		System.out.println("Finished.");
	}

	/**
	 * This method tests number of edges vs. number of edge crossings in CoSE
	 * layout with and without rotations allowed.
	 */
	private void testNumberOfEdgesForEdgeCrossingsCiSERotations()
	{
		long noOfCrossings;
		long noOfTries = 20;

		System.out.println("------------------------------------------------");
		System.out.println("Number of edges vs number of edge crossings... ");
		System.out.println("------------------------------------------------");

		for (int i = 1; i <= noOfTries; i++)
		{
			this.layout = null;
			this.layoutStyle = LayoutStyle.CiSE;
			Layout layout = this.getLayout();
			LayoutManager lm = new LayoutManager();
			lm.setLayout(this.getLayout());

			File xmlfile = new File("E:\\cise_rotation_test_base.graphml");
			XMLReader reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);

//			this.noOfNodes = 10 + 50 * i;
//			this.noOfEdges = (int)(noOfNodes * 1.1);
			this.igeEdgeRatio += 0.02;
			this.createRandomIGEs();
			this.writeGraph("E:\\cise_rotation_test.graphml");

			xmlfile = new File("E:\\cise_rotation_test.graphml");
			reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			this.printNodeEdgeCounts();
			lm.setRoot(this.rootGraph);
			lm.createTopology();
//			layout.allowRotations = true;
			((CiSELayout)layout).idealEdgeLength *= 1.3;
			this.performLayoutSameStyle(lm);
			noOfCrossings = this.computeEdgeCrossings();
			System.out.print(" w/ rotations=" + noOfCrossings);
			this.writeGraph("E:\\cise_with_rotations_" + (i-1) + ".graphml");

			xmlfile = new File("E:\\cise_rotation_test.graphml");
			reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);

			this.layout = null;
			this.layoutStyle = LayoutStyle.CiSE;
			layout = this.getLayout();
			lm = new LayoutManager();
			lm.setLayout(this.getLayout());

			lm.setRoot(this.rootGraph);
			lm.createTopology();
//			layout.allowRotations = false;
			this.performLayoutSameStyle(lm);
			noOfCrossings = this.computeEdgeCrossings();
			System.out.println(" w/out rotations=" + noOfCrossings);
			this.writeGraph("E:\\cise_without_rotations_" + (i-1) + ".graphml");
		}

		System.out.println("Finished.");
	}

	private void createRandomIGEs()
	{
		int noOfNodes = this.rootGraph.getNodes().size();
		int noOfIGEs = (int) (noOfNodes * this.igeEdgeRatio);
		int noOfClusters = 0;
		int maxClusterID = -1;
        boolean clusters [] = new boolean[noOfNodes];
		EClusterManager clusterMgr = this.rootGraph.getClusterManager();
		Random random = new Random(System.currentTimeMillis());

		for (int i = 0; i < noOfNodes; i++)
		{
			clusters[i] = false;
		}

		for (Object obj : this.rootGraph.getNodes())
		{
			NodeModel node = (NodeModel) obj;
			Cluster cluster = node.getClusters().get(0);

			if (!clusters[cluster.getClusterID()])
			{
				noOfClusters++;

				if (cluster.getClusterID() > maxClusterID)
				{
					maxClusterID = cluster.getClusterID();
				}
			}

			clusters[cluster.getClusterID()] = true;
		}

		if (noOfClusters < 2)
		{
			return;
		}

		int igeCount = 0;
		int srcClusterID;
		int trgClusterID;
		Cluster srcCluster;
		Cluster trgCluster;
		Object [] srcNodesArray;
		Object [] trgNodesArray;
		NodeModel srcNode;
		NodeModel trgNode;

		EdgeModel edge;
		CreateConnectionCommand command;

		while (igeCount < noOfIGEs)
		{
			do {
				srcClusterID = random.nextInt(maxClusterID);
				srcCluster = clusterMgr.getClusterByID(srcClusterID);
			} while (srcCluster == null);

			do {
				trgClusterID = random.nextInt(maxClusterID);
				trgCluster = clusterMgr.getClusterByID(trgClusterID);
			} while (trgClusterID == srcClusterID || trgCluster == null);

			srcNodesArray = srcCluster.getNodes().toArray();
			trgNodesArray = trgCluster.getNodes().toArray();

			srcNode = (NodeModel)(srcNodesArray[random.nextInt(srcNodesArray.length)]);

			do {
				trgNode = (NodeModel)(trgNodesArray[random.nextInt(trgNodesArray.length)]);
			} while (trgNode == srcNode);

			edge = new EdgeModel();
			command = new CreateConnectionCommand();
			command.setSource(srcNode);
			command.setTarget(trgNode);
			command.setConnection(edge);
			command.execute();

			igeCount++;
		}
	}

	/**
	 * This method tests number of nodes vs. number of node-to-node overlaps.
	 * The experiments are done once with compounds and once w/out compounds so
	 * the two can be compared.
	 */
	private void testNumberOfNodesForOverlap()
	{
		int noOfCrossings;
		int start = 19;
		int noOfTries = 1;

		System.out.println("-------------------------------------------------");
		System.out.println("Number of edges vs edge crossings...");
		System.out.println("-------------------------------------------------");

		File xmlfile;
		XMLReader reader;
		LayoutManager lm;

		for (int i = start; i < start + noOfTries; i++)
		{
			// update the node and edge count
			this.noOfNodes = 10 + 50 * i;
			this.noOfEdges = (int)(noOfNodes * 2.0);

//			xmlfile = new File("E:\\cise-six" + i + ".graphml");
//			reader = new GraphMLReader();
//			this.rootGraph = reader.readXMLFile(xmlfile);

			xmlfile = new File("E:\\cise" + i + ".graphml");
			reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			this.printNodeEdgeCounts();
			noOfCrossings = this.computeEdgeCrossings();
			System.out.print(" #crossings= " + noOfCrossings);

			xmlfile = new File("E:\\six" + i + ".graphml");
			reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			noOfCrossings = this.computeEdgeCrossings();
			System.out.println(" #crossings= " + noOfCrossings);

//			this.createRandomGraph();
//			this.writeGraph("E:\\cise-six" + i + ".graphml");

//			xmlfile = new File("E:\\cise-six" + i + ".graphml");
//			reader = new GraphMLReader();
//			this.rootGraph = reader.readXMLFile(xmlfile);
//			this.layoutStyle = LayoutStyle.CiSE;
//			lm = new LayoutManager();
//			lm.setLayout(this.getLayout());
//			lm.setRoot(this.rootGraph);
//			lm.createTopology();
//			this.performLayoutSameStyle(lm);
//			this.writeGraph("E:\\cise.graphml");
//
//			// compute no of overlaps
//			this.printNodeEdgeCounts();
//			noOfCrossings = this.computeNoOfOverlaps();
//			System.out.println(" #overlaps= " + noOfCrossings);

//			xmlfile = new File("E:\\cise-six" + i + ".graphml");
//			reader = new GraphMLReader();
//			this.rootGraph = reader.readXMLFile(xmlfile);
//			this.layoutStyle = LayoutStyle.Six;
//			lm = new LayoutManager();
//			lm.setLayout(this.getLayout());
//			lm.setRoot(this.rootGraph);
//			lm.createTopology();
//			this.performLayoutSameStyle(lm);
//			this.writeGraph("E:\\six.graphml");
//
//			// compute no of overlaps
//			noOfCrossings = this.computeNoOfOverlaps();
//			System.out.println(" #overlaps= " + noOfCrossings);
		}

		System.out.println("Finished.");
	}

	
	/**
	 * This private method provides a generic access point for automated testing of 
	 * several variables according to time.  
	 *  
	 * @param fieldName
	 * @param start
	 * @param increment
	 * @param count
	 */
	private void vsTime(String fieldName, double start, double increment, int count)
	{
		long averageRunTime;

		for (int i = 0; i < count; i++)
		{
			double value = start + increment * i;

			Field field;

			try
			{
				field = PerformanceAnalyzer.class.getField(fieldName);

				if (field.getType().isAssignableFrom(int.class))
				{
					field.set(null, (int) value);
				}
				else
				{
					field.set(null, value);
				}

			}
			catch (Exception e)
			{
				//e.printStackTrace();
			}

			this.noOfEdges =
				(int)(noOfNodes * edgeNodeRatio);

			averageRunTime = this.performLayoutWithRandomGraph();

			System.out.printf("%s=%.2f", fieldName, value);
			System.out.print("\t#node=" + this.noOfNodes);
			System.out.print("\t#edge=" + this.noOfEdges);
			System.out.println("\t#time=" + averageRunTime);
		}
	}

	/**
	 * This method tests the running time according to number of nodes.
	 */
	private void testNumberOfNodesForTime()
	{
		System.out.println("Testing number of nodes vs time");
		edgeNodeRatio = 1.5;
		this.vsTime("noOfNodes", 10, 50, 50);
	}


	/**
	 * This method tests the running time according to number of edges.
	 */
	private void testPercentageOfEdgesForTime()
	{
		System.out.println("Testing edge/node raito vs time");
		noOfNodes = 100;
		this.vsTime("edgeNodeRatio", 1.1, 0.1, 9);
	}

	
	/**
	 * This method tests the running time according to ratio of inter
	 * cluster edges.
	 */
	private void testICESForTime()
	{
		System.out.println("Testing ice percentage vs time");
		noOfNodes = 500;
		this.vsTime("igeEdgeRatio", 0, 0.025, 10);
	}


	/**
	 * This method tests the running time according to maximum cluster size.
	 */
	private void testMaxClusterSizeForTime()
	{
		System.out.println("Testing max clusters vs time");
		noOfNodes = 500;
		this.vsTime("maxClusterSize", 5, 5, 10);
	}

	
	/**
	 * This method tests the running time according to the difference
	 * between maximum and minimum cluster sizes.
	 */
	private void testMinMaxClusterSizeDiscrepencyForTime()
	{
		System.out.println("Testing min max cluster sizes discrepency vs time");
		noOfNodes = 500;
		maxClusterSize = 15;
		this.vsTime("clusterSizeDiscrepancy", 1, 1, 12);
	}

	private void testDepthForTime()
	{
		long averageRunTime;

		this.probabilityOfBranchPruning = 0;
		this.noOfNodes = 500;


		System.out.println("Depth vs time");
		for (int i = 0; i < 10; i++)
		{
			//Update the node and edge count
			this.depth = i;

			System.out.println("Testing maximumDepth vs time" +
				" for maximumDepth: " + depth);
			averageRunTime = this.performLayoutWithRandomGraph();
			System.out.println("Depth = " + depth + " time: " + averageRunTime);
		}
		System.out.println("Finished.");

	}

	private void testIGEsForTime()
	{
		long averageRunTime;
		//Update the node and edge count
		this.noOfNodes = 500;
		this.edgeNodeRatio = 1.5;

		System.out.println("Percentage of IGEs vs time");
		for (int i = 0; i < 40; i++)
		{
			//Update the node and edge count
			this.igeEdgeRatio = (i + 1);

			System.out.println("Testing Percentage of IGEs  vs time" +
				" for PERCENTAGE_OF_IGES: " + igeEdgeRatio);
			averageRunTime = this.performLayoutWithRandomGraph();
			System.out.println("PERCENTAGE_OF_IGES = " + igeEdgeRatio +
				" time: " +
				averageRunTime);
		}
		System.out.println("Finished.");

	}

	private void testNumberOfBranchesForTime()
	{
		long averageRunTime;
		this.noOfNodes = 500;
		this.edgeNodeRatio = 1.5;

		System.out.println("Number of branches vs time");
		for (int i = 0; i < 50; i++)
		{
			//Update the node and edge count
			this.noOfBranches = i;

			System.out.println("Testing Number of branches vs time" +
				" for noOfBranches: " + noOfBranches);
			averageRunTime = this.performLayoutWithRandomGraph();
			System.out.println("noOfBranches = " + noOfBranches +
				" time: " +
				averageRunTime);
		}
		System.out.println("Finished.");

	}

	/*
	 * This method creates a random graph with current parameters and performs
	 * layout on this random graph.
	 */
	private long performLayoutWithRandomGraph()
	{
		// Construct new random graph
		this.createRandomGraph();

		// Layout
		return this.performLayout();
	}

	/*
	 * This method performs layout on the current graph using current style.
	 */
	private long performLayout()
	{
		assert this.rootGraph != null;

		LayoutManager lm = new LayoutManager();

		// Try this graph 10 times and average results
		long averageRunTime = 0;
		long startTime;
		long finishTime;

		for (int j = 0; j < 1; j++)
		{
			lm.setLayout(this.getLayout());
			lm.setRoot(this.rootGraph);
			lm.createTopology();

			startTime = System.currentTimeMillis();
			lm.runLayout();
			finishTime = System.currentTimeMillis();
			averageRunTime += finishTime - startTime;
		}

//		System.out.println("Average run time: " + averageRunTime / 10 + " ms.");
//		System.out.print(averageRunTime / 10 + " ");
		return averageRunTime / 10;
	}

	private long performLayoutSameStyle(LayoutManager lm)
	{
		assert this.rootGraph != null;

		// Try this graph 10 times and average results
		long averageRunTime = 0;
		long startTime;
		long finishTime;
		int noOfRuns = 1;

		for (int j = 0; j < noOfRuns; j++)
		{
			startTime = System.currentTimeMillis();
			lm.runLayout();
			finishTime = System.currentTimeMillis();
			averageRunTime += finishTime - startTime;
		}

//		System.out.println("Average run time: " + averageRunTime / 10 + " ms.");
//		System.out.print(averageRunTime / noOfRuns + " ");
		return averageRunTime / 10;
	}

	/*
	 * This method performs layout on graphs read from files using current style.
	 */
	private synchronized void performLayoutFromFile()
	{
		String fullPath = "D:\\workspace\\research_work\\chied2x\\graphs\\rome\\";
		String fileName = "";
		File xmlfile = null;
		XMLReader reader;

		/*for (int i = 0; i < 30; i++)
		{
			// trees
			if ((i >= 0) && (i <= 7))
			{
				fileName = "tree_"+i+".xml";
			}

			// mashes
			if ((i >= 8) && (i <= 15))
			{
				fileName = "mash_"+(i-8)+".xml";
			}

			// compounds
			if ((i >= 16) && (i <= 29))
			{
				fileName = "compound_"+(i-16)+".xml";
			}

			xmlfile = new File(fullPath + fileName);
			reader = new GraphMLReader();
			root = reader.readXMLFile(xmlfile);
			coseL = new CoSELayout();

			lm.setLayout(coseL);
			lm.setRoot(root);
			lm.createTopology();

			long startTime = 0, endTime = 0, currExcTime = 0, averageExcTime = 0;
			// calculate layout five times and take the average execution time
			for (int j = 0; j < 5; j++)
			{
				// calculate the execution time
				startTime = System.currentTimeMillis();
				lm.runLayout();
				endTime = System.currentTimeMillis();
				currExcTime = endTime - startTime;

				averageExcTime = averageExcTime + currExcTime;
			}

			averageExcTime = averageExcTime/5;

			System.out.println("Average execution time of " + fileName + " is: " +
				averageExcTime + " ms.");
		}
		*/
		for (int i = 1; i <= 7; i++)
		{
			fileName = "mesh_auto_"+ i +"00.xml";

			System.out.println("file name: " + fileName);
			xmlfile = new File(fullPath + fileName);
			reader = new GraphMLReader();
			this.rootGraph = reader.readXMLFile(xmlfile);
			System.out.print("Average execution time of " + fileName + " is: ");
			this.performLayout();
		}
	}

	/**
	 * This method writes current graph to given xml file for debugging purposes.
	 */
	private void writeGraph(String fileName)
	{
		try
		{
			BufferedWriter xmlFile = new BufferedWriter(new FileWriter(fileName));
			XMLWriter writer = new GraphMLWriter();
			xmlFile.write(writer.writeXMLFile(this.rootGraph).toString());
			xmlFile.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used internally to create a diagram using a graph
	 * manager.
	 */
	private void createRandomGraph()
	{
		RandomGraphCreator randomGraphCreator = new RandomGraphCreator(
			noOfNodes,
			noOfEdges,
			depth,
			igeEdgeRatio,
			noOfBranches,
			probabilityOfBranchPruning,
			maxClusterSize,
			maxClusterSize - clusterSizeDiscrepancy,
			false,
			0,
			0,
			maxNodeSize,
			minNodeSize,
			removeDisconnectedNodes,
			flatGraphType,
			minNumberOfChildren,
			maxNumberOfChildren,
			pruningProbability);

		this.rootGraph = randomGraphCreator.run();

		if (this.main != null)
		{
			this.rootGraph.setAsRoot();

			PerformanceAnalyzer.this.main.getViewer().setContents(
				PerformanceAnalyzer.this.rootGraph);
		}
	}

	/**
	 * This method prints no of nodes and edges in the current graph manager.
	 */
	private void printNodeEdgeCounts()
	{
		Set nodes = this.rootGraph.getNodes();
		Set edges = this.rootGraph.getEdges();
		System.out.print("#nodes=" + nodes.size() +
			" #edges=" + edges.size() + " ");
//		System.out.print(nodes.size() + " ");
	}

// -----------------------------------------------------------------------------
// Section: Computations on current layout
// -----------------------------------------------------------------------------

	/**
	 * This method computes the number of edge crossings of the current graph
	 * manager.
	 */
	public int computeEdgeCrossings()
	{
		return this.computeEdgeCrossings(true);
	}

	/**
	 * This method computes the number of edge crossings of the current graph
	 * manager.
	 */
	public int computeEdgeCrossings(boolean onlyInterClusterEdges)
	{
		int noOfCrossings = 0;
		int noOfNonCrossings = 0;
		Set<EdgeModel> allEdges1 = this.rootGraph.getEdges();
		Set<EdgeModel> allEdges2 = this.rootGraph.getEdges();

		for (EdgeModel edge1: allEdges1)
		{
			NodeModel src1 = edge1.getSource();
			NodeModel trg1 = edge1.getTarget();

			for (EdgeModel edge2: allEdges2)
			{
				NodeModel src2 = edge2.getSource();
				NodeModel trg2 = edge2.getTarget();

				if (edge1 == edge2)
				{
					continue;
				}

				// if target and source nodes don't have common cluster
				if (!onlyInterClusterEdges ||
					(!src1.getClusters().isEmpty() || !trg1.getClusters().isEmpty()) &&
					!src1.hasCommonCluster(trg1) &&
					(!src2.getClusters().isEmpty() || !trg2.getClusters().isEmpty()) &&  
					!src2.hasCommonCluster(trg2))
				{
					// If one of source or targets of the edges are same,
					// then they shouldn't intersect.
					if (src1 == src2 || src1 == trg2 || trg1 == src2 || trg1 == trg2)
					{
						noOfNonCrossings++;
					}
					else if (Line2D.linesIntersect(
						src1.getCenterX(),
						src1.getCenterY(),
						trg1.getCenterX(),
						trg1.getCenterY(),
						src2.getCenterX(),
						src2.getCenterY(),
						trg2.getCenterX(),
						trg2.getCenterY()))
					{
						noOfCrossings++;
					}
					else
					{
						noOfNonCrossings++;
					}
				}
			}
		}

		// divide by 2 for undoing double counting
		return noOfCrossings / 2;
	}

	/**
	 * This method computes the variance of the edge lengths of the current
	 * graph manager to measure edge length uniformity.
	 */
	public double computeEdgeLengthVariance()
	{
		Set<EdgeModel> allEdges = this.rootGraph.getEdges();
		double noOfEdges = allEdges.size();
		double mean = 0.0;
		double sumOfSquares = 0.0;
		double variance;

		for (EdgeModel edge: allEdges)
		{
			NodeModel src = edge.getSource();
			NodeModel trg = edge.getTarget();

			int srcCenterX = src.getCenterX();
			int srcCenterY = src.getCenterY();
			int trgCenterX = trg.getCenterX();
			int trgCenterY = trg.getCenterY();

			double length = Math.sqrt((srcCenterX - trgCenterX) * (srcCenterX - trgCenterX) +
				(srcCenterY - trgCenterY) * (srcCenterY - trgCenterY));

			mean += length;
		}

		mean /= noOfEdges;

		for (EdgeModel edge: allEdges)
		{
			NodeModel src = edge.getSource();
			NodeModel trg = edge.getTarget();

			int srcCenterX = src.getCenterX();
			int srcCenterY = src.getCenterY();
			int trgCenterX = trg.getCenterX();
			int trgCenterY = trg.getCenterY();

			double length = Math.sqrt((srcCenterX - trgCenterX) * (srcCenterX - trgCenterX) +
				(srcCenterY - trgCenterY) * (srcCenterY - trgCenterY));

			sumOfSquares += (mean - length) * (mean - length);
		}

		variance = sumOfSquares / noOfEdges;

		return variance;
	}

	/**
	 * This method computes number of overlaps for the current graph manager.
	 */
	private int computeNoOfOverlaps()
	{
		int noOfOverlaps = 0;


		Set<NodeModel> nodes1 = this.rootGraph.getNodes();
		Set<NodeModel> nodes2 = this.rootGraph.getNodes();

		for (NodeModel node1 : nodes1)
		{
			for (NodeModel node2 : nodes2)
			{
				Rectangle rect1 = node1.getConstraint();
				Rectangle rect2 = node2.getConstraint();

				if (node1 != node2 &&
					!rect1.contains(rect2) && !rect2.contains(rect1) &&
					rect1.intersects(rect2))
				{
					noOfOverlaps++;
				}
			}
		}

		// divide by 2 for undoing double counting
		return noOfOverlaps / 2;
	}

// -----------------------------------------------------------------------------
// Section: Main method
// -----------------------------------------------------------------------------
	public synchronized static void main(String[] args)
	{
		new PerformanceAnalyzer().runTests();
	}

// -----------------------------------------------------------------------------
// Section: Instance variables
// -----------------------------------------------------------------------------
	public enum LayoutStyle {CoSE, CiSE, Six}

	/*
	 * Root graph containing the drawing and a handle to it
	 */
	private CompoundModel rootGraph = null;
	private ChisioMain main = null;

	/*
	 * Layout style
	 */
	public LayoutStyle layoutStyle;

	/*
	 * Layout object
	 */
	public Layout layout = null;

	/*
	 * Parameters for creating random graphs
	 */
	public static int noOfNodes;
	public static int noOfEdges;
	public static double edgeNodeRatio;
	public static int depth;
	public static double igeEdgeRatio;
	public static double probabilityOfBranchPruning;
	public static int noOfBranches;

	public static Dimension minNodeSize;
	public static Dimension maxNodeSize;
	
	public static boolean generateMesh;
	public static RandomGraphCreator.FlatGraphType flatGraphType;
	
	public static int maxClusterSize;
	public static int clusterSizeDiscrepancy;
	public static boolean removeDisconnectedNodes;

	public static int minNumberOfChildren;
	public static int maxNumberOfChildren;
	public static double pruningProbability;
}