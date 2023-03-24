


import image.PGImage;
import imageProcessing.FilterCollection;
import imageProcessing.ImageFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.*;


public class ImageProcessingApp extends JFrame implements ActionListener {
	private File currentDir;
	private File currentFile;
	private JFileChooser chooser;
	private JButton open, save, reload;
	File fChoosen;
	private ImagePanel imgPanel;
	private ImageFilter[] filters;

	// This class is used as listener of filter button events 
	class ButtonFilter implements ActionListener {
		private ImageFilter filter;
		private JButton button;
		
		public ButtonFilter(ImageFilter f, JButton b) {
			filter = f;
			button = b;
		}
		public void actionPerformed(ActionEvent e) {
			PGImage img = imgPanel.getImage();
			if (img != null) {
				img = filter.execute(img);
				imgPanel.setImage(img);
			}
			else 
				JOptionPane.showMessageDialog(null, "You have to load an image first!");
		}
	}
		
	
	// Constructor
	public ImageProcessingApp() {
		super("Image processing application");

		chooser = new JFileChooser();
		try {
			currentDir = (new File(".")).getCanonicalFile();
		} catch (IOException ex) {}
		
		// Build content pane
        makeContentPane();
        // Frame configuration
        pack();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation( (d.width-getSize().width)/2, (d.height-getSize().height)/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void makeContentPane() {
		// Get content pane
		Container cnt = this.getContentPane();
		// Create toolbar
		add(createToolbar(), BorderLayout.NORTH);
		// Create image panel
		imgPanel = new ImagePanel();
		JScrollPane sp = new JScrollPane(imgPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cnt.add(sp, BorderLayout.CENTER);
		
		// Create filter buttons panel
		JPanel filtersPanel = new JPanel();
		cnt.add(filtersPanel, BorderLayout.SOUTH);
		filtersPanel.setBorder(new TitledBorder("Filters"));
		
		// Get registered filters
		filters = FilterCollection.getRegisteredFilters();
		// Create array of mappings that associate button to filter
		ButtonFilter[] buttonFilters = new ButtonFilter[filters.length];
		
		for (int i = 0; i < filters.length; ++i) {
			ImageFilter filter = filters[i];
			JButton button = new JButton(filter.getName());
			filtersPanel.add(button);
			buttonFilters[i] = new ButtonFilter(filter, button);
			// Add action listener
			button.addActionListener(buttonFilters[i]);
		}
	}	
	
	private JToolBar createToolbar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		// Create "Open File" icon
		open = new JButton(new ImageIcon("resources/openFile.gif"));
		open.setToolTipText("Open image file");
		open.addActionListener(this);
		toolBar.add(open);
		
		// Create "Save File" icon
		save = new JButton(new ImageIcon("resources/saveFile.gif"));
		save.setToolTipText("Save current image file");
		save.addActionListener(this);
		toolBar.add(save);
		
		// Create "Reload File" icon
		reload = new JButton("Reload image");
		reload.setFont(new Font("Arial", Font.PLAIN, 19));
		reload.setToolTipText("Reload original image file");
		reload.addActionListener(this);
		toolBar.add(reload);
		
		return toolBar;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == open) {
			chooser.setCurrentDirectory(currentDir);	
			chooser.rescanCurrentDirectory();
			int result = chooser.showOpenDialog(this);
			repaint();
			if (result != JFileChooser.APPROVE_OPTION)
				return;
			currentDir = chooser.getCurrentDirectory();
			fChoosen = chooser.getSelectedFile();
			openFile(fChoosen);
		}
		else if (e.getSource() == save) {
			if (currentFile == null || imgPanel.getImage() == null)
				return;
			chooser.setCurrentDirectory(currentDir);
			chooser.rescanCurrentDirectory();
			int result = chooser.showSaveDialog(this);
			repaint();
			if (result != JFileChooser.APPROVE_OPTION)
				return;
			currentDir = chooser.getCurrentDirectory();
			File fChoosen = chooser.getSelectedFile();
			if (fChoosen != null && fChoosen.exists()) {
				String message = "File " + fChoosen.getName() +
					" already exists. Override?";
				int result2 = JOptionPane.showConfirmDialog(
									this, message, getTitle(),
									JOptionPane.YES_NO_OPTION);
				if (result2 != JOptionPane.YES_OPTION)
					return;
			}
			saveFile(fChoosen);
			setCurrentFile(fChoosen);
		}
		else if (e.getSource() == reload) {
			openFile(fChoosen);
		}
	}
	private void setCurrentFile(File file) {
		if (file != null) {
			currentFile = file;
			setTitle("Image Processing [" + file.getName() + "]");
		}
	}
	private void openFile(File file) {
		if (file == null || !file.exists()) {
			JOptionPane.showMessageDialog(null, "You have to load an image first!");
			return;
		}
		PGImage img = new PGImage(file.toString()); 
		imgPanel.setImage(img);
		setCurrentFile(file);
	}

	private void saveFile(final File file) {
		if (file == null || imgPanel.getImage() == null)
			return;
		imgPanel.getImage().save(file.toString());
	}

	
	//
	// Main method
	//
	public static void main(String[] args) {
		new ImageProcessingApp();
	}
}




