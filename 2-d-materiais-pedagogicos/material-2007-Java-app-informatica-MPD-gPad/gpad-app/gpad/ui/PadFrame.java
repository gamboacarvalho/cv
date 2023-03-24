package gpad.ui;

import gpad.model.CompositeShape;
import gpad.model.IShape;
import gpad.props.PropertySheet;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This frame shows the toolbar and the graph.
 * @uml.dependency   supplier="gpad.PadToolBar" stereotypes="Standard::Create"
 */
public class PadFrame extends JFrame{
	/**
      Constructs a graph frame that displays a given graph.
      @param graph the graph to display
	 */
	public PadFrame(IShape [] prototypes){
		// Init fields and properties
		_prototypes = prototypes;
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Init Toolbar
		_toolBar = new PadToolBar(prototypes, new CompositeShape());
		this.add(_toolBar, BorderLayout.NORTH);
		
		// Init main menu and components
		constructFrameComponents();
		constructFrameMenus();      
	}

	/**
	 * Constructs the tool bar and graph panel.
	 */
	private void constructFrameComponents(){
		_panel = new PadDraw(_toolBar);
		_scrollPane = new JScrollPane(_panel);
		this.add(_scrollPane, BorderLayout.CENTER);
	}
	/**
	 * Constricts the menubar, menu and menuItens.
	 */
	private void constructFrameMenus(){
		// Set up menus
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		JMenuItem deleteItem = new JMenuItem("Delete");
		JMenuItem propertiesItem = new JMenuItem("Properties");
		JMenuItem undoItem = new JMenuItem("Undo");
		JMenu editMenu = new JMenu("Edit");
		editMenu.add(deleteItem);
		editMenu.add(propertiesItem);
		editMenu.add(undoItem);
		menuBar.add(editMenu);
		undoItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				_panel.undo();
			}
			
		});
		openItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				openFile();
				repaint();
			}
		});
		saveItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				saveFile();
			}
		});
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				dispose();
			}
		});
		deleteItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				_toolBar.getSelectedTool().removeSelected();
				repaint();
			}
		});
		propertiesItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				IShape selected = _toolBar.getSelectedTool().getSelected();
				PropertySheet sheet = new PropertySheet(selected);
				sheet.addChangeListener(new ChangeListener(){
					public void stateChanged(ChangeEvent event){
						repaint();
					}
				});
				JOptionPane.showMessageDialog(null, sheet, "Properties", JOptionPane.QUESTION_MESSAGE);        
			}
		});
	}
	/**
      Asks the user to open a graph file.
	 */
	private void openFile()
	{
		// let user select file

		JFileChooser fileChooser = new JFileChooser();
		int r = fileChooser.showOpenDialog(this);
		if (r == JFileChooser.APPROVE_OPTION)
		{
			// Open the file that the user selected
			try
			{
				File file = fileChooser.getSelectedFile();
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(file));
				_prototypes = (IShape [])in.readObject();
				CompositeShape shape = (CompositeShape) in.readObject();
				_toolBar.initialize(_prototypes, shape);
				in.close();
				validate();
				repaint();
			}
			catch (IOException exception)
			{
				JOptionPane.showMessageDialog(null,
						exception);
			}
			catch (ClassNotFoundException exception)
			{
				JOptionPane.showMessageDialog(null,
						exception);
			}
		}
	}

	/**
      Saves the current graph in a file.
	 */
	private void saveFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(this)
				== JFileChooser.APPROVE_OPTION)
		{
			try
			{
				File file = fileChooser.getSelectedFile();
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(file));
				out.writeObject(_prototypes);
				out.writeObject(_toolBar.getSelectedTool().getShape());
				out.close();
			}
			catch (IOException exception)
			{
				JOptionPane.showMessageDialog(null,
						exception);
			}
		}
	}
	/**
	 * @uml.property   name=""
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared"
	 */
	private PadDraw _panel;
	private JScrollPane _scrollPane;
	private PadToolBar _toolBar;
	private IShape [] _prototypes; 

	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 400;
}
