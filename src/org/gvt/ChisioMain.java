package org.gvt;

import java.util.HashMap;

import javax.swing.JOptionPane;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.gvt.action.DeleteAction;
import org.gvt.action.InspectorAction;
import org.gvt.action.LoadAction;
import org.gvt.action.ZoomAction;
import org.gvt.editpart.ChsEditPartFactory;
import org.gvt.editpart.ChsRootEditPart;
import org.gvt.editpart.ChsScalableRootEditPart;
import org.gvt.figure.HighlightLayer;
import org.gvt.model.CompoundModel;

/**
 * This class maintains the main function for this application. Chisio is a
 * compound graph editor with support for various types of layout algorithms.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class ChisioMain extends ApplicationWindow
	implements MouseListener, KeyListener, MouseTrackListener
{
	private ScrollingGraphicalViewer viewer;
	private EditDomain editDomain;
	private PopupManager popupManager;
	private ChsScalableRootEditPart rootEditPart;

	private Action changeMode ;

	public static boolean transferNode = false;

	public static Color higlightColor;
	
	// true if polygon is should be shown for each cluster
	public boolean isClusterBoundShown;

	public static Combo zoomCombo;

	public Combo modeCombo;

	private String currentFilename = null;

	public Point clickLocation;

	public ChisioMain()
	{
		super(null);
		this.editDomain = new EditDomain();
		this.createChangeModeAction();
		this.isClusterBoundShown = false;
	}

	protected void handleShellCloseEvent()
	{
		if (LoadAction.saveChangesBeforeDiscard(this))
		{
			super.handleShellCloseEvent();
			Shell[] inspectors = Display.getDefault().getShells();
			int size = inspectors.length;

			for(int i = 0; i < size ; i++)
			{
				Shell current = inspectors[i];

				if(current.getText().indexOf("Properties") > 0)
				{
					current.close();
				}
			}
		}
	}

	public static void main(String[] args)
	{
		ChisioMain window = new ChisioMain();
		window.setBlockOnOpen(true);
		window.addMenuBar();
		window.addToolBar(SWT.FLAT  | SWT.RIGHT);
		window.open();

		Display.getCurrent().dispose();
	}

	public Dimension getCurrentSize()
	{
		int w = this.getShell().getSize().x;
		int h = this.getShell().getSize().y;
		return new Dimension(w, h);
	}

	protected Control createContents(Composite parent)
	{
		this.getShell().setText("Chisio");
		this.getShell().setSize(800, 600);
		this.getShell().setImage(
			ImageDescriptor.createFromFile(ChisioMain.class,
				"icon/chisio-icon.png").createImage());

		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new FillLayout());

		this.viewer = new ScrollingGraphicalViewer();
		this.viewer.setEditDomain(this.editDomain);
		this.viewer.createControl(composite);
		this.viewer.getControl().setBackground(ColorConstants.white);
		this.rootEditPart= new ChsScalableRootEditPart();
		this.viewer.setRootEditPart(this.rootEditPart);
		this.viewer.setEditPartFactory(new ChsEditPartFactory());

		((FigureCanvas)this.viewer.getControl()).
			setScrollBarVisibility(FigureCanvas.ALWAYS);
		// DropTargetListener
		this.viewer.addDropTargetListener(new ChsFileDropTargetListener(this.viewer, this));
		// DragSourceListener
		this.viewer.addDragSourceListener(new ChsFileDragSourceListener(this.viewer));

		CompoundModel model = new CompoundModel();
		model.setAsRoot();

		this.viewer.setContents(model);
		this.viewer.getControl().addMouseListener(this);

		this.popupManager = new PopupManager(this);
		this.popupManager.setRemoveAllWhenShown(true);
		this.popupManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ChisioMain.this.popupManager.createActions(manager);
			}
		});

		KeyHandler keyHandler = new KeyHandler();
		ActionRegistry a = new ActionRegistry();
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
			new DeleteAction(this.viewer));

		keyHandler.put(KeyStroke.getPressed('+',SWT.KEYPAD_ADD, 0),
			new ZoomAction(this,1,null));

		keyHandler.put(KeyStroke.getPressed('-',SWT.KEYPAD_SUBTRACT, 0),
			new ZoomAction(this,-1,null));

		/*keyHandler.put(KeyStroke.getPressed(SWT.CTRL, 0),
			new ZoomAction(this,-1,null));*/

		keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), a
			.getAction(GEFActionConstants.DIRECT_EDIT));

		this.viewer.setKeyHandler(keyHandler);
		this.higlightColor = ColorConstants.yellow;

		this.createCombos();

		return composite;
	}

	public ScrollingGraphicalViewer getViewer()
	{
		return this.viewer;
	}

	public CompoundModel getRootGraph()
	{
		return (CompoundModel)((ChsRootEditPart) this.getViewer().
				getRootEditPart().getChildren().get(0)).getModel();
	}

	protected MenuManager createMenuManager()
	{
		return TopMenuBar.createBarMenu(this);
	}

	protected ToolBarManager createToolBarManager(int style)
	{
		return new ToolbarManager(style, this);
	}

	public void mouseDoubleClick(MouseEvent e)
	{
		InspectorAction inspectorAction = new InspectorAction(this, false);
		inspectorAction.run();
	}

	public void mouseDown(MouseEvent e)
	{
		this.clickLocation = new Point(e.x,e.y);
		this.popupManager.setClickLocation(this.clickLocation);
		this.viewer.getControl().setMenu(
			this.popupManager.createContextMenu(this.viewer.getControl()));
	}

	public void mouseUp(MouseEvent e)
	{
		this.clickLocation = new Point(e.x,e.y);
	}

	public void keyPressed(KeyEvent e)
	{
		transferNode = !transferNode;
		JOptionPane.showMessageDialog(null, "Transfer Mode: " + transferNode);
	}

	public void keyReleased(KeyEvent e)
	{

	}

	public void mouseEnter(MouseEvent e)
	{
	}

	public void mouseExit(MouseEvent e)
	{
	}

	public void mouseHover(MouseEvent e)
	{
	}

	public EditDomain getEditDomain()
	{
		return this.editDomain;
	}

	public void createChangeModeAction()
	{
		this.changeMode = new Action("Transfer Mode", IAction.AS_CHECK_BOX)
		{
			 public void run()
			{
				transferNode = !transferNode;
				if(transferNode == false)
				{
					ChisioMain.this.modeCombo.select(0);
				}
				else
				{
					ChisioMain.this.modeCombo.select(1);
				}
				this.setChecked(transferNode);
				// after changing combo box, switch to SELECT tool
				ChisioMain.this.editDomain.setActiveTool(ChisioMain.this.editDomain.getDefaultTool());
			}
		};
	}

	public void createCombos()
	{
		ToolBar toolbar =  this.getToolBarManager().getControl();

		ToolItem item = new ToolItem(toolbar, SWT.SEPARATOR,5);
		this.modeCombo = new Combo(toolbar, SWT.READ_ONLY);
		this.modeCombo.add("Move Mode");
		this.modeCombo.add("Transfer Mode");
		this.modeCombo.pack();
		item.setWidth(this.modeCombo.getSize().x);
		item.setControl(this.modeCombo);
		this.modeCombo.select(0);

		this.modeCombo.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				if(ChisioMain.this.modeCombo.getSelectionIndex() == 0)
				{
					transferNode = true;
					ChisioMain.this.changeMode.run();
				}
				else
				{
					transferNode = false;
					ChisioMain.this.changeMode.run();
				}
			}
		});

		item = new ToolItem(toolbar, SWT.SEPARATOR, 9);
		zoomCombo = new Combo(toolbar, SWT.NONE);
		zoomCombo.add("2000%");
		zoomCombo.add("1000%");
		zoomCombo.add("500%");
		zoomCombo.add("150%");
		zoomCombo.add("100%");
		zoomCombo.add("75%");
		zoomCombo.add("50%");
		zoomCombo.add("25%");
		zoomCombo.pack();
		item.setWidth(zoomCombo.getSize().x);
		item.setControl(zoomCombo);

		zoomCombo.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent keyEvent)
			{
				// When ENTER is pressed, zoom to given level
				if(keyEvent.keyCode == 13)
				{
					((ChsScalableRootEditPart)ChisioMain.this.viewer.getRootEditPart()).
						getZoomManager().setZoomAsText(zoomCombo.getText());
				}
			}

			public void keyReleased(KeyEvent keyEvent)
			{
				// nothing
			}
		});

		zoomCombo.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				((ChsScalableRootEditPart)ChisioMain.this.viewer.getRootEditPart()).
					getZoomManager().setZoomAsText(zoomCombo.getText());
			}
		});

		updateCombo(((ChsScalableRootEditPart)this.viewer.getRootEditPart()).
			getZoomManager().getZoomAsText());
	}

	public Action getChangeMode()
	{
		return this.changeMode;
	}

	public static void updateCombo(String newValue)
	{
		zoomCombo.setText(newValue);
		zoomCombo.update();
	}

	public  void setCurrentFilename(String filename)
	{
		this.currentFilename = filename;

		if (filename != null)
		{
			this.getShell().setText(filename + " - Chisio");
		}
		else
		{
			this.getShell().setText("Chisio");
		}
	}

	public String getCurrentFilename()
	{
		if (this.currentFilename == null)
		{
			return "graph.xml";
		}
		else
		{
			return this.currentFilename;
		}
	}

	public HighlightLayer getHighlightLayer()
	{
		HighlightLayer highlight = (HighlightLayer)
			((ChsScalableRootEditPart)this.viewer.getRootEditPart()).
			getLayer(HighlightLayer.HIGHLIGHT_LAYER);

		return highlight;
	}

	public Layer getHandleLayer()
	{
		Layer handle = (Layer) ((ChsScalableRootEditPart)this.viewer.getRootEditPart()).
			getLayer(LayerConstants.HANDLE_LAYER);

		return handle;
	}

	// -------------------------------------------------------------------------
	// Class Constants
	// -------------------------------------------------------------------------
	/**
	 * Used for preventing antialiasing and transparent colors in non-windows
	 * systems. During packaging this variable should be manually set.
	 */
	public static boolean runningOnWindows = true;

	// Following expression can be used for automatic detection of operating
	// system. However we don't use it for safety.

	// runningOnWindows = System.getProperty("os.name").startsWith("Windows");
}