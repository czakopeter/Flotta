package com.sec.billing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLHandler {

  private Document doc;

  private String xmlString;
  
  public XMLHandler(MultipartFile file, List<com.sec.billing.MyNode> templates) throws Exception {
    if (file.isEmpty()) {
      throw new Exception("Empty file");
    }

    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        line = br.readLine();
      }

      xmlString = sb.toString().replaceAll(">\\s+<", "><");
      
      //TODO convert to utf-8
      

      DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));

      doc.getDocumentElement().normalize();
      
//      if (!validXMLStructure(templates)) {
//        throw new Exception("Not a valid XML");
//      }

    } catch (IOException e) {
      
    } catch (SAXException e) {
      
    } catch (ParserConfigurationException e) {
      
    }

  }
  
  public XMLHandler(String xmlString, List<com.sec.billing.MyNode> templates) throws Exception {
    DocumentBuilder dBuilder;
    try {
      dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));
      
      doc.getDocumentElement().normalize();
      
      if (!validXMLStructure(templates)) {
        throw new Exception("Not a valid XML");
      }
    } catch (ParserConfigurationException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
      
  }

  private boolean validXMLStructure(List<com.sec.billing.MyNode> templates) {
    
    for (com.sec.billing.MyNode template : templates) {
      if (doc.getDocumentElement().getNodeName().equalsIgnoreCase(template.getName())) {
        if (equals(doc.getDocumentElement().getChildNodes(), template.getChild())) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean equals(NodeList nodes, List<com.sec.billing.MyNode> template) {
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.TEXT_NODE) {
        System.out.println(i + "\t" + node.getNodeValue().trim());
      }
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        boolean valid = false;
        for (com.sec.billing.MyNode t : template) {
          if (node.getNodeName().equalsIgnoreCase(t.getName())) {
            System.out.println(node.getNodeName());
            if (equals(node.getChildNodes(), t.getChild())) {
              valid = true;
              break;
            }
          }
        }
        if (!valid) {
          return false;
        }
      }
    }
    return true;
  }
  
  public void print() {
    System.out.println("root: " + doc.getDocumentElement().getNodeName() + " [OPEN]");
    printNote(doc.getDocumentElement().getChildNodes());
    System.out.println("root: " + doc.getDocumentElement().getNodeName() + " [CLOSE]");
  }
  
  private static void printNote(NodeList nodeList) {

    for (int count = 0; count < nodeList.getLength(); count++) {

      Node tempNode = nodeList.item(count);
      
      if(tempNode.getNodeType() == Node.TEXT_NODE) {
        System.out.println("Node value =" + tempNode.getNodeValue());
      }
      // make sure it's element node.
      if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

        // get node name
        System.out.println("Node Name =" + tempNode.getNodeName() + " [OPEN]");

        if (tempNode.hasChildNodes()) {
          // loop again if has child nodes
          printNote(tempNode.getChildNodes());
        }
        System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
      }
    }
  }
  
  public Bill getBill() {
    Bill bill = new Bill();
    
    bill.setInvoiceNumber(getValueOfUniqueTag("InvNb"));
    bill.setInvoiceNetAmount(Double.valueOf(getValueOfUniqueTag("InvTotalNetA").replace(',', '.')));
    bill.setInvoiceTaxAmount(Double.valueOf(getValueOfUniqueTag("InvTotalTaxA").replace(',', '.')));
    bill.setFromDate(LocalDate.parse(getValueOfUniqueTag("Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
    bill.setEndDate(LocalDate.parse(getValueOfUniqueTag("End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")));
    
    NodeList nodes = doc.getElementsByTagName("FeeItem");
    for(int i = 0; i < nodes.getLength(); i++) {
      bill.addFee(new FeeItem((Element) nodes.item(i)));
    }
    return bill;
  }
  
  private String getValueOfUniqueTag(String tagname) {
    return doc.getElementsByTagName(tagname).item(0).getFirstChild().getNodeValue();
  }
}
