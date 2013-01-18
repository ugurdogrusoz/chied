package org.gvt;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.gvt.action.*;

/**
 * This class implements the top menu bar.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class TopMenuBar
{
	static ChisioMain chisio;

	/**
	 * Creates the bar menu for the main window
	 */
	public static MenuManager createBarMenu(ChisioMain main)
	{
		chisio = main;

		MenuManager menuBar = new MenuManager("");
		MenuManager fileMenu = new MenuManager("&File");
		MenuManager editMenu = new MenuManager("&Edit");
		MenuManager viewMenu = new MenuManager("&View");
		MenuManager clusterMenu = new MenuManager("&Cluster");
		MenuManager layoutMenu = new MenuManager("&Layout");
		MenuManager helpMenu = new MenuManager("&Help");

		// FILE
		menuBar.add(fileMenu);
		fileMenu.add(new NewAction("New", chisio));
		fileMenu.add(new LoadAction(chisio));
		fileMenu.add(new CreateRandomGraphAction(chisio));
		fileMenu.add(new Separator());
		fileMenu.add(new SaveAction(chisio));
		fileMenu.add(new SaveAsImageAction(chisio, true));
		fileMenu.add(new SaveAsImageAction(chisio, false));
		fileMenu.add(new Separator());
		fileMenu.add(new PrintAction(chisio));
		fileMenu.add(new Separator());
		fileMenu.add(new ExitAction("Exit", chisio));

		// EDIT
		menuBar.add(editMenu);

		MenuManager selectNodesMenu = new MenuManager("&Select Nodes");
		selectNodesMenu.add(
			new SelectNodesAction(chisio, SelectNodesAction.ALL_NODES));
		selectNodesMenu.add(
			new SelectNodesAction(chisio, SelectNodesAction.SIMPLE_NODES));
		selectNodesMenu.add(
			new SelectNodesAction(chisio, SelectNodesAction.COMPOUND_NODES));

		MenuManager selectEdgesMenu = new MenuManager("&Select Edges");
		selectEdgesMenu.add(
			new SelectEdgesAction(chisio, SelectEdgesAction.ALL_EDGES));
		selectEdgesMenu.add(
			new SelectEdgesAction(chisio, SelectEdgesAction.INTRA_GRAPH_EDGES));
		selectEdgesMenu.add(
			new SelectEdgesAction(chisio, SelectEdgesAction.INTER_GRAPH_EDGES));

		editMenu.add(new SelectionToolAction("Select Tool",
			chisio.getEditDomain()));
		editMenu.add(chisio.getChangeMode());
		editMenu.add(new MarqueeZoomToolAction("Marquee Zoom Tool",
			chisio.getEditDomain()));
		editMenu.add(new Separator());
		editMenu.add(selectNodesMenu);
		editMenu.add(selectEdgesMenu);
		editMenu.add(new Separator());
		editMenu.add(new NodeCreationToolAction("Create Simple Node",
			chisio.getEditDomain()));
		editMenu.add(new CompoundCreationToolAction("Create Compound Node",
			chisio.getEditDomain()));
		editMenu.add(new CreateCompoundFromSelectedAction(chisio));
		editMenu.add(new ConnectionCreationAction("Create Edge",
			chisio.getEditDomain()));
		editMenu.add(new Separator());
		editMenu.add(new RemoveCompoundAction(chisio));
		editMenu.add(new DeleteAction(chisio));
		editMenu.add(new Separator());
		editMenu.add(new InspectorAction(chisio, false));

		// VIEW
		menuBar.add(viewMenu);

		MenuManager zoomMenu = new MenuManager("&Zoom");
		zoomMenu.add(new ZoomAction(chisio, -1, null));
		zoomMenu.add(new ZoomAction(chisio, 1, null));
		zoomMenu.add(new Separator());
		zoomMenu.add(new ZoomAction(chisio, 50, null));
		zoomMenu.add(new ZoomAction(chisio, 100, null));
		zoomMenu.add(new ZoomAction(chisio, 200, null));
		zoomMenu.add(new ZoomAction(chisio, 500, null));
		zoomMenu.add(new ZoomAction(chisio, 1000, null));
		viewMenu.add(new ZoomAction(chisio, 0, null));
		viewMenu.add(new Separator());
		viewMenu.add(zoomMenu);
		viewMenu.add(new Separator());
		viewMenu.add(new HighlightSelectedAction(chisio));
		viewMenu.add(new RemoveHighlightFromSelectedAction(chisio));
		viewMenu.add(new RemoveHighlightsAction(chisio));

		// CLUSTER
		menuBar.add(clusterMenu);
		clusterMenu.add(new AssignNewClusterIDAction(chisio));
		clusterMenu.add(new ClusterGraphAction(chisio));
		clusterMenu.add(new ResetClusterIDAction(chisio));
		clusterMenu.add(new Separator());
		clusterMenu.add(new ShowClusterBoundsAction(chisio));
		clusterMenu.add(new ColorWithClusterIDAction(chisio));

		// LAYOUT
		menuBar.add(layoutMenu);
		layoutMenu.add(new CoSELayoutAction(chisio));
		layoutMenu.add(new ClusterLayoutAction(chisio));
		layoutMenu.add(new CiSELayoutAction(chisio));
		layoutMenu.add(new AVSDFLayoutAction(chisio));
		layoutMenu.add(new SpringLayoutAction(chisio));
		layoutMenu.add(new SugiyamaLayoutAction(chisio));
		layoutMenu.add(new Separator());
		layoutMenu.add(new StopLayoutAction());
		layoutMenu.add(new Separator());
		layoutMenu.add(new LayoutInspectorAction(chisio));

		// HELP
		menuBar.add(helpMenu);
		helpMenu.add(new HowToUseAction(chisio));
		helpMenu.add(new HowToCustomizeAction(chisio));
		helpMenu.add(new AboutAction(chisio));

		return menuBar;
	}
}