package workFlow;

import java.util.List;
import java.util.Map;

public class Function {

	private String id; //id
	private String name;
	private String urlName;
	
	private Map fieldMap ;//表单映射和值
	private Map nodeList ;//节点列表
	private NLPParser parser;
	
	public Function(String id, String name, Map fieldMap, Map nodeList) {
		super();
		this.id = id;
		this.name = name;
		this.fieldMap = fieldMap;
		this.nodeList = nodeList;
	}
	
	public Function(String id, String name, String urlName, Map fieldMap, Map nodeList) {
		super();
		this.id = id;
		this.name = name;
		this.setUrlName(urlName);
		this.fieldMap = fieldMap;
		this.nodeList = nodeList;
	}

	public NLPParser getParser() {
		return parser;
	}

	public void setParser(NLPParser parser) {
		this.parser = parser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map getNodeList() {
		return nodeList;
	}

	public void setNodeList(Map nodeList) {
		this.nodeList = nodeList;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	
}
