package org.gvt.inspector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.gvt.ChisioMain;
import org.gvt.action.*;
import org.ivis.layout.LayoutConstants;
import org.ivis.layout.LayoutOptionsPack;
import org.ivis.layout.avsdf.AVSDFConstants;
import org.ivis.layout.cise.CiSEConstants;
import org.ivis.layout.cose.CoSEConstants;
import org.ivis.layout.fd.FDLayoutConstants;
import org.ivis.layout.sgym.SgymConstants;
import org.ivis.layout.spring.SpringConstants;

/**
 * This class maintains the Layout Properties dialog to set the parameters of
 * each layout
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class LayoutInspector extends Dialog
{
	//General
	private Text nodeSeparation;
	private Button leftToRight;
	private Button topToBottom;
	private Button incremental;
	private Button proofButton;
	private Button defaultButton;
	private Button draftButton;
	private Group tuningGroup;
	private Scale animationPeriod;
	protected Button animateDuringLayoutButton;
	protected Button animateOnLayoutButton;
	private Button createBendsAsButton;
	private Button uniformLeafNodeSizesButton;

	//CoSE
	private Scale gravityStrength,
		gravityRange,
		springStrength,
		repulsionStrength,
		compoundGravityStrength,
		compoundGravityRange;
	private Text desiredEdgeLengthCoSE;
	private Button smartEdgeLengthCalc;
	private Button multiLevelScaling;
	private Button smartRepulsionRangeCalc;
	
	//Sgym
	private Text verticalSpacing;
	private Text horizantalSpacing;

	//CiSE
	private Text nodeSeparationCiSE;
	private Text desiredEdgeLengthCiSE;
	private Scale interClusterEdgeLengthFactorCiSE;
	private Button allowNodesInsideCircles;
	private Scale maxRatioOfNodesInsideCircle;
	
	//Spring
	private Text disconnectedNodeDistanceSpringRestLength;
	private Text nodeDistanceRestLength;

	//Cluster
	private Text desiredEdgeLengthCluster;
	private Scale clusterSeparation;
	private Scale clusterGravityStrength;

	protected Object result;

	protected Shell shell;

	private ChisioMain main;

	public static int lastTab = 0;

	private KeyAdapter keyAdapter = new KeyAdapter()
	{
		public void keyPressed(KeyEvent arg0)
		{
			arg0.doit = isDigit(arg0.keyCode);
		}

		public boolean isDigit(int keyCode)
		{
			if (Character.isDigit(keyCode)
				|| keyCode == SWT.DEL
				|| keyCode == 8
				|| keyCode == SWT.ARROW_LEFT
				|| keyCode == SWT.ARROW_RIGHT)
			{
				return true;
			}
			return false;
		}
	};

	public static void main(String args[])
	{
		//	new LayoutInspector(new Shell(), SWT.TITLE).open();
	}

	/**
	 * Create the dialog
	 */
	public LayoutInspector(ChisioMain main)
	{
		super(main.getShell(), SWT.NONE);
		this.main = main;
	}

	/**
	 * Open the dialog
	 */
	public Object open()
	{
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	/**
	 * Create contents of the dialog
	 */
	protected void createContents()
	{
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(420, 355);
		shell.setText("Layout Properties");

		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 10, 395, 269);

		final TabItem generalTabItem = new TabItem(tabFolder, SWT.NONE);
		generalTabItem.setText("General");

		final Composite compositeGeneral = new Composite(tabFolder, SWT.NONE);
		generalTabItem.setControl(compositeGeneral);

		final Group animationGroup = new Group(compositeGeneral, SWT.NONE);
		animationGroup.setText("Animation");
		animationGroup.setBounds(10, 10, 237, 131);

		final Label animationPeriodLabel = new Label(animationGroup, SWT.NONE);
		animationPeriodLabel.setBounds(10, 94, 118, 20);
		animationPeriodLabel.setText("Animation Period");

		animationPeriod = new Scale(animationGroup, SWT.NONE);
		animationPeriod.setBounds(130, 85, 100, 33);
		animationPeriod.setSelection(50);
		animationPeriod.setIncrement(5);

		animateOnLayoutButton = new Button(animationGroup, SWT.CHECK);
		animateOnLayoutButton.setText("Animate on Layout");
		animateOnLayoutButton.setBounds(10, 28, 183, 20);

		animateDuringLayoutButton = new Button(animationGroup, SWT.CHECK);
		animateDuringLayoutButton.setAlignment(SWT.UP);
		animateDuringLayoutButton.setText("Animate during Layout");
		animateDuringLayoutButton.setBounds(10, 61, 183, 20);

		final Group layoutQualityGroup = new Group(compositeGeneral, SWT.NONE);
		layoutQualityGroup.setBounds(253, 10, 120, 100);
		layoutQualityGroup.setText("Layout Quality");

		draftButton = new Button(layoutQualityGroup, SWT.RADIO);
		draftButton.setBounds(10, 21, 87, 20);
		draftButton.setText("Draft");

		defaultButton = new Button(layoutQualityGroup, SWT.RADIO);
		defaultButton.setBounds(10, 43, 87, 20);
		defaultButton.setText("Default");

		proofButton = new Button(layoutQualityGroup, SWT.RADIO);
		proofButton.setBounds(10, 65, 87, 20);
		proofButton.setText("Proof");

		incremental = new Button(compositeGeneral, SWT.CHECK);
		incremental.setBounds(10, 147, 107, 20);
		incremental.setText("Incremental");

		createBendsAsButton = new Button(compositeGeneral, SWT.CHECK);
		createBendsAsButton.setText("Create Bends as Needed");
		createBendsAsButton.setBounds(10, 173, 226, 20);

		uniformLeafNodeSizesButton = new Button(compositeGeneral, SWT.CHECK);
		uniformLeafNodeSizesButton.setText("Uniform Leaf Node Sizes");
		uniformLeafNodeSizesButton.setBounds(226, 147, 226, 20);
		
		final TabItem coseTabItem = new TabItem(tabFolder, SWT.NONE);
		coseTabItem.setText("CoSE");

		final TabItem clusterTabItem = new TabItem(tabFolder, SWT.NONE);
		clusterTabItem.setText("Cluster");

		final TabItem ciSETabItem = new TabItem(tabFolder, SWT.NONE);
		ciSETabItem.setText("CiSE");

		final TabItem avsdfTabItem = new TabItem(tabFolder, SWT.NONE);
		avsdfTabItem.setText("Circular");

		final Composite compositeAVSDF = new Composite(tabFolder, SWT.NONE);
		avsdfTabItem.setControl(compositeAVSDF);

		final Label nodeSeparationLabel = new Label(compositeAVSDF, SWT.NONE);
		nodeSeparationLabel.setBounds(10, 28, 129, 20);
		nodeSeparationLabel.setText("Node Separation");

		nodeSeparation = new Text(compositeAVSDF, SWT.BORDER);
		nodeSeparation.addKeyListener(keyAdapter);
		nodeSeparation.setBounds(145, 22, 41, 25);

		final TabItem springTabItem = new TabItem(tabFolder, SWT.NONE);
		springTabItem.setText("Spring");

		final Composite compositeCoSE = new Composite(tabFolder, SWT.NONE);
		coseTabItem.setControl(compositeCoSE);

		final Label desiredEdgeLengthLabel = new Label(compositeCoSE, SWT.NONE);
		desiredEdgeLengthLabel.setText("Desired Edge Length");
		desiredEdgeLengthLabel.setBounds(10, 183, 110, 20);

		desiredEdgeLengthCoSE = new Text(compositeCoSE, SWT.BORDER);
		desiredEdgeLengthCoSE.addKeyListener(keyAdapter);
		desiredEdgeLengthCoSE.setBounds(125, 177, 41, 25);
		
		smartEdgeLengthCalc = new Button(compositeCoSE, SWT.CHECK);
		smartEdgeLengthCalc.setText("Smart Edge Length Calculation");
		smartEdgeLengthCalc.setBounds(195, 177, 180, 25);
		
		multiLevelScaling = new Button(compositeCoSE, SWT.CHECK);
		multiLevelScaling.setText("Multi-Level Scaling");
		multiLevelScaling.setBounds(195, 208, 165, 25);
		
		tuningGroup = new Group(compositeCoSE, SWT.NONE);
		tuningGroup.setText("Tuning");
		tuningGroup.setBounds(10, 10, 365, 160);
		
		smartRepulsionRangeCalc = new Button(tuningGroup, SWT.CHECK);
		smartRepulsionRangeCalc.setText("Smart Range Calculation");
		smartRepulsionRangeCalc.setBounds(210, 59, 150, 25);

		final Label gravityLevelLabel = new Label(tuningGroup, SWT.NONE);
		gravityLevelLabel.setBounds(10, 94, 102, 20);
		gravityLevelLabel.setText("Gravity");

		gravityStrength = new Scale(tuningGroup, SWT.NONE);
		gravityStrength.setBounds(107, 85, 100, 33);
		gravityStrength.setIncrement(5);
		
		final Label gravityRangeLabel = new Label(tuningGroup,
			SWT.NONE);
		gravityRangeLabel.setBounds(210, 94, 35, 20);
		gravityRangeLabel.setText("Range");
		
		gravityRange = new Scale(tuningGroup, SWT.NONE);
		gravityRange.setBounds(250, 85, 100, 33);
		gravityRange.setIncrement(5);

		final Label springStrengthLabel = new Label(tuningGroup, SWT.NONE);
		springStrengthLabel.setBounds(10, 28, 102, 20);
		springStrengthLabel.setText("Spring");

		springStrength = new Scale(tuningGroup, SWT.NONE);
		springStrength.setBounds(107, 19, 100, 33);
		springStrength.setIncrement(5);

		final Label repulsionStrengthLabel = new Label(tuningGroup, SWT.NONE);
		repulsionStrengthLabel.setBounds(10, 61, 102, 20);
		repulsionStrengthLabel.setText("Repulsion");

		repulsionStrength = new Scale(tuningGroup, SWT.NONE);
		repulsionStrength.setBounds(107, 52, 100, 33);
		repulsionStrength.setIncrement(5);

		final Label compoundGravityStrengthLabel = new Label(tuningGroup,
			SWT.NONE);
		compoundGravityStrengthLabel.setBounds(10, 127, 102, 20);
		compoundGravityStrengthLabel.setText("Compound Gravity");

		compoundGravityStrength = new Scale(tuningGroup, SWT.NONE);
		compoundGravityStrength.setBounds(107, 118, 100, 33);
		compoundGravityStrength.setIncrement(5);
		
		final Label compoundGravityRangeLabel = new Label(tuningGroup,
			SWT.NONE);
		compoundGravityRangeLabel.setBounds(210, 127, 35, 20);
		compoundGravityRangeLabel.setText("Range");
		
		compoundGravityRange = new Scale(tuningGroup, SWT.NONE);
		compoundGravityRange.setBounds(250, 118, 100, 33);
		compoundGravityRange.setIncrement(5);
		
		compositeCoSE.setTabList(new Control[] {tuningGroup});

		final TabItem sugiyamaTabItem = new TabItem(tabFolder, SWT.NONE);
		sugiyamaTabItem.setText("Sugiyama");

		final Composite compositeSugiyama = new Composite(tabFolder, SWT.NONE);
		sugiyamaTabItem.setControl(compositeSugiyama);

		final Group spacingGroup = new Group(compositeSugiyama, SWT.NONE);
		spacingGroup.setText("Spacing");
		spacingGroup.setBounds(10, 10, 191, 100);

		final Label horizantalSpacingLabel = new Label(spacingGroup, SWT.NONE);
		horizantalSpacingLabel.setBounds(10, 28, 124, 20);
		horizantalSpacingLabel.setText("Horizantal Spacing");

		final Label verticalSpacingLabel = new Label(spacingGroup, SWT.NONE);
		verticalSpacingLabel.setBounds(10, 61, 124, 20);
		verticalSpacingLabel.setText("Vertical Spacing");

		verticalSpacing = new Text(spacingGroup, SWT.BORDER);
		verticalSpacing.addKeyListener(keyAdapter);
		verticalSpacing.setBounds(140, 55, 40, 25);

		horizantalSpacing = new Text(spacingGroup, SWT.BORDER);
		horizantalSpacing.addKeyListener(keyAdapter);
		horizantalSpacing.setBounds(140, 22, 40, 25);

		final Group orientationGroup = new Group(compositeSugiyama, SWT.NONE);
		orientationGroup.setBounds(207, 10, 153, 80);
		orientationGroup.setText("Orientation");

		topToBottom = new Button(orientationGroup, SWT.RADIO);
		topToBottom.setBounds(10, 21, 133, 20);
		topToBottom.setText("Top-to-Bottom");

		leftToRight = new Button(orientationGroup, SWT.RADIO);
		leftToRight.setBounds(10, 43, 133, 20);
		leftToRight.setText("Left-to-Right");

		final Composite compositeSpring = new Composite(tabFolder, SWT.NONE);
		springTabItem.setControl(compositeSpring);

		final Label nodedistancerestlengthLabel = new Label(compositeSpring,
			SWT.NONE);
		nodedistancerestlengthLabel.setText("Desired Edge Length");
		nodedistancerestlengthLabel.setBounds(10, 28, 154, 20);

		final Label disconnectednodedistancespringrestlengthLabel = new Label(
			compositeSpring,
			SWT.NONE);
		disconnectednodedistancespringrestlengthLabel.
			setText("Disconnected Component Separation");
		disconnectednodedistancespringrestlengthLabel.
			setBounds(10, 61, 269, 20);

		nodeDistanceRestLength = new Text(compositeSpring, SWT.BORDER);
		nodeDistanceRestLength.addKeyListener(keyAdapter);
		nodeDistanceRestLength.setBounds(280, 22, 42, 25);

		disconnectedNodeDistanceSpringRestLength = new Text(compositeSpring,
			SWT.BORDER);
		disconnectedNodeDistanceSpringRestLength.addKeyListener(keyAdapter);
		disconnectedNodeDistanceSpringRestLength.setBounds(280, 55, 42, 25);

		final Composite compositeCluster = new Composite(tabFolder, SWT.NONE);
		clusterTabItem.setControl(compositeCluster);

		final Label desiredEdgeLengthLabel_1 = new Label(compositeCluster,
			SWT.NONE);
		desiredEdgeLengthLabel_1.setBounds(10, 94, 154, 20);
		desiredEdgeLengthLabel_1.setText("Desired Edge Length");

		desiredEdgeLengthCluster = new Text(compositeCluster, SWT.BORDER);
		desiredEdgeLengthCluster.addKeyListener(keyAdapter);
		desiredEdgeLengthCluster.setBounds(183, 88, 41, 25);

		final Label clusterSeparationLabel = new Label(compositeCluster,
			SWT.NONE);
		clusterSeparationLabel.setBounds(10, 28,129, 20);
		clusterSeparationLabel.setText("Cluster Separation");

		clusterSeparation = new Scale(compositeCluster, SWT.NONE);
		clusterSeparation.setBounds(175, 19,100, 33);
		clusterSeparation.setIncrement(5);

		final Label clusterGravityStrengthLabel = new Label(compositeCluster, SWT.NONE);
		clusterGravityStrengthLabel.setBounds(10, 61, 162, 20);
		clusterGravityStrengthLabel.setText("Cluster Gravity Strength");

		clusterGravityStrength = new Scale(compositeCluster, SWT.NONE);
		clusterGravityStrength.setBounds(175, 52, 100, 33);
		clusterGravityStrength.setSelection(50);
		clusterGravityStrength.setIncrement(5);

		final Composite compositeCiSE = new Composite(tabFolder, SWT.NONE);
		ciSETabItem.setControl(compositeCiSE);

		final Label nodeSeparationLabel_2 = new Label(compositeCiSE,
			SWT.NONE);
		nodeSeparationLabel_2.setBounds(10, 28, 139, 20);
		nodeSeparationLabel_2.setText("Node Seperation");

		nodeSeparationCiSE = new Text(compositeCiSE, SWT.BORDER);
		nodeSeparationCiSE.addKeyListener(keyAdapter);
		nodeSeparationCiSE.setBounds(248, 22, 41, 25);

		final Label desiredEdgeLengthLabel_2 = new Label(compositeCiSE,
				SWT.NONE);
		desiredEdgeLengthLabel_2.setBounds(10, 61, 155, 20);
		desiredEdgeLengthLabel_2.setText("Desired Edge Length");

		desiredEdgeLengthCiSE = new Text(compositeCiSE, SWT.BORDER);
		desiredEdgeLengthCiSE.addKeyListener(keyAdapter);
		desiredEdgeLengthCiSE.setBounds(248, 55, 41, 25);

		final Label interClusterEdgeLengthFactorLabel_2 =
			new Label(compositeCiSE, SWT.NONE);
		interClusterEdgeLengthFactorLabel_2.setBounds(10, 96, 231, 20);
		interClusterEdgeLengthFactorLabel_2.setText(
				"Inter-Cluster Edge Length Factor");

		interClusterEdgeLengthFactorCiSE = new Scale(compositeCiSE, SWT.NONE);
		interClusterEdgeLengthFactorCiSE.setBounds(240, 85, 100, 33);
		interClusterEdgeLengthFactorCiSE.setSelection(50);
		interClusterEdgeLengthFactorCiSE.setIncrement(5);

		allowNodesInsideCircles = new Button(compositeCiSE, SWT.CHECK);
		allowNodesInsideCircles.setText("Allow Nodes Inside Circles");
		allowNodesInsideCircles.setBounds(10, 125, 250, 25);
		
		final Label maxRatioOfNodesInsideCircleLabel =
			new Label(compositeCiSE, SWT.NONE);
		maxRatioOfNodesInsideCircleLabel.setBounds(10, 160, 231, 20);
		maxRatioOfNodesInsideCircleLabel.setText(
				"Max Ratio Of Nodes Inside Circle");
		
		maxRatioOfNodesInsideCircle = new Scale(compositeCiSE, SWT.NONE);
		maxRatioOfNodesInsideCircle.setBounds(240, 148, 100, 33);
		maxRatioOfNodesInsideCircle.setIncrement(5);
		
		final Button okButton = new Button(shell, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				storeValuesToOptionsPack();
				lastTab = tabFolder.getSelectionIndex();
				shell.close();
			}
		});
		okButton.setText("OK");

		final Button layoutButton = new Button(shell, SWT.NONE);
		layoutButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				int i = tabFolder.getSelectionIndex();
				storeValuesToOptionsPack();

				switch (i)
				{
					case 1:
						new CoSELayoutAction(main).run();
						break;
					case 2:
						new ClusterLayoutAction(main).run();
						break;
					case 3:
						new CiSELayoutAction(main).run();
						break;
					case 4:
						new AVSDFLayoutAction(main).run();
						break;
					case 5:
						new SpringLayoutAction(main).run();
						break;
					case 6:
						new SugiyamaLayoutAction(main).run();
						break;
				}
			}
		});

		layoutButton.setText("Layout");

		final Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				lastTab = tabFolder.getSelectionIndex();
				shell.close();
			}
		});

		cancelButton.setText("Cancel");

		final Button defaultButton2 = new Button(shell, SWT.NONE);
		defaultButton2.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				int i = tabFolder.getSelectionIndex();
				setDefaultLayoutProperties(i);
			}
		});

		defaultButton2.setText("Default");

		okButton.setBounds(128, 285, 60, 30);
		cancelButton.setBounds(194, 285, 60, 30);
		layoutButton.setBounds(262, 285, 60, 30);
		defaultButton2.setBounds(328, 285, 60, 30);

		if (lastTab == 0)
		{
			layoutButton.setEnabled(false);
		}

		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0)
			{
				if (tabFolder.getSelectionIndex() == 0)
				{
					layoutButton.setEnabled(false);
				}
				else
				{
					layoutButton.setEnabled(true);
				}
			}
		});

		setInitialValues();

		tabFolder.setSelection(lastTab);
	}

	public void storeValuesToOptionsPack()
	{
		LayoutOptionsPack lop = LayoutOptionsPack.getInstance();

		//General
		lop.getGeneral().animationPeriod = animationPeriod.getSelection();
		lop.getGeneral().animationOnLayout =
			animateOnLayoutButton.getSelection();
		lop.getGeneral().animationDuringLayout =
			animateDuringLayoutButton.getSelection();

		if (proofButton.getSelection())
		{
			lop.getGeneral().layoutQuality = LayoutConstants.PROOF_QUALITY;
		}
		else if (draftButton.getSelection())
		{
			lop.getGeneral().layoutQuality = LayoutConstants.DRAFT_QUALITY;
		}
		else
		{
			lop.getGeneral().layoutQuality = LayoutConstants.DEFAULT_QUALITY;
		}

		lop.getGeneral().incremental = incremental.getSelection();
		lop.getGeneral().createBendsAsNeeded =
			createBendsAsButton.getSelection();
		lop.getGeneral().uniformLeafNodeSizes =
			uniformLeafNodeSizesButton.getSelection();
		
		//CoSE
		lop.getCoSE().idealEdgeLength = Integer.parseInt(desiredEdgeLengthCoSE.getText());
		lop.getCoSE().smartEdgeLengthCalc = smartEdgeLengthCalc.getSelection();
		lop.getCoSE().multiLevelScaling = multiLevelScaling.getSelection();
		lop.getCoSE().smartRepulsionRangeCalc = smartRepulsionRangeCalc.getSelection();
		lop.getCoSE().compoundGravityStrength = compoundGravityStrength.getSelection();
		lop.getCoSE().compoundGravityRange = compoundGravityRange.getSelection();
		lop.getCoSE().gravityStrength = gravityStrength.getSelection();
		lop.getCoSE().gravityRange = gravityRange.getSelection();
		lop.getCoSE().repulsionStrength = repulsionStrength.getSelection();
		lop.getCoSE().springStrength = springStrength.getSelection();

		//Cluster
		lop.getCluster().idealEdgeLength = Integer.parseInt(desiredEdgeLengthCluster.getText());
		lop.getCluster().clusterSeperation = clusterSeparation.getSelection();
		lop.getCluster().clusterGravityStrength = clusterGravityStrength.getSelection();

		//CiSE
		lop.getCiSE().nodeSeparation = Integer.parseInt(nodeSeparationCiSE.getText());
		lop.getCiSE().desiredEdgeLength = Integer.parseInt(desiredEdgeLengthCiSE.getText());
		lop.getCiSE().interClusterEdgeLengthFactor = interClusterEdgeLengthFactorCiSE.getSelection();
		lop.getCiSE().maxRatioOfNodesInsideCircle = (double) maxRatioOfNodesInsideCircle.getSelection() / 100;
		lop.getCiSE().allowNodesInsideCircle = allowNodesInsideCircles.getSelection();
		
		//AVSDF
		lop.getAVSDF().nodeSeparation = Integer.parseInt(nodeSeparation.getText());

		//Spring
		lop.getSpring().nodeDistanceRestLength = Integer.parseInt(nodeDistanceRestLength.getText());
		lop.getSpring().disconnectedNodeDistanceSpringRestLength = Integer.parseInt(disconnectedNodeDistanceSpringRestLength.getText());

		//SGYM
		lop.getSgym().horizontalSpacing = Integer.parseInt(horizantalSpacing.getText());
		lop.getSgym().verticalSpacing = Integer.parseInt(verticalSpacing.getText());
		lop.getSgym().vertical = topToBottom.getSelection();
	}

	public void setInitialValues()
	{
		LayoutOptionsPack lop = LayoutOptionsPack.getInstance();

		//General
		animationPeriod.setSelection(lop.getGeneral().animationPeriod);
		animateDuringLayoutButton.setSelection(lop.getGeneral().animationDuringLayout);
		animateOnLayoutButton.setSelection(lop.getGeneral().animationOnLayout);

		if (lop.getGeneral().layoutQuality == LayoutConstants.PROOF_QUALITY)
		{
			proofButton.setSelection(true);
		}
		else if (lop.getGeneral().layoutQuality == LayoutConstants.DRAFT_QUALITY)
		{
			draftButton.setSelection(true);
		}
		else
		{
			defaultButton.setSelection(true);
		}

		incremental.setSelection(lop.getGeneral().incremental);
		createBendsAsButton.setSelection(lop.getGeneral().createBendsAsNeeded);
		uniformLeafNodeSizesButton.setSelection(lop.getGeneral().uniformLeafNodeSizes);

		//CoSE
		desiredEdgeLengthCoSE.setText(String.valueOf(lop.getCoSE().idealEdgeLength));
		smartEdgeLengthCalc.setSelection(lop.getCoSE().smartEdgeLengthCalc);
		multiLevelScaling.setSelection(lop.getCoSE().multiLevelScaling);
		smartRepulsionRangeCalc.setSelection(lop.getCoSE().smartRepulsionRangeCalc);
		gravityStrength.setSelection(lop.getCoSE().gravityStrength);
		gravityRange.setSelection(lop.getCoSE().gravityRange);
		compoundGravityStrength.setSelection(lop.getCoSE().compoundGravityStrength);
		compoundGravityRange.setSelection(lop.getCoSE().compoundGravityRange);
		repulsionStrength.setSelection(lop.getCoSE().repulsionStrength);
		springStrength.setSelection(lop.getCoSE().springStrength);

		//Cluster
		desiredEdgeLengthCluster.setText(String.valueOf(lop.getCluster().idealEdgeLength));
		clusterSeparation.setSelection(lop.getCluster().clusterSeperation);
		clusterGravityStrength.setSelection(lop.getCluster().clusterGravityStrength);

		//CiSE
		nodeSeparationCiSE.setText(String.valueOf(lop.getCiSE().nodeSeparation));
		desiredEdgeLengthCiSE.setText(String.valueOf(lop.getCiSE().desiredEdgeLength));
		interClusterEdgeLengthFactorCiSE.setSelection(lop.getCiSE().interClusterEdgeLengthFactor);
		maxRatioOfNodesInsideCircle.setSelection((int)(lop.getCiSE().maxRatioOfNodesInsideCircle * 100));
		allowNodesInsideCircles.setSelection(lop.getCiSE().allowNodesInsideCircle);
		
		//AVSDF
		nodeSeparation.setText(String.valueOf(lop.getAVSDF().nodeSeparation));

		//Spring
		nodeDistanceRestLength.setText(String.valueOf(lop.getSpring().nodeDistanceRestLength));
		disconnectedNodeDistanceSpringRestLength.setText(String.valueOf(lop.getSpring().disconnectedNodeDistanceSpringRestLength));

		//SGYM
		horizantalSpacing.setText(String.valueOf(lop.getSgym().horizontalSpacing));
		verticalSpacing.setText(String.valueOf(lop.getSgym().verticalSpacing));

		if (lop.getSgym().vertical)
		{
			topToBottom.setSelection(true);
		}
		else
		{
			leftToRight.setSelection(true);
		}
	}

	public void setDefaultLayoutProperties(int select)
	{
		if (select == 0)
		{
			//General
			animateDuringLayoutButton.setSelection(
				LayoutConstants.DEFAULT_ANIMATION_DURING_LAYOUT);
			animationPeriod.setSelection(50);
			animateOnLayoutButton.setSelection(
				LayoutConstants.DEFAULT_ANIMATION_ON_LAYOUT);
			defaultButton.setSelection(true);
			proofButton.setSelection(false);
			draftButton.setSelection(false);
			incremental.setSelection(LayoutConstants.DEFAULT_INCREMENTAL);
			createBendsAsButton.setSelection(
				LayoutConstants.DEFAULT_CREATE_BENDS_AS_NEEDED);
			uniformLeafNodeSizesButton.setSelection(
				LayoutConstants.DEFAULT_UNIFORM_LEAF_NODE_SIZES);
		}
		else if (select == 1)
		{
			//CoSE
			desiredEdgeLengthCoSE.setText(
				String.valueOf(CoSEConstants.DEFAULT_EDGE_LENGTH));
			smartEdgeLengthCalc.setSelection(
				CoSEConstants.DEFAULT_USE_SMART_IDEAL_EDGE_LENGTH_CALCULATION);
			multiLevelScaling.setSelection(
				CoSEConstants.DEFAULT_USE_MULTI_LEVEL_SCALING);
			smartRepulsionRangeCalc.setSelection(
				FDLayoutConstants.DEFAULT_USE_SMART_REPULSION_RANGE_CALCULATION);
			gravityStrength.setSelection(50);
			gravityRange.setSelection(50);
			compoundGravityStrength.setSelection(50);
			compoundGravityRange.setSelection(50);
			repulsionStrength.setSelection(50);
			springStrength.setSelection(50);
		}
		else if (select == 2)
		{
			//Cluster
			
			desiredEdgeLengthCluster.setText(
				String.valueOf(CoSEConstants.DEFAULT_EDGE_LENGTH));
			clusterSeparation.setSelection(50);
			clusterGravityStrength.setSelection(50);
		}
		else if (select == 3)
		{
			//CiSE
			nodeSeparationCiSE.setText(
					String.valueOf(CiSEConstants.DEFAULT_NODE_SEPARATION));
			desiredEdgeLengthCiSE.setText(
					String.valueOf(CiSEConstants.DEFAULT_EDGE_LENGTH));
			interClusterEdgeLengthFactorCiSE.setSelection(50);
			maxRatioOfNodesInsideCircle.setSelection(
					(int) (CiSEConstants.DEFAULT_MAX_RATIO_OF_NODES_INSIDE_CIRCLE 
							* 100));
			allowNodesInsideCircles.setSelection(
					CiSEConstants.DEFAULT_ALLOW_NODES_INSIDE_CIRCLE);
		}
		else if (select == 4)
		{
			//AVSDF
			nodeSeparation.setText(
				String.valueOf(AVSDFConstants.DEFAULT_NODE_SEPARATION));
		}
		else if (select == 5)
		{
			//Spring		
			nodeDistanceRestLength.setText(String.valueOf((int)
				SpringConstants.DEFAULT_NODE_DISTANCE_REST_LENGTH_CONSTANT));
			disconnectedNodeDistanceSpringRestLength.setText(
				String.valueOf((int) SpringConstants.
					DEFAULT_DISCONNECTED_NODE_DISTANCE_SPRING_REST_LENGTH));
		}
		else if (select == 6)
		 {
			//SGYM
			horizantalSpacing.setText(
				String.valueOf(SgymConstants.DEFAULT_HORIZONTAL_SPACING));
			verticalSpacing.setText(
				String.valueOf(SgymConstants.DEFAULT_VERTICAL_SPACING));
			topToBottom.setSelection(SgymConstants.DEFAULT_VERTICAL);
			leftToRight.setSelection(!topToBottom.getSelection());
		}
	}
}