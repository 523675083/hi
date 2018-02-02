package com.zz.controller.h5.pay;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class SimpleXMLParseUtil {
    //private static Logger logger = LoggerFactory.getLogger(SimpleXMLParseUtil.class);

    public static Map<String, String> doXMLParse(String strxml) {
        if ((null == strxml) || ("".equals(strxml))) {
            return null;
        }

        Map<String, String> m = new HashMap<String, String>();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List<Element> list = root.getChildren();
            Iterator<Element> it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String k = e.getName();
                String v = "";
                List<Element> children = e.getChildren();
                if (children.isEmpty())
                    v = e.getTextNormalize();
                else {
                    v = getChildrenText(children);
                }

                m.put(k, v);
            }
        } catch (IOException e1) {
            //logger.error("IOException", e1);
        } catch (JDOMException e2) {
            //logger.error("JDOMException", e2);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                //logger.error("IOException", e);
            }
        }

        return m;
    }

    public static String getChildrenText(List<?> children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator<?> it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List<Element> list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    public static Map<String, Object> doMultiXMLParse(String strxml) {
        if ((null == strxml) || ("".equals(strxml))) {
            return null;
        }

        Map<String, Object> m = new HashMap<String, Object>();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List<Element> list = root.getChildren();
            Iterator<Element> it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String k = e.getName();
                String v = "";
                Map<String, Object> vObj = new HashMap<String, Object>();
                List<Element> children = e.getChildren();
                String nameTemp = e.getAttributeValue("name");
                if (nameTemp != null && !nameTemp.isEmpty()){
                    k = nameTemp;
                }
                if (children.isEmpty()) {
                    v = e.getTextNormalize();
                    m.put(k, v);
                } else {
                    vObj = getChildrenMap(children);
                    m.put(k, vObj);
                }
            }
        } catch (IOException e1) {
            //logger.error("IOException", e1);
        } catch (JDOMException e2) {
            //logger.error("JDOMException", e2);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
               // logger.error("IOException", e);
            }
        }

        return m;
    }

    /**
     * 获取多层结构的xml
     *
     * @param children
     * @return
     */
    public static Map<String, Object> getChildrenMap(List<?> children) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator<?> it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String nameTemp = e.getAttributeValue("name");
                if (nameTemp != null && !nameTemp.isEmpty()){
                    name = nameTemp;
                }
                String value = e.getTextNormalize();
                Map<String, Object> childrenMap = new HashMap<String, Object>();
                List<Element> list = e.getChildren();
                if (list.isEmpty()) {
                    map.put(name, value);
                } else {
                    childrenMap = getChildrenMap(list);
                    map.put(name, childrenMap);
                }
            }
        }
        return map;
    }

}