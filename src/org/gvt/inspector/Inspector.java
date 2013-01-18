package org.gvt.inspector;

import java.util.*;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.gvt.model.*;
import org.gvt.action.ChangeMarginAction;
import org.gvt.ChisioMain;
import org.ivis.layout.Cluster;

/**
 * This class maintains the base inspector window. For edges, nodes, compound
 * nodes and graphs, this class is dervied and used with necessary attributes.
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public abstract class Inspector extends Dialog
{
// -----------------------------------------------------------------------------
// Section: Instance variables
// -----------------------------------------------------------------------------
	// Font of Table
	protected Font tableFont;
	// The table which is shown on the inspector window to choose attributes
	protected Table table;
	// This inspector windows owner model. Properties are belong to this object.
	protected GraphObject model;

	protected static List<Inspector> instances = new ArrayList();

	protected Shell shell;

	protected Font newFont;

	protected ChisioMain main;
	
	protected KeyAdapter keyAdapter = new KeyAdapter()
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
	
	/*
	 * This Key Adapter is used for setting cluster IDS, using commas
	 */
	protected KeyAdapter clusterAdapter = new KeyAdapter()
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
				|| keyCode == ',' // additionally comma and
				|| keyCode == ' ' // space are allowed
				|| keyCode == SWT.ARROW_LEFT
				|| keyCode == SWT.ARROW_RIGHT)
			{
				return true;
			}
			return false;
		}
	};

// -----------------------------------------------------------------------------
// Section: Class methods.
// -----------------------------------------------------------------------------
	protected Inspector(GraphObject model, String title, ChisioMain main)
	{
		super(main.getShell(), SWT.NONE);
		
		this.shell = new Shell(main.getShell(), SWT.DIALOG_TRIM);
		this.shell.setText(title + " Properties");
		this.shell.setLayout(null);
		this.main = main;

		this.model = model;
		this.table = createTable(this.shell);
		this.newFont = model.getTextFont();
	}

	/**
	 * This method creates the table and initializes it.
	 */
	public Table createTable(final Shell shell)
	{
		shell.setLayout(null);
		// create table font
		Font headerFont = new Font(null, new FontData("Verdana", 9, SWT.BOLD));
		// Create the table
		final Table table = new Table(shell, SWT.SINGLE | SWT.FULL_SELECTION
			| SWT.HIDE_SELECTION);
		table.setLinesVisible(true);
		table.setFont(headerFont);

		TableColumn attributes = new TableColumn(table, SWT.LEFT);
		attributes.pack();
		final TableColumn values = new TableColumn(table, SWT.LEFT);
		values.pack();

		return table;
	}

	/**
	 * This method creates a TableItem and adds into table.
	 */
	public TableItem addRow(Table table, String label)
	{
		this.tableFont = new Font(null, new FontData("Verdana", 9, SWT.NORMAL));
		// Create the row
		final TableItem item = new TableItem(table, SWT.NONE);
		// It is done to set height of row. There is no way to set height.
		item.setImage(new Image(null, 1, rowHeight));
		item.setFont(this.tableFont);
		table.getColumn(0).setWidth(col0Width);
		table.getColumn(1).setWidth(col1Width);
		// Create the editor and text
		TableEditor itemEditor = new TableEditor(table);
		Label itemText = new Label(table, SWT.PUSH);
		// Set attributes of the text
		item.setText(label);
		itemText.setText(label);
		itemText.setFont(tableFont);
		itemText.setBackground(ColorConstants.white);
		// Set attributes of the editor
		itemEditor.grabHorizontal = true;
		itemEditor.minimumHeight = itemText.getSize().y;
		itemEditor.minimumWidth = itemText.getSize().x;
		// Set the editor for the first column in the row
		itemEditor.setEditor(itemText, item, 0);

		return item;
	}

	/**
	 * This method creates the interactions in second column. Adds a mouse
	 * listener to table and detects the selected row. according to row, a
	 * combo box, or a color dialog or only an editable text field is shown.
	 *
	 * @param shell
	 */
	public void createContents(final Shell shell)
	{
		// Create an editor object to use for text editing
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		// Use a selection listener to get seleceted row
		table.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				// Dispose any existing editor
				Control old = editor.getEditor();

				if (old != null)
				{
					old.dispose();
				}

				// Determine which row was selected
				final TableItem item = (TableItem) event.item;

				if (item != null)
				{
					// COMBO
					if (item.getText().equals("Style")
						|| item.getText().equals("Arrow")
						|| item.getText().equals("Shape"))
					{
						// Create the dropdown and add data to it
						final CCombo combo = new CCombo(table, SWT.READ_ONLY);

						if (item.getText().equals("Style"))
						{
							String[] styleOfEdge = {"Solid", "Dashed"};
							combo.setItems(styleOfEdge);
						}
						else if (item.getText().equals("Arrow"))
						{
							String[] arrowOfEdge = {"None",
								"Source",
								"Target",
								"Both"};
							combo.setItems(arrowOfEdge);
						}
						else if (item.getText().equals("Shape"))
						{
							 combo.setItems(NodeModel.shapes);
						}

						// Select the previously selected item from the cell
						combo.select(combo.indexOf(item.getText(1)));
						combo.setFont(tableFont);
						editor.setEditor(combo, item, 1);

						// Add a listener to set the selected item back into the
						// cell
						combo.addSelectionListener(new SelectionAdapter()
						{
							public void widgetSelected(SelectionEvent event)
							{
								item.setText(1, combo.getText());
                                // They selected an item; end the editing
								// session
								combo.dispose();
							}
						});
					}

					// TEXT
					else if (item.getText().equals("Name"))
					{
						// Create the Text object for our editor
						final Text text = new Text(table, SWT.LEFT);
						// text.setForeground(item.getForeground());

						// Transfer any text from the cell to the Text control,
						// set the color to match this row, select the text,
						// and set focus to the control
						text.setText(item.getText(1));
						text.setFont(tableFont);

						// text.setForeground(item.getForeground());
						text.selectAll();
						text.setFocus();
						editor.setEditor(text, item, 1);

						// Add a handler to transfer the text back to the cell
						// any time it's modified
						text.addModifyListener(new ModifyListener()
						{
							public void modifyText(ModifyEvent event)
							{
								// Set the text of the editor's control back
								// into the cell
								item.setText(1, text.getText());
							}
						});
					}

					// NUMBER
					else if (item.getText().equals("Margin")
						|| item.getText().equals("Cluster ID")
						|| item.getText().equals("Width"))
					{
						// Create the Text object for our editor
						final Text text = new Text(table, SWT.LEFT);
						// text.setForeground(item.getForeground());

						// Transfer any text from the cell to the Text control,
						// set the color to match this row, select the text,
						// and set focus to the control
						text.setText(item.getText(1));
						text.setFont(tableFont);

						// text.setForeground(item.getForeground());
						text.selectAll();
						text.setFocus();
						editor.setEditor(text, item, 1);

						// Add a handler to transfer the text back to the cell
						// any time it's modified
						text.addModifyListener(new ModifyListener()
						{
							public void modifyText(ModifyEvent event)
							{
								// Set the text of the editor's control back
								// into the cell
								item.setText(1, text.getText());
							}
						});
						
						// if cluster id is set, let commas and spaces
						if( item.getText().equals("Cluster ID"))
						{
							text.addKeyListener(clusterAdapter);
						}
						else
						{
							text.addKeyListener(keyAdapter);
						}
					}

					// COLOR
					else if (item.getText().equals("Border Color")
						|| item.getText().equals("Color")
						|| item.getText().equals("Highlight Color"))
					{
						ColorDialog dialog = new ColorDialog(shell);
						dialog.setRGB(item.getBackground(1).getRGB());
						RGB rgb = dialog.open();

						if (rgb != null)
						{
							item.setBackground(1,
								new Color(shell.getDisplay(), rgb));
						}
					}

					// FONT
					else if (item.getText().equals("Text Font"))
					{
						FontDialog dlg = new FontDialog(shell);

						// Pre-fill the dialog with any previous selection
						dlg.setFontList(newFont.getFontData());
						dlg.setRGB(item.getForeground(1).getRGB());

						if (dlg.open() != null)
						{
						  // Create the new font and set it into the label
							newFont =
								new Font(shell.getDisplay(), dlg.getFontList());

							String name = newFont.getFontData()[0].getName();
							int size = newFont.getFontData()[0].getHeight();
							int style = newFont.getFontData()[0].getStyle();

							if (size > 14)
							{
								size = 14;
							}

							item.setText(1, name);
							item.setFont(1,	new Font(null, name, size, style));
							item.setForeground(1,
								new Color(shell.getDisplay(), dlg.getRGB()));
						}
					}

					table.setSelection(-1);
				}
			}
		});

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.addSelectionListener(new OkButtonSelectionAdapter(main));
		
		okButton.setText("OK");
		final Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(final SelectionEvent e)
			{
				shell.close();
			}
		});

		cancelButton.setText("Cancel");
		final Button defaultButton = new Button(shell, SWT.NONE);
		defaultButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(final SelectionEvent e)
			{
				setAsDefault();
			}
		});

		defaultButton.setText("Set As Default");

		if (this instanceof GraphInspector)
		{
			defaultButton.setVisible(false);
		}

		int numOfRow = table.getItemCount();
		int heightOfRow = rowHeight + 1;
		int tableWidth = col0Width + col1Width;
		int defaultButtonWidth = 85;

		if (!ChisioMain.runningOnWindows)
		{
			tableWidth += 20;
			defaultButtonWidth += 20;
			heightOfRow += 4;
		}

		defaultButton.setBounds(10,
			20 + numOfRow * heightOfRow,
			defaultButtonWidth,
			30);
		okButton.setBounds(tableWidth - 112,
			20 + numOfRow * heightOfRow,
			60,
			30);
		cancelButton.setBounds(tableWidth - 50,
			20 + numOfRow * heightOfRow,
			60,
			30);
		table.setBounds(10,	10, tableWidth,	3 + numOfRow * heightOfRow);

		shell.pack();
		shell.setSize(tableWidth + 25, 87 + numOfRow * heightOfRow);
	}

	protected class OkButtonSelectionAdapter extends SelectionAdapter
	{
		private ChisioMain main;
		
		public OkButtonSelectionAdapter(ChisioMain main)
		{
			super();
			this.main = main;
		}
		
		public void widgetSelected(final SelectionEvent e)
		{
			TableItem[] items = table.getItems();

			for (TableItem item : items)
			{
				if (item.getText().equals("Name"))
				{
					model.setText(item.getText(1));
				}
				else if (item.getText().equals("Text Font"))
				{
					model.setTextFont(newFont);
					model.setTextColor(item.getForeground(1));
				}
				else if (item.getText().equals("Color"))
				{
					model.setColor(item.getBackground(1));
				}
				else if (item.getText().equals("Border Color"))
				{
					((NodeModel) model).
						setBorderColor(item.getBackground(1));
				}
				else if (item.getText().equals("Highlight Color"))
				{
					ChisioMain.higlightColor = item.getBackground(1);
				}
				else if (item.getText().equals("Shape"))
				{
					((NodeModel) model).setShape(item.getText(1));
				}
				else if (item.getText().equals("Cluster ID"))
				{
					// parse Cluster string and set the clusterIDs
					String clusterString = item.getText(1);
					
					String[] clusterIDs = clusterString.split("[ ,]");
					((NodeModel) model).resetClusters();
					
					NodeModel nodeModel = (NodeModel) model;
					
					for(int i=0; i<clusterIDs.length; i++)
					{
						if ( !clusterIDs[i].equals("") )
						{
							int clusterID = Integer.parseInt(clusterIDs[i]);
							
							if (clusterID == 0)
							{
								continue;
							}

							// if this cluster is newly created, add polygon to 
							// highlight layer
							if (this.main.isClusterBoundShown
								&& nodeModel.getParentModel().getClusterManager().
									getClusterByID(clusterID) == null) 
							{
								// new cluster is created
								nodeModel.addCluster(clusterID);
															
								// new created cluster is found
								Cluster newCluster = nodeModel.getParentModel().
									getClusterManager().getClusterByID(clusterID);
								
								// polygon for new created cluster is added
								// to highlight layer
								this.main.getHighlightLayer().
									addHighlightToCluster(newCluster);
							}
							else
							{
								nodeModel.addCluster(clusterID);
							}
						}
					}
				}
				else if (item.getText().equals("Style"))
				{
					((EdgeModel) model).setStyle(item.getText(1));
				}
				else if (item.getText().equals("Arrow"))
				{
					((EdgeModel) model).setArrow(item.getText(1));
				}
				else if (item.getText().equals("Width"))
				{
					((EdgeModel) model).
						setWidth(Integer.parseInt(item.getText(1)));
				}
				else if (item.getText().equals("Margin"))
				{
					new ChangeMarginAction((CompoundModel)model,
						Integer.parseInt(item.getText(1))).run();
				}
			}

			shell.close();
		}


	}

	protected static boolean isSingle(GraphObject model)
	{
		if (instances.size() == 0)
		{
			return true;
		}
		else
		{
			Iterator<Inspector> iter = instances.iterator();

			while (iter.hasNext())
			{
				Inspector inspector = iter.next();

				if (inspector.model.equals(model))
				{
					if (inspector.shell.isDisposed() == true)
					{
						instances.remove(inspector);
						return true;
					}

					inspector.shell.forceActive();

					return false;
				}
			}

			return true;
		}
	}

	/**
	 * Default parameters for creation is changed with the current parameters.
	 */
	public abstract void setAsDefault();

	public Point calculateInspectorLocation(int clickLocX, int clickLocY)
	{
		Point loc = shell.getParent().getShell().getLocation();

		Point inspectorLoc = new Point(clickLocX + 10, clickLocY + 80);

		int height = shell.getParent().getSize().y;
		int width = shell.getParent().getSize().x;
		int diffY = inspectorLoc.y + shell.getSize().y - height;
	  	int diffX = inspectorLoc.x + shell.getSize().x - width;

		if (diffY < 0)
		{
			diffY = 0;
		}

		if (diffX < 0)
		{
			diffX = 0;
		}

		return new Point (loc.x + inspectorLoc.x - diffX,
			loc.y + inspectorLoc.y - diffY);
	}

// ---------------------------------------------------------------------------
// Section: Class Variables
// ---------------------------------------------------------------------------
	protected static int col0Width = 105;

	protected static int col1Width = 110;

	protected static int rowHeight = 20;
}