package mpdUnitTest.viewers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import mpdUnitTest.EvaluationResult;
import mpdUnitTest.ITestResultViewer;


public final class GuiJTableTestResultViewer extends JFrame implements ITestResultViewer {
	
	public static final Color FAIL_COLOR = new Color(255, 190, 190);
	public static final Color SUCCESS_COLOR = new Color(190, 255, 190);
	
	public class SuccessRenderer implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean arg2, boolean arg3, int row, int column) {
			
			Boolean success = table.getValueAt(row, COLUMN_NAMES.length-1).toString().charAt(0) == 'O'; 
			
			String contentStr = value==null? "" : value.toString();
			Color color = success == null? Color.WHITE : success? SUCCESS_COLOR : FAIL_COLOR;
			
			JLabel label = new JLabel(contentStr);
			JPanel content = new JPanel();
			content.setBackground(color);
			content.setLayout(new BorderLayout());
			if (success != null)
				content.setToolTipText(model.getValueAt(row, COLUMN_NAMES.length - 1).toString());
			content.add(label);
			return content;			
		}
	}
	
	private class UneditableDefaultTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 2517321076975099881L;

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	private static final long serialVersionUID = 8254842475382274584L;
	
	private static final String[] COLUMN_NAMES = { "Class", "Method", "Evaluation", "Info" };
		
	private UneditableDefaultTableModel model;
	private JTable table;

	public GuiJTableTestResultViewer(){
		super("GuiJTableTestResultViewer");
		
		// Create a table model
		this.model = new UneditableDefaultTableModel();
		this.model.setColumnIdentifiers(COLUMN_NAMES);
		
		// Create a table with the table model
		this.table = new JTable(this.model);
		
		this.table.setDefaultRenderer(Object.class, new SuccessRenderer());
		this.table.setAutoCreateRowSorter(true);
		
		// Use a scroll panel
		JScrollPane scrollPane = new JScrollPane(this.table);
		// table.setFillsViewportHeight(true);
		
		// Add the table
		this.setLayout(new BorderLayout());
		this.getContentPane().add(scrollPane);
		
		// Show the window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void startedTestSession() { }
	@Override
	public void startedClassTest(String className, String description) { }
	@Override
	public void startedMethodTest(String methodName) { }

	@Override
	public void methodEvaluation(EvaluationResult evaluationResult) {
		Vector<String> newRow = new Vector<String>();

		newRow.add(evaluationResult.ClassName);							// Class
		newRow.add(evaluationResult.MethodName);						// Method
		newRow.add(evaluationResult.AnnotationName);					// Evaluation
		newRow.add(evaluationResult.Message);							// Info
		
		this.model.addRow(newRow);
	}

	@Override
	public void endedMethodTest(boolean methodSuccess) { }
	@Override
	public void endedClassTest(boolean classSuccess) { }
	@Override
	public void endedTestSession(boolean testSessionSuccess) { }
}
