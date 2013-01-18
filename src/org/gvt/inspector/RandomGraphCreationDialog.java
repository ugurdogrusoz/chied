package org.gvt.inspector;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.gvt.ChisioMain;
import org.gvt.util.RandomGraphCreator;
import org.gvt.util.RandomGraphOptionsPack;

/**
 * This class maintains the Random Graph Creation dialog to set the parameters
 * random graphs
 *
 * @author Esat Belviranli
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class RandomGraphCreationDialog extends Dialog
{
	// UI components that are needed to be accessed by different methods

	// Common parameters
	private Text numberOfNodes;
	private Text numberOfEdges;
	private Text minNodeSizeWidth;
	private Text maxNodeSizeWidth;
	private Text minNodeSizeHeight;
	private Text maxNodeSizeHeight;
	private Button removeDisconnectedNodes;

	// Tree related
	private Scale pruningProbability;
	private Label pruningProbabilityLabel;
	private Text minBranching;
	private Text maxBranching;
	
	// Compound graph related
	private Scale iGEdgeRatio;
	private Label iGEdgeRatioLabel;
	private Text noOfSiblings;
	private Scale branchFactor;
	private Label branchFactorLabel;
	private Text compoundDepth;

	// Cluster graph related
	private Scale iCEdgeRatio;
	private Label iCEdgeRatioLabel;
	private Text maxClusterSize;
	private Button allowOverlaps;
	private Text clusterOverlapFrequency;
	private Text clusterOverlapAmount;

	private Button flatGraph;
	private Button compoundGraph;
	private Button clusterGraph;

	// Flat graph types
	private Button treeGraph;
	private Button meshGraph;
	private Button generalGraph;
	
	// Panes that contain specific options
	private Composite clusterGraphPane;
	private Composite allowOverlapsGraphPane;
	private Composite flatGraphPane;
	private Composite treeGraphPane;
	private Composite compoundGraphPane;

	// This variable holds the random graph created by this dialog.
	protected Object result;

	protected Shell shell;

	private ChisioMain main;

	public static int lastGraphType = 0;

	private KeyAdapter keyAdapter = new KeyAdapter()
	{
		public void keyPressed(KeyEvent arg0)
		{
			arg0.doit = this.isDigit(arg0.keyCode);
		}

		private boolean isDigit(int keyCode)
		{
			if (Character.isDigit(keyCode)
				|| keyCode == SWT.DEL
				|| keyCode == 8
				|| keyCode == SWT.ARROW_LEFT
				|| keyCode == SWT.ARROW_RIGHT)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	};

	public static void main(String args[])
	{
		RandomGraphCreationDialog dialog = new RandomGraphCreationDialog(null);
		dialog.open();
	}

	/**
	 * Create the dialog
	 */
	public RandomGraphCreationDialog(ChisioMain main)
	{
		super(new Shell(),SWT.NONE);

		this.main = main;
	}

	/**
	 * Open the dialog
	 */
	public Object open()
	{
		this.createContents();
		this.setInitialValues();
		this.shell.open();
		this.shell.layout();
		Display display = this.getParent().getDisplay();
		while (!this.shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		return this.result;
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents()
	{
		int shellWidth = 330;
		int shellHeight = 470;

		this.shell =
			new Shell(this.getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		this.shell.setSize(shellWidth, shellHeight);
		this.shell.setText("Create Random Graph");

		Rectangle rect = this.shell.getClientArea();
		shellWidth = rect.width;
		shellHeight = rect.height;

		int marginX = 10;
		int marginY = 10;
		int labelPaddingY = 3;

		int labelWidth = 140;
		int smallLabelWidth = 20; // Used for the "x" label
		int scaleLabelWidth = 30;
		int labelHeight = 22;
		int textFieldWidth = 30;
		int textFieldHeight = labelHeight;
		int radioButtonWidth = 85;
		int buttonWidth = 50;
		int buttonHeight = 25;
		int scaleWidth = 90;
		int scaleHeight = 31;

		int paddingX = 20;
		int xIncrement = shellWidth/2;

		int currentY = 10;
		int yIncrement = 27;

		int currentX = 0;

		int commonOptionsGroupWidth = shellWidth - 2*marginX;
		int commonOptionsGroupHeight = 5*yIncrement + 3*marginY;

		int specificOptionsGroupWidth = commonOptionsGroupWidth ;
		int specificOptionsGroupHeight = 6*yIncrement + 4*marginY + 13;

		int specificOptionPaneWidth = specificOptionsGroupWidth - 3* marginX;
		int specificOptionPaneHeight = specificOptionsGroupHeight - 3* marginY;

		int buttonSeparation = 5;

		Composite mainContent = new Composite(this.shell, SWT.NONE);
		mainContent.setLocation(0, 0);
		mainContent.setSize(shellWidth, shellHeight);

		Group commonOptionsGroup = new Group(mainContent, SWT.NONE);
		commonOptionsGroup.setText("General");
		commonOptionsGroup.setBounds(marginX,
			marginY,
			commonOptionsGroupWidth,
			commonOptionsGroupHeight);

		currentX = paddingX;
		currentY += marginY;

		final Label noOfNodesLabel = new Label(commonOptionsGroup, SWT.NONE);
		noOfNodesLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		noOfNodesLabel.setText("Number of nodes");

		currentX += xIncrement;

		this.numberOfNodes = new Text(commonOptionsGroup, SWT.BORDER);
		this.numberOfNodes.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
		currentX = paddingX;
		currentY += yIncrement;

		final Label noOfEdgesLabel = new Label(commonOptionsGroup, SWT.NONE);
		noOfEdgesLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		noOfEdgesLabel.setText("Number of edges");

		currentX += xIncrement;

		this.numberOfEdges = new Text(commonOptionsGroup, SWT.BORDER);
		this.numberOfEdges.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);

		currentX = paddingX;
		currentY += yIncrement;

		final Label minNodeSizeLabel = new Label(commonOptionsGroup, SWT.NONE);
		minNodeSizeLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		minNodeSizeLabel.setText("Minimum node size");

		currentX += xIncrement;

		this.minNodeSizeWidth = new Text(commonOptionsGroup, SWT.BORDER);
		this.minNodeSizeWidth.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);

		currentX += textFieldWidth;

		final Label sizeCrossLabel = new Label(commonOptionsGroup, SWT.NONE);
		sizeCrossLabel.setAlignment(SWT.CENTER);
		sizeCrossLabel.setBounds(currentX, currentY+labelPaddingY, smallLabelWidth, labelHeight);
		sizeCrossLabel.setText("X");

		currentX += smallLabelWidth;

		this.minNodeSizeHeight = new Text(commonOptionsGroup, SWT.BORDER);
		this.minNodeSizeHeight.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);

		currentX = paddingX;
		currentY += yIncrement;

		final Label maxNodeSizeLabel = new Label(commonOptionsGroup, SWT.NONE);
		maxNodeSizeLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		maxNodeSizeLabel.setText("Maximum node size");

		currentX += xIncrement;

		this.maxNodeSizeWidth = new Text(commonOptionsGroup, SWT.BORDER);
		this.maxNodeSizeWidth.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);

		currentX += textFieldWidth;

		final Label sizeCrossLabel2 = new Label(commonOptionsGroup, SWT.NONE);
		sizeCrossLabel2.setAlignment(SWT.CENTER);
		sizeCrossLabel2.setBounds(currentX, currentY+labelPaddingY, smallLabelWidth, labelHeight);
		sizeCrossLabel2.setText("X");

		currentX += smallLabelWidth;

		this.maxNodeSizeHeight = new Text(commonOptionsGroup, SWT.BORDER);
		this.maxNodeSizeHeight.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);

		currentY += yIncrement;
		currentX = paddingX;

		final Label removeDisconnectedNodesLabel =
			new Label(commonOptionsGroup, SWT.NONE);
		removeDisconnectedNodesLabel.setBounds(
			currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		removeDisconnectedNodesLabel.setText("Remove disconnected nodes");

		currentX += xIncrement;

		this.removeDisconnectedNodes = new Button(commonOptionsGroup, SWT.CHECK);
		this.removeDisconnectedNodes.setBounds(
			currentX, currentY, textFieldWidth, textFieldHeight);

		currentX = marginX;
		currentY += 2*yIncrement;

		final Group specificOptionsGroup = new Group(mainContent, SWT.NONE);
		specificOptionsGroup.setText("Type Specific");
		specificOptionsGroup.setBounds(marginX,
			currentY,
			specificOptionsGroupWidth,
			specificOptionsGroupHeight);

		SelectionAdapter graphTypeSelectionAdapter = new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent selectionEvent)
			{
				RandomGraphCreationDialog.this.graphTypeSelectionChanged(selectionEvent);
			}
		};

		currentX = paddingX;
		currentY = yIncrement;

		this.flatGraph = new Button(specificOptionsGroup, SWT.RADIO);
		this.flatGraph.setBounds(currentX, currentY, radioButtonWidth, labelHeight);
		this.flatGraph.setText("Flat");
		this.flatGraph.addSelectionListener(graphTypeSelectionAdapter);
		this.flatGraph.setSelection(true);

		currentX += radioButtonWidth;

		this.clusterGraph = new Button(specificOptionsGroup, SWT.RADIO);
		this.clusterGraph.setBounds(currentX, currentY, radioButtonWidth, labelHeight);
		this.clusterGraph.setText("Clustered");
		this.clusterGraph.addSelectionListener(graphTypeSelectionAdapter);

		currentX += radioButtonWidth;

		this.compoundGraph = new Button(specificOptionsGroup, SWT.RADIO);
		this.compoundGraph.setBounds(currentX, currentY, radioButtonWidth, labelHeight);
		this.compoundGraph.setText("Compound");
		this.compoundGraph.addSelectionListener(graphTypeSelectionAdapter);

		currentX = paddingX;

		this.flatGraphPane = new Composite(specificOptionsGroup, SWT.NONE);
		this.flatGraphPane.setBounds(currentX,
			currentY,
			specificOptionPaneWidth,
			specificOptionPaneHeight);
		this.flatGraphPane.setVisible(false);
		
		final Group flatGraphGroup = new Group(this.flatGraphPane, SWT.NONE);
		flatGraphGroup.setText("Select Flat Type");
		flatGraphGroup.setBounds(0,
			currentY,
			specificOptionsGroupWidth-29,
			specificOptionsGroupHeight-65);
		flatGraphGroup.setVisible(true);
		
		this.compoundGraphPane = new Composite(specificOptionsGroup, SWT.NONE);
		this.compoundGraphPane.setBounds(currentX ,
			currentY,
			specificOptionPaneWidth,
			specificOptionPaneHeight);
		this.compoundGraphPane.setVisible(false);

		this.clusterGraphPane = new Composite(specificOptionsGroup, SWT.NONE);
		this.clusterGraphPane.setBounds(currentX,
			currentY,
			specificOptionPaneWidth,
			specificOptionPaneHeight);
		this.clusterGraphPane.setVisible(false);

		//----------------------------
		//     Flat graph options
		//----------------------------
		
		currentX = marginX;
		currentY = yIncrement;
		
		this.treeGraph = new Button(flatGraphGroup, SWT.RADIO);
		this.treeGraph.setBounds(currentX, currentY, radioButtonWidth, labelHeight);
		this.treeGraph.setText("Tree");
		this.treeGraph.addSelectionListener(graphTypeSelectionAdapter);

		this.treeGraphPane = new Composite(flatGraphGroup, SWT.NONE);
		this.treeGraphPane.setBounds(currentX,
			currentY + yIncrement,
			specificOptionPaneWidth -15,
			specificOptionPaneHeight -100); // TODO: DECREASE
		this.treeGraphPane.setVisible(false);
		
		currentX += radioButtonWidth;

		this.meshGraph = new Button(flatGraphGroup, SWT.RADIO);
		this.meshGraph.setBounds(currentX, currentY, radioButtonWidth, labelHeight);
		this.meshGraph.setText("Mesh");
		this.meshGraph.addSelectionListener(graphTypeSelectionAdapter);

		currentX += radioButtonWidth;

		this.generalGraph = new Button(flatGraphGroup, SWT.RADIO);
		this.generalGraph.setBounds(currentX, currentY, radioButtonWidth, labelHeight);
		this.generalGraph.setText("General");
		this.generalGraph.addSelectionListener(graphTypeSelectionAdapter);
		this.generalGraph.setSelection(true);
		
		currentX = 0;
		currentY -= labelPaddingY;
		
		final Label pruningProbabilityLabel = new Label(this.treeGraphPane, SWT.NONE);
		pruningProbabilityLabel.setBounds(
			currentX, labelPaddingY, labelWidth, scaleHeight);
		pruningProbabilityLabel.setText("Pruning probability");
		
		this.pruningProbability = new Scale(this.treeGraphPane, SWT.NONE);
		this.pruningProbability.setLocation(labelWidth,-9);
		this.pruningProbability.setSize(scaleWidth, scaleHeight);
		this.pruningProbability.setSelection(50);
		this.pruningProbability.setMaximum(100);
		this.pruningProbability.setMinimum(0);
		this.pruningProbability.setIncrement(1);
		this.pruningProbability.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event)
			{
				RandomGraphCreationDialog.this.updateScaleLabel(
					RandomGraphCreationDialog.this.pruningProbability);
			}
		});
		
		currentX += scaleWidth;

		this.pruningProbabilityLabel = new Label(this.treeGraphPane, SWT.NONE);
		this.pruningProbabilityLabel.setBounds(
			labelWidth + scaleWidth, labelPaddingY, scaleLabelWidth, labelHeight);
		this.pruningProbabilityLabel.setText("50%");
		
		currentX = 0;
		
		final Label minNumOfChildrenLabel = new Label(this.treeGraphPane, SWT.NONE);
		minNumOfChildrenLabel.setBounds(
			currentX, scaleHeight, labelWidth, scaleHeight);
		minNumOfChildrenLabel.setText("Minimum branching");
		
		final Label maxNumOfChildrenLabel = new Label(this.treeGraphPane, SWT.NONE);
		maxNumOfChildrenLabel.setBounds(
			currentX, 2*scaleHeight, labelWidth, scaleHeight);
		maxNumOfChildrenLabel.setText("Maximum branching");
		
		currentX += labelWidth;
		
		this.minBranching = new Text(this.treeGraphPane, SWT.BORDER);
		this.minBranching.setBounds(currentX+8, scaleHeight-5, textFieldWidth, textFieldHeight);
		
		this.maxBranching = new Text(this.treeGraphPane, SWT.BORDER);
		this.maxBranching.setBounds(currentX+8, 2*scaleHeight - 5, textFieldWidth, textFieldHeight);
		
		currentX = 0;

		final Label iGEdgeRatio = new Label(this.compoundGraphPane, SWT.NONE);
		iGEdgeRatio.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		iGEdgeRatio.setText("Inter-graph edge ratio");

		currentX += xIncrement;

		Composite scaleContainer = new Composite(this.compoundGraphPane, SWT.NONE);
		scaleContainer.setLocation(currentX, currentY);
		scaleContainer.setSize(scaleWidth, labelHeight);

		this.iGEdgeRatio = new Scale(scaleContainer, SWT.NONE);
		this.iGEdgeRatio.setLocation(-7,-9);
		this.iGEdgeRatio.setSize(scaleWidth,scaleHeight);
		this.iGEdgeRatio.setSelection(50);
		this.iGEdgeRatio.setMaximum(100);
		this.iGEdgeRatio.setMinimum(0);
		this.iGEdgeRatio.setIncrement(1);
		this.iGEdgeRatio.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event)
			{
				RandomGraphCreationDialog.this.updateScaleLabel(
					RandomGraphCreationDialog.this.iGEdgeRatio);
			}
		});

		currentX += scaleWidth;

		this.iGEdgeRatioLabel = new Label(this.compoundGraphPane, SWT.NONE);
		this.iGEdgeRatioLabel.setBounds(
			currentX, currentY + labelPaddingY, scaleLabelWidth, labelHeight);

		currentX = 0;

		final Label iCEdgeRatioLabel = new Label(this.clusterGraphPane, SWT.NONE);
		iCEdgeRatioLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		iCEdgeRatioLabel.setText("Inter-cluster edge ratio");

		currentX += xIncrement;

		scaleContainer = new Composite(this.clusterGraphPane, SWT.NONE);
		scaleContainer.setLocation(currentX, currentY);
		scaleContainer.setSize(scaleWidth, labelHeight);

		this.iCEdgeRatio = new Scale(scaleContainer, SWT.NONE);
		this.iCEdgeRatio.setLocation(-7,-9);
		this.iCEdgeRatio.setSize(scaleWidth,scaleHeight);
		this.iCEdgeRatio.setSelection(50);
		this.iCEdgeRatio.setMaximum(100);
		this.iCEdgeRatio.setMinimum(0);
		this.iCEdgeRatio.setIncrement(1);
		this.iCEdgeRatio.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event)
			{
				RandomGraphCreationDialog.this.updateScaleLabel(
					RandomGraphCreationDialog.this.iCEdgeRatio);
			}
		});

		currentX += scaleWidth;

		this.iCEdgeRatioLabel = new Label(this.clusterGraphPane, SWT.NONE);
		this.iCEdgeRatioLabel.setBounds(
			currentX, currentY + labelPaddingY, scaleLabelWidth, labelHeight);

		currentX = 0;
		currentY += yIncrement;

		final Label branchPruningLabel = new Label(this.compoundGraphPane, SWT.NONE);
		branchPruningLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		branchPruningLabel.setText("Branch factor");

		currentX += xIncrement;

		scaleContainer = new Composite(this.compoundGraphPane, SWT.NONE);
		scaleContainer.setLocation(currentX, currentY);
		scaleContainer.setSize(scaleWidth, labelHeight);

		this.branchFactor = new Scale(scaleContainer, SWT.NONE);
		this.branchFactor.setLocation(-7,-9);
		this.branchFactor.setSize(scaleWidth,scaleHeight);
		this.branchFactor.setSelection(50);
		this.branchFactor.setMaximum(100);
		this.branchFactor.setMinimum(0);
		this.branchFactor.setIncrement(1);
		this.branchFactor.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event)
			{
				RandomGraphCreationDialog.this.updateScaleLabel(
					RandomGraphCreationDialog.this.branchFactor);
			}
		});

		currentX += scaleWidth;

		this.branchFactorLabel = new Label(this.compoundGraphPane, SWT.NONE);
		this.branchFactorLabel.setBounds(
			currentX, currentY + labelPaddingY, scaleLabelWidth, labelHeight);

		currentX = 0;

		final Label maxClusterSizeLabel = new Label(this.clusterGraphPane, SWT.NONE);
		maxClusterSizeLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		maxClusterSizeLabel.setText("Maximum cluster size");

		currentX += xIncrement;

		this.maxClusterSize = new Text(this.clusterGraphPane, SWT.BORDER);
		this.maxClusterSize.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);

		currentX = 0;
		currentY += yIncrement;
		
		final Label allowOverlapsLabel = new Label(this.clusterGraphPane, SWT.NONE);
		allowOverlapsLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		allowOverlapsLabel.setText("Allow overlaps");
		
		currentX += xIncrement;

		SelectionAdapter allowOverlapsSelectionAdapter = new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent selectionEvent)
			{
				RandomGraphCreationDialog.this.allowOverlapsSelectionChanged(selectionEvent);
			}
		};
		
		this.allowOverlaps = new Button(this.clusterGraphPane, SWT.CHECK);
		this.allowOverlaps.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
		this.allowOverlaps.addSelectionListener(allowOverlapsSelectionAdapter);
		
		currentX = 0;
		currentY += yIncrement;

		this.allowOverlapsGraphPane = new Composite(this.clusterGraphPane, SWT.NONE);
		this.allowOverlapsGraphPane.setBounds(currentX,
			currentY,
			specificOptionPaneWidth,
			specificOptionPaneHeight);
		this.allowOverlapsGraphPane.setVisible(false);
				
		currentX = 0;
		currentY -= yIncrement;
		
		final Label noOfSiblingsLabel = new Label(this.compoundGraphPane, SWT.NONE);
		noOfSiblingsLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		noOfSiblingsLabel.setText("Number of siblings");

		currentX += xIncrement;

		this.noOfSiblings = new Text(this.compoundGraphPane, SWT.BORDER);
		this.noOfSiblings.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);

		currentX = 0;
		currentY += yIncrement;

		final Label compoundDepthLabel = new Label(this.compoundGraphPane, SWT.NONE);
		compoundDepthLabel.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		compoundDepthLabel.setText("Compound depth");

		currentX += xIncrement;

		this.compoundDepth = new Text(this.compoundGraphPane, SWT.BORDER);
		this.compoundDepth.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);

		currentX  = 0;
		currentY = 0;
		
		final Label clusterOverlapFreq = new Label(this.allowOverlapsGraphPane, SWT.NONE);
		clusterOverlapFreq.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		clusterOverlapFreq.setText("Cluster overlap frequency");

		currentX += xIncrement;

		this.clusterOverlapFrequency = new Text(this.allowOverlapsGraphPane, SWT.BORDER);
		this.clusterOverlapFrequency.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
		
		currentX += textFieldWidth + 3;

		final Label percentageLabel1 = new Label(this.allowOverlapsGraphPane, SWT.NONE);
		percentageLabel1.setAlignment(SWT.LEFT);
		percentageLabel1.setBounds(currentX, currentY+labelPaddingY, smallLabelWidth, labelHeight);
		percentageLabel1.setText("%");
		
		currentX = 0;
		currentY += yIncrement;

		final Label clusterOverlapAmount = new Label(this.allowOverlapsGraphPane, SWT.NONE);
		clusterOverlapAmount.setBounds(currentX, currentY + labelPaddingY, labelWidth, labelHeight);
		clusterOverlapAmount.setText("Cluster overlap amount");

		currentX += xIncrement;

		this.clusterOverlapAmount = new Text(this.allowOverlapsGraphPane, SWT.BORDER);
		this.clusterOverlapAmount.setBounds(currentX, currentY, textFieldWidth, textFieldHeight);
		
		currentX += textFieldWidth + 3;

		final Label percentageLabel2 = new Label(this.allowOverlapsGraphPane, SWT.NONE);
		percentageLabel2.setAlignment(SWT.LEFT);
		percentageLabel2.setBounds(currentX, currentY+labelPaddingY, smallLabelWidth, labelHeight);
		percentageLabel2.setText("%");


		final Button okButton = new Button(mainContent, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				RandomGraphCreationDialog.this.storeValuesToOptionsPack();
				RandomGraphCreationDialog.this.shell.close();
				RandomGraphCreationDialog.this.result = Boolean.TRUE;
			}
		});
		okButton.setText("OK");
		okButton.setBounds(shellWidth - marginX- 3*buttonWidth - 2*buttonSeparation,
			shellHeight-marginY-buttonHeight,
			buttonWidth,
			buttonHeight);

		final Button cancelButton = new Button(mainContent, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				RandomGraphCreationDialog.this.shell.close();
			}
		});

		cancelButton.setText("Cancel");
		cancelButton.setBounds(shellWidth-marginX- 2*buttonWidth - buttonSeparation,
			shellHeight-marginY-buttonHeight,
			buttonWidth,
			buttonHeight);

		final Button createButton = new Button(mainContent, SWT.NONE);
		createButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				RandomGraphCreationDialog.this.storeValuesToOptionsPack();
				RandomGraphCreationDialog.this.createRandomGraph();
				RandomGraphCreationDialog.this.shell.close();
			}
		});

		createButton.setText("Create");
		createButton.setBounds(shellWidth-marginX-buttonWidth,
			shellHeight-marginY-buttonHeight,
			buttonWidth,
			buttonHeight);
	}

	/**
	 * This method launches RandomGraphCreator according to current set of
	 * parameters
	 */
	protected void createRandomGraph()
	{
		RandomGraphOptionsPack optionsPack =
			RandomGraphOptionsPack.getInstance();

		int noOfNodes = optionsPack.getCommon().getNoOfNodes();
		int noOfEdges = optionsPack.getCommon().getNoOfEdges();
		Dimension minNodeSize = optionsPack.getCommon().getMinNodeSize();
		Dimension maxNodeSize = optionsPack.getCommon().getMaxNodeSize();
		boolean removeDisconnectedNodes =
			optionsPack.getCommon().isRemoveDisconnectedNodes();

		RandomGraphCreator.FlatGraphType flatGraphType = 
			optionsPack.getFlat().getGraphType();
		double pruningProbability = 
			optionsPack.getFlat().getPruningProbability();
		int minBranching = optionsPack.getFlat().getMinBranching();
		int maxBranching = optionsPack.getFlat().getMaxBranching();
		
		int icEdgePercentage = optionsPack.getCluster().getICEdgePercentage();
		int maxClusterSize = optionsPack.getCluster().getMaxClusterSize();
		boolean allowOverlaps = optionsPack.getCluster().isAllowOverlaps();
		int clusterOverlapFrequency = optionsPack.getCluster().getClusterOverlapFrequency();
		int clusterOverlapAmount = optionsPack.getCluster().getClusterOverlapAmount();

		int igEdgePercentage = optionsPack.getCompound().getIGEdgePercentage();
		int depth = optionsPack.getCompound().getDepth();
		int noOfSiblings = optionsPack.getCompound().getNoOfSplings();
		// Convert the factor to pruning probability
		double branchPruningProbability =
			1.0 - optionsPack.getCompound().getBranchFactor();

		double igEdgeRatio = 0.0;

		// Check which type of graph is selected and perform necessary parameter
		// changes that will imply the graph type for the RandomGraphCreator

		if (this.flatGraph.getSelection())
		{
			depth = 0;
			maxClusterSize = 1;
			igEdgeRatio = 1.0;
		}
		else if (this.clusterGraph.getSelection())
		{
			igEdgeRatio = icEdgePercentage / 100.0;
			depth = 0;
		}
		else if (this.compoundGraph.getSelection())
		{
			igEdgeRatio = igEdgePercentage / 100.0;
		}

		// Create the generator and run it
		RandomGraphCreator creator = new RandomGraphCreator(this.main,
			noOfNodes,
			noOfEdges,
			depth,
			igEdgeRatio,
			noOfSiblings,
			branchPruningProbability,
			maxClusterSize,
			2,
			allowOverlaps,
			clusterOverlapFrequency,
			clusterOverlapAmount,
			minNodeSize,
			maxNodeSize,
			removeDisconnectedNodes,
			flatGraphType,
			minBranching,
			maxBranching,
			pruningProbability);

		this.result = creator.run();
	}

	protected void updateScaleLabel(Scale scale)
	{
		int value = scale.getSelection();

		if (this.branchFactor == scale)
		{
			this.branchFactorLabel.setText(String.valueOf(value/100.0));
		}
		else if (this.iCEdgeRatio == scale)
		{
			this.iCEdgeRatioLabel.setText(String.valueOf(value));
		}
		else if (this.iGEdgeRatio == scale)
		{
			this.iGEdgeRatioLabel.setText(String.valueOf(value));
		}
		else if (this.pruningProbability == scale)
		{
			this.pruningProbabilityLabel.setText(String.valueOf(value) + "%");
		}
	}

	private void allowOverlapsSelectionChanged(SelectionEvent selectionEvent)
	{
		Button allowOverlapsButton = (Button) selectionEvent.getSource();
		
		boolean isSelected = allowOverlapsButton.getSelection();
		
		if(isSelected)
		{
			this.allowOverlapsGraphPane.setVisible(true);
		}
		else
		{
			this.allowOverlapsGraphPane.setVisible(false);
			
		}
	}
	
	private void graphTypeSelectionChanged(SelectionEvent selectionEvent)
	{
		Button sourceButton = (Button) selectionEvent.getSource();

		boolean isSelected = sourceButton.getSelection();

		if (sourceButton == this.compoundGraph)
		{
			this.compoundGraphPane.setVisible(isSelected);
		}
		else if (sourceButton == this.clusterGraph)
		{
			this.clusterGraphPane.setVisible(isSelected);
		}
		if (sourceButton == this.flatGraph)
		{
			this.flatGraphPane.setVisible(isSelected);
		}

		if (sourceButton == this.treeGraph)
		{
			this.treeGraphPane.setVisible(true);
		}
		else if ((sourceButton == this.meshGraph) || (sourceButton == this.generalGraph))
		{
			this.treeGraphPane.setVisible(false);
		}
	}


	private void storeValuesToOptionsPack()
	{
		RandomGraphOptionsPack rgop = RandomGraphOptionsPack.getInstance();

		// Common options
		rgop.getCommon().setNoOfNodes(
			Integer.valueOf(this.numberOfNodes.getText()));

		rgop.getCommon().setNoOfEdges(
			Integer.valueOf(this.numberOfEdges.getText()));

		Dimension minNodeSize = new Dimension(
			Integer.valueOf(this.minNodeSizeWidth.getText()),
			Integer.valueOf(this.minNodeSizeHeight.getText()));

		Dimension maxNodeSize = new Dimension(
			Integer.valueOf(this.maxNodeSizeWidth.getText()),
			Integer.valueOf(this.maxNodeSizeHeight.getText()));

		rgop.getCommon().setMaxNodeSize(maxNodeSize);
		rgop.getCommon().setMinNodeSize(minNodeSize);

		rgop.getCommon().setRemoveDisconnectedNodes(
			this.removeDisconnectedNodes.getSelection());

		if (this.flatGraph.getSelection())
		{
			rgop.getCommon().setGraphType(RandomGraphCreator.FLAT_GRAPH);
			
			if (this.treeGraph.getSelection())
			{
				rgop.getFlat().setGraphType(RandomGraphCreator.FlatGraphType.TREE);
				rgop.getFlat().setPruningProbability((double)this.pruningProbability.getSelection() / 100);
				rgop.getFlat().setMinBranching(Integer.valueOf(this.minBranching.getText()));
				rgop.getFlat().setMaxBranching(Integer.valueOf(this.maxBranching.getText()));
			}
			else if(this.meshGraph.getSelection())
			{
				rgop.getFlat().setGraphType(RandomGraphCreator.FlatGraphType.MESH);
			}
			else if(this.generalGraph.getSelection())
			{
				rgop.getFlat().setGraphType(RandomGraphCreator.FlatGraphType.GENERAL);
			}
		}
		else if (this.clusterGraph.getSelection())
		{
			rgop.getCommon().setGraphType(RandomGraphCreator.CLUSTER_GRAPH);
		}
		else if (this.compoundGraph.getSelection())
		{
			rgop.getCommon().setGraphType(RandomGraphCreator.COMPOUND_GRAPH);
		}

		// Cluster graph options
		rgop.getCluster().setMaxClusterSize(
			Integer.valueOf(this.maxClusterSize.getText()));
		rgop.getCluster().setICEdgePercentage(
			Integer.valueOf(this.iCEdgeRatio.getSelection()));
		rgop.getCluster().setAllowOverlaps(this.allowOverlaps.getSelection());
		rgop.getCluster().setClusterOverlapFrequency(
			Integer.valueOf(this.clusterOverlapFrequency.getText()));
		rgop.getCluster().setClusterOverlapAmount(
			Integer.valueOf(this.clusterOverlapAmount.getText()));

		// Compound graph options
		rgop.getCompound().setIGEdgePercentage(
			Integer.valueOf(this.iGEdgeRatio.getSelection()));
		rgop.getCompound().setDepth(
			Integer.valueOf(this.compoundDepth.getText()));
		rgop.getCompound().setBranchFactor(
			this.branchFactor.getSelection() / 100.0);
		rgop.getCompound().setNoOfSiblings(
			Integer.valueOf(this.noOfSiblings.getText()));
	}

	private void setInitialValues()
	{
		RandomGraphOptionsPack rgop = RandomGraphOptionsPack.getInstance();

		// Common options
		this.numberOfNodes.setText(
			String.valueOf(rgop.getCommon().getNoOfNodes()));

		this.numberOfEdges.setText(
				String.valueOf(rgop.getCommon().getNoOfEdges()));

		Dimension minNodeSize = rgop.getCommon().getMinNodeSize();
		Dimension maxNodeSize = rgop.getCommon().getMaxNodeSize();

		this.minNodeSizeHeight.setText(String.valueOf(minNodeSize.height));
		this.minNodeSizeWidth.setText(String.valueOf(minNodeSize.width));

		this.maxNodeSizeHeight.setText(String.valueOf(maxNodeSize.height));
		this.maxNodeSizeWidth.setText(String.valueOf(maxNodeSize.width));

		this.removeDisconnectedNodes.setSelection(
			rgop.getCommon().isRemoveDisconnectedNodes());

		this.pruningProbability.setSelection((int)(rgop.getFlat().getPruningProbability()));
		this.pruningProbabilityLabel.setText(this.pruningProbability.getSelection() + "%");
		this.minBranching.setText(String.valueOf(rgop.getFlat().getMinBranching()));
		this.maxBranching.setText(String.valueOf(rgop.getFlat().getMaxBranching()));
		
		int graphType = rgop.getCommon().getGraphType();

		if (graphType == RandomGraphCreator.FLAT_GRAPH)
		{
			this.flatGraph.setSelection(true);
			this.compoundGraph.setSelection(false);
			this.clusterGraph.setSelection(false);

			this.flatGraphPane.setVisible(true);
			this.compoundGraphPane.setVisible(false);
			this.clusterGraphPane.setVisible(false);
			
			RandomGraphCreator.FlatGraphType flatGraphType = 
				rgop.getFlat().getGraphType();
			
			if (flatGraphType == RandomGraphCreator.FlatGraphType.TREE)
			{
				this.treeGraph.setSelection(true);
				this.meshGraph.setSelection(false);
				this.generalGraph.setSelection(false);
				
				this.treeGraphPane.setVisible(true);
			}
			else if(flatGraphType == RandomGraphCreator.FlatGraphType.MESH)
			{
				this.treeGraph.setSelection(false);
				this.meshGraph.setSelection(true);
				this.generalGraph.setSelection(false);
				
				this.treeGraphPane.setVisible(false);
			}
			else if(flatGraphType == RandomGraphCreator.FlatGraphType.GENERAL)
			{
				this.treeGraph.setSelection(false);
				this.meshGraph.setSelection(false);
				this.generalGraph.setSelection(true);
				
				this.treeGraphPane.setVisible(false);
			}
		}
		else if (graphType == RandomGraphCreator.COMPOUND_GRAPH)
		{
			this.flatGraph.setSelection(false);
			this.compoundGraph.setSelection(true);
			this.clusterGraph.setSelection(false);

			this.flatGraphPane.setVisible(false);
			this.compoundGraphPane.setVisible(true);
			this.clusterGraphPane.setVisible(false);
		}
		else if (graphType == RandomGraphCreator.CLUSTER_GRAPH)
		{
			this.flatGraph.setSelection(false);
			this.compoundGraph.setSelection(false);
			this.clusterGraph.setSelection(true);

			this.flatGraphPane.setVisible(false);
			this.compoundGraphPane.setVisible(false);
			this.clusterGraphPane.setVisible(true);
		}

		// Cluster graph options
		this.maxClusterSize.setText(
			String.valueOf(rgop.getCluster().getMaxClusterSize()));
		this.iCEdgeRatio.setSelection(
			rgop.getCluster().getICEdgePercentage());
		this.allowOverlaps.setSelection(
			rgop.getCluster().isAllowOverlaps());
		if(rgop.getCluster().isAllowOverlaps())
		{
			this.allowOverlapsGraphPane.setVisible(true);
		}
		else
		{
			this.allowOverlapsGraphPane.setVisible(false);
		}
		this.clusterOverlapFrequency.setText(
			String.valueOf(rgop.getCluster().getClusterOverlapFrequency()));
		this.clusterOverlapAmount.setText(
			String.valueOf(rgop.getCluster().getClusterOverlapAmount()));

		// Compound graph options
		this.iGEdgeRatio.setSelection(
			rgop.getCompound().getIGEdgePercentage());
		this.compoundDepth.setText(
			String.valueOf(rgop.getCompound().getDepth()));
		this.branchFactor.setSelection(
			(int)(rgop.getCompound().getBranchFactor()* 100));
		this.noOfSiblings.setText(
			String.valueOf(rgop.getCompound().getNoOfSplings()));

		this.updateScaleLabel(this.branchFactor);
		this.updateScaleLabel(this.iGEdgeRatio);
		this.updateScaleLabel(this.iCEdgeRatio);
	}

	public static RandomGraphCreationDialog getInstance()
	{
		return RandomGraphCreationDialog.instance;
	}

	private static RandomGraphCreationDialog instance;
}