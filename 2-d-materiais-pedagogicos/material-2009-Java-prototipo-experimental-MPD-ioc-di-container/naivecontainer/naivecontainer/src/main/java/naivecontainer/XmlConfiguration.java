package naivecontainer;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import naivecontainer.exceptions.NaiveContainerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlConfiguration implements InjectorConfiguration {

	private final Document _doc;
	
	public XmlConfiguration(String uri) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		_doc = builder.parse(uri);
	}
	
	public XmlConfiguration(InputStream is) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		_doc = builder.parse(is);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processElement(Element elem, Injector injector) throws NaiveContainerXmlConfigurationException{
		
		String bind = null;
		String named = null;
		String to = null;
		String toInstance = null;
		
		NamedNodeMap attrs = elem.getAttributes();
		for(int j = 0 ; j<attrs.getLength() ; ++j){
			String name = attrs.item(j).getNodeName();
			String value = attrs.item(j).getNodeValue();
			if(name.equals("bind")){
				if(bind != null) throw new NaiveContainerXmlConfigurationException("multiple definitions for 'to'");
				bind = value;				
			}else if(name.equals("named")){
				if(named != null) throw new NaiveContainerXmlConfigurationException("multiple definitions for 'named'");
				named = value;
			}else if(name.equals("to")){
				if(to != null) throw new NaiveContainerXmlConfigurationException("multiple definitions for 'named'");
				to = value;
			}else if(name.equals("toInstance")){
				if(to != null) throw new NaiveContainerXmlConfigurationException("multiple definitions for 'named'");
				toInstance = value;
			}else{
				throw new NaiveContainerXmlConfigurationException("unknown attribute");
			}
		}
		
		if(bind == null) throw new NaiveContainerXmlConfigurationException("'bind' attribute is missing");
		if(to == null && toInstance == null) throw new NaiveContainerXmlConfigurationException("'to' or 'toInstance' attributes are missing");
		if(to != null && toInstance != null) throw new NaiveContainerXmlConfigurationException("Only one of 'to' or 'toInstance' may be defined");
		Class<?> fromClass = null;
		try {
			fromClass = Class.forName(bind);
		} catch (ClassNotFoundException e) {
			throw new NaiveContainerXmlConfigurationException(String.format("Class %s was not found",bind));
		}
		Dependency dep = null;
		if(named == null){
			dep = new Dependency(fromClass);
		}else{
			dep = new NamedDependency(named, fromClass);
		}
		if(to != null){
			Class<?> toClass = null;
			try {
				toClass = Class.forName(to);
			} catch (ClassNotFoundException e) {
				throw new NaiveContainerXmlConfigurationException(String.format("Class %s was not found",to));
			}
			injector.addBinding(new TypeBinding(dep, toClass));
		}else{
			if(!fromClass.equals(String.class) && !fromClass.equals(Integer.class)){
				throw new NaiveContainerXmlConfigurationException("binding to instances is allowed only on strings and integers");
			}
			if(fromClass.equals(String.class)){
				injector.addBinding(new InstanceBinding(dep, toInstance));
			}else if(fromClass.equals(Integer.class)){
				Integer i = Integer.valueOf(toInstance);
				injector.addBinding(new InstanceBinding(dep, i));
			}else{
				throw new NaiveContainerXmlConfigurationException(String.format("Instances of type %s are not supported",fromClass.getName()));
			}
		}
	}
	
	@Override
	public void configure(Injector injector) throws NaiveContainerException {
		Element root = _doc.getDocumentElement();
		String rootName = root.getNodeName();
		if(!rootName.equals("bindings")) throw new NaiveContainerXmlConfigurationException("root element must be '<bindings>'");
		NodeList childs = root.getChildNodes();
		for(int i = 0 ; i<childs.getLength() ; ++i){
			Node node = childs.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element elem = (Element) node;
			String name = elem.getNodeName();
			if(!name.equals("binding")) throw new NaiveContainerXmlConfigurationException("unknown element");
			processElement(elem, injector);		
		}
	}
}
