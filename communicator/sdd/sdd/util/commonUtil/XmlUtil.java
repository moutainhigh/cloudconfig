package com.kuangchi.sdd.util.commonUtil;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XmlUtil {

	public static Document getDocument(File file) throws Exception {
		SAXReader reader = new SAXReader();

		Document doc = null;
		try {
			doc = reader.read(file);
		} catch (DocumentException e) {

			e.printStackTrace();

			throw new Exception(e);
		}
		return doc;
	}

	/**
	 * 选取多个节点
	 * @param rootNode
	 * @param xpathExpression
	 * @return
	 */
	public static List<Node> selectNodes(Node rootNode, String xpathExpression) {

		return rootNode.selectNodes(xpathExpression);

	}
	
	/**
	 * 选取单个节点
	 * @param rootNode
	 * @param xpathExpression
	 * @return
	 */
	public static Node selectNode(Node rootNode, String xpathExpression) {
		return rootNode.selectSingleNode(xpathExpression);
	}

	/**
	 * 节点值
	 * @param node
	 * @return
	 */
	public static String nodeValue(Node node) {
		return node.getStringValue();
	}

	public static void main(String[] args) throws Exception {
		File file = new File("F://001-100105.eep");

		Document doc = XmlUtil.getDocument(file);

		Node node = XmlUtil.selectNode(doc, "//电子文件封装包/被签名对象");

		Node test = XmlUtil.selectNode(node, "封装包创建单位");

		System.out.println(XmlUtil.nodeValue(test));
		
	
		
		

	}

}
