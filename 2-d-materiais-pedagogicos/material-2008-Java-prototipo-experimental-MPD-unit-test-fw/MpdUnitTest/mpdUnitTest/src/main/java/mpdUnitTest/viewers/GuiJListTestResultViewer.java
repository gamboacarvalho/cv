package mpdUnitTest.viewers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import mpdUnitTest.EvaluationResult;
import mpdUnitTest.ITestResultViewer;

/**
 * JFrame that uses a JList to show the results of a test session
 * by implementing <code>ITestResultViewer</code>.
 * A custom cell renderer is used to highlight each row
 * according to it's success.
 * 
 * @see mpdUnitTest.ITestResultViewer
 * @see javax.swing.JFrame
 * @see javax.swing.JList
 */
public final class GuiJListTestResultViewer  extends JFrame  implements ITestResultViewer{

	public static final Color FAIL_COLOR = new Color(255, 190, 190);
	public static final Color SUCCESS_COLOR = new Color(190, 255, 190);
	
	/**
	 * Custom <code>ListCellRenderer</code> that highlights 
	 * each row according to its success value.
	 */
	public class SuccessRenderer implements ListCellRenderer {
		
		@Override
		public Component getListCellRendererComponent(JList arg0, Object value,
				int ind, boolean arg3, boolean arg4) {
			
			Boolean success = successes.get(ind); 
			
			String contentStr = value==null? "" : value.toString();
			Color color = success == null? Color.WHITE : success? SUCCESS_COLOR : FAIL_COLOR;
			
			JLabel label = new JLabel(contentStr);
			JPanel content = new JPanel();
			content.setBackground(color);
			content.setLayout(new BorderLayout());
			if (success != null)
				content.setToolTipText(success? "OK" : "Failed");
			content.add(label, BorderLayout.WEST);
			return content;	
		}
	}
	
	private final class GuiJListTestResultViewerListModel extends AbstractListModel {
		private static final long serialVersionUID = 1932018951222178126L;
		@Override
		public Object getElementAt(int index) { return elements.get(index); }
		@Override
		public int getSize() { return elements.size(); }
		
		/**
		 * Notifies all of the ListModel's listeners that the contents have changed.
		 */
		public void nofityListeners() { this.fireContentsChanged(this, 0, elements.size()); pack(); }
	};		
	
	private static final long serialVersionUID = -8855085422016687351L;
	private static final Object IDENTATION = "           ";
	private static final String SEPARATOR = " - ";
	private JList list;
	private GuiJListTestResultViewerListModel model;
	private List<StringBuilder> elements;
	private int currentClassInd;
	private int currentMethodInd;
	
	private List<Boolean> successes;
	
	public GuiJListTestResultViewer(){
		super("GuiJListTestResultViewer");

		// Create a list for StringBuilders
		this.elements = new LinkedList<StringBuilder>();
		
		this.successes = new LinkedList<Boolean>();
		
		// Create a list model
		this.model = new GuiJListTestResultViewerListModel();
		
		// Create a list with the list model
		this.list = new JList(this.model);
		
		this.list.setCellRenderer(new SuccessRenderer());
		
		// Use a scroll pane
		JScrollPane scrollPane = new JScrollPane(this.list);
		
		// Add the list
		this.setLayout(new BorderLayout());
		this.getContentPane().add(scrollPane);
		
		// Show the window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void startedTestSession() {
		StringBuilder testSessionInfo = new StringBuilder();
		testSessionInfo.append("Test Session");
		this.elements.add(testSessionInfo);
		this.successes.add(null);
		// this.model.nofityListeners();
	}

	@Override
	public void startedClassTest(String className, String description) {
		StringBuilder classTestInfo = new StringBuilder();
		this.ident(classTestInfo, 1);
		classTestInfo.append(className);
		classTestInfo.append(SEPARATOR);
		classTestInfo.append(description);
		
		this.elements.add(classTestInfo);
		this.currentClassInd = this.elements.size() - 1;
		this.successes.add(null);
		// this.model.nofityListeners();
	}

	@Override
	public void startedMethodTest(String methodName) {
		StringBuilder methodTestInfo = new StringBuilder();
		this.ident(methodTestInfo, 2);
		methodTestInfo.append(methodName);
		
		this.elements.add(methodTestInfo);
		this.currentMethodInd = this.elements.size() - 1;
		this.successes.add(null);
		// this.model.nofityListeners();
	}

	@Override
	public void methodEvaluation(EvaluationResult evaluationResult) {
		StringBuilder evaluationResultInfo = new StringBuilder();
		this.ident(evaluationResultInfo, 3);
		evaluationResultInfo.append(evaluationResult.AnnotationName);
		evaluationResultInfo.append(SEPARATOR);
		evaluationResultInfo.append(evaluationResult.Message);
		
		this.elements.add(evaluationResultInfo);
		this.successes.add(evaluationResult.Success);
		// this.model.nofityListeners();
	}

	@Override
	public void endedMethodTest(boolean methodSuccess) {
		StringBuilder testClassInfo = this.elements.get(this.currentMethodInd);
		testClassInfo.append(SEPARATOR);
		testClassInfo.append(this.getSuccessString(methodSuccess));
		this.successes.set(this.currentMethodInd, methodSuccess);
		// this.model.nofityListeners();
	}
	
	@Override
	public void endedClassTest(boolean classSuccess) {
		StringBuilder testClassInfo = this.elements.get(this.currentClassInd);
		testClassInfo.append(SEPARATOR);
		testClassInfo.append(this.getSuccessString(classSuccess));
		this.successes.set(this.currentClassInd, classSuccess);
		// this.model.nofityListeners();
	}

	@Override
	public void endedTestSession(boolean testSessionSuccess) {
		StringBuilder testSessionInfo = this.elements.get(0);
		testSessionInfo.append(SEPARATOR);
		testSessionInfo.append(this.getSuccessString(testSessionSuccess));
		this.successes.set(0, testSessionSuccess);
		this.model.nofityListeners();
	}
	
	/**
	 * Transforms a boolean value in a <code>String</code>
	 * that indicates success.
	 *  
	 * @param success	Indication of success.
	 * @return			A <code>String</code> that indicates the success.
	 */
	private String getSuccessString(boolean success){
		return success? " OK" : " Failed";
	}

	/**
	 * Applies an indentation to the <code>StringBuilder</code> the
	 * specified amount of times.
	 * 
	 * @param evaluationResultInfo		The <code>StringBuilder</code> where to add the indentation.
	 * @param times						The amount of times to add the indentation.
	 */
	private void ident(StringBuilder evaluationResultInfo, int times) {
		for (int i=0; i<times; ++i)
			evaluationResultInfo.insert(0, IDENTATION);
	}
}
