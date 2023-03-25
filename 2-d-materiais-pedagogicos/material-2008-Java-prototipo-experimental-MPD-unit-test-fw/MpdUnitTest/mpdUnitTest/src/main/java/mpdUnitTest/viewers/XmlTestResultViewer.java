package mpdUnitTest.viewers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import mpdUnitTest.EvaluationResult;
import mpdUnitTest.ITestResultViewer;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * An implementation of <code>ITestResultViewer</code> that
 * prints the test results to an output stream in XML format.
 * <br> This viewer uses SAX to process XML. This allows processing
 * of bigger amounts of data without the need for in 
 * memory document representation.
 * 
 * @see mpdUnitTest.ITestResultViewer
 * @see org.xml.sax
 * @see javax.xml.transform.sax
 */
public class XmlTestResultViewer implements ITestResultViewer {

	private static final String DTD_FILE = "testResults.dtd";

	private static final String CDATA = "CDATA";

	private static final String EMPTY = "";
	
	private static final String TEST_SESSION_TAGNAME = "testSession";
	private static final String TEST_CLASS_TAGNAME = "testClass";
	private static final String TEST_MEHTOD_TAGNAME = "testMethod";
	private static final String METHOD_EVALUATION_TAGNAME = "methodEvaluation";

	private static final String FAILED = "Failed";
	private static final String OK = "OK";
	
	private TransformerHandler transformerHandler;
	private PrintWriter out;
	private AttributesImpl atts;

	/**
	 * Creates a new instance of <code>XmlTestResultViewer</code>.
	 * <br>The results are written to the file "testResults.xml".
	 * @throws FileNotFoundException 	If the file "testResults.xml" cannot be created, or cannot be opened for any other reason.
	 */
	public XmlTestResultViewer() throws FileNotFoundException{
		OutputStream outputStream = new FileOutputStream("testResults.xml");
		this.init(outputStream);
	}
	
	/**
	 * Creates a new instance of <code>XmlTestResultViewer</code>.
	 * 
	 * @param outputStream	The output stream in which to write the data.
	 */
	public XmlTestResultViewer(OutputStream outputStream){
		this.init(outputStream);
	}

	/**
	 * Initializes the required SAX tools to write XML to an output stream.
	 * 
	 * @param outputStream								The stream where to write the data.
	 * @throws TransformerFactoryConfigurationError		Thrown if a SAX implementation is not available or cannot be instantiated.
	 * @throws IllegalArgumentException					If the argument is null.
	 */
	private void init(OutputStream outputStream) throws TransformerFactoryConfigurationError, IllegalArgumentException {
		if (outputStream == null)
			throw new IllegalArgumentException("The outputStream arg cannot be null.");

		this.out = new PrintWriter(outputStream);
		
		StreamResult streamResult = new StreamResult(this.out);
		
		SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		
		// SAX2.0 ContentHandler.
		try {
			this.transformerHandler = tf.newTransformerHandler();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}		
		Transformer serializer = this.transformerHandler.getTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
		serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,DTD_FILE);
		serializer.setOutputProperty(OutputKeys.INDENT,"yes");
		this.transformerHandler.setResult(streamResult);
		
		this.atts = new AttributesImpl();
	}
	
	@Override
	public void startedTestSession() {
		String time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());
		this.atts.addAttribute(EMPTY, EMPTY, "time", CDATA, time);
		
		try {
			this.transformerHandler.startDocument();
			this.transformerHandler.startElement(EMPTY, EMPTY, TEST_SESSION_TAGNAME, atts);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startedClassTest(String className, String description) {
		this.atts.clear();
		this.atts.addAttribute(EMPTY, EMPTY, "name", CDATA, className);
		this.atts.addAttribute(EMPTY, EMPTY, "description", CDATA, description);
		try {
			this.transformerHandler.startElement(EMPTY, EMPTY, TEST_CLASS_TAGNAME, atts);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startedMethodTest(String methodName) {
		this.atts.clear();
		this.atts.addAttribute(EMPTY, EMPTY, "name", CDATA, methodName);
		try {
			this.transformerHandler.startElement(EMPTY, EMPTY, TEST_MEHTOD_TAGNAME, atts);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void methodEvaluation(EvaluationResult evaluationResult) {
		this.atts.clear();
		this.atts.addAttribute(EMPTY, EMPTY, "success", CDATA, evaluationResult.Success ? OK : FAILED);
		this.atts.addAttribute(EMPTY, EMPTY, "annotationType", CDATA, evaluationResult.AnnotationName);
		try {
			this.transformerHandler.startElement(EMPTY, EMPTY, METHOD_EVALUATION_TAGNAME, atts);
			this.printCDATA(evaluationResult.Message);
			this.transformerHandler.endElement(EMPTY, EMPTY, METHOD_EVALUATION_TAGNAME);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endedMethodTest(boolean methodSuccess) {
		try {
			this.printSuccess(methodSuccess);
			this.transformerHandler.endElement(EMPTY, EMPTY, TEST_MEHTOD_TAGNAME);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endedClassTest(boolean classSuccess) {
		try {
			this.printSuccess(classSuccess);
			this.transformerHandler.endElement(EMPTY, EMPTY, TEST_CLASS_TAGNAME);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endedTestSession(boolean testSessionSuccess) {
		try {
			this.printSuccess(testSessionSuccess);
			this.transformerHandler.endElement(EMPTY, EMPTY, TEST_SESSION_TAGNAME);
			this.transformerHandler.endDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		this.out.close();
	}
	
	/**
	 * Prints the specified <code>boolean</code> value
	 * to the <code>OutputStream</code> as a representation
	 * of failure or success.
	 * 
	 * @param success			Indication of success.
	 * @throws SAXException		any SAX exception, possibly wrapping another exception
	 */
	private void printSuccess(boolean success) throws SAXException {
		this.printCDATA(success ? OK : FAILED);
	}
	
	/**
	 * Prints the specified data to the <code>OutputSrtream</code>.
	 * 
	 * @param data				The data to print.
	 * @throws SAXException		any SAX exception, possibly wrapping another exception
	 */
	private void printCDATA(String data) throws SAXException{
		char[] dataChars = data.toCharArray();
		this.transformerHandler.characters(dataChars, 0, dataChars.length);
	}
}
