package com.sec.billing.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sec.entity.User;

@Service
public class BillingService {

  private NodeService nodeService;

  @Autowired
  public void setNodeService(NodeService nodeService) {
    this.nodeService = nodeService;
  }

  // beolvassa a számlát
  // valid akkor konvertál validFeeItem-re
  // newm valid új template készítése
  public void uploadBill(MultipartFile file) {
    try {
      String xmlString = test(file);

      InputStream is = file.getInputStream();
      DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//      Document doc = dBuilder.parse(is);
      Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));

      doc.getDocumentElement().normalize();

      if (validXMLStructure(doc.getDocumentElement())) {
        System.out.println("OK");
      } else {
        System.out.println("NO");
      }

    } catch (Exception e) {
      System.out.println("Exception");
    }

  }

  List<ValidFeeItem> getFeeItemsOfUser(User user) {
    return null;
  }

  List<ValidFeeItem> getFeeItemsOfUser(User user, LocalDate begin, LocalDate end) {
    return null;
  }

  boolean addCategory(String category) {
    return true;
  }

  void connectValidFeeItemToCategory(ValidFeeItem item, String category) {

  }

  private static void printNote(NodeList nodeList) {

    for (int count = 0; count < nodeList.getLength(); count++) {

      Node tempNode = nodeList.item(count);

      // make sure it's element node.
      if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

        // get node name and value
        System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
//        System.out.println("Node Value =" + tempNode.getTextContent());

        if (tempNode.hasAttributes()) {

          // get attributes names and values
          NamedNodeMap nodeMap = tempNode.getAttributes();

          for (int i = 0; i < nodeMap.getLength(); i++) {

            Node node = nodeMap.item(i);
//            System.out.println("attr name : " + node.getNodeName());
//            System.out.println("attr value : " + node.getNodeValue());

          }
        }

        if (tempNode.hasChildNodes()) {
          // loop again if has child nodes
          printNote(tempNode.getChildNodes());
        }
        System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
      }
    }
  }

  private boolean validXMLStructure(Node root) {
    for (com.sec.billing.service.Node template : nodeService.findAllRoot()) {
      if (root.getNodeName().equalsIgnoreCase(template.getName())) {
        if (equals(root.getChildNodes(), template.getChild())) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean equals(NodeList nodes, List<com.sec.billing.service.Node> template) {
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      System.out.println("--------------------");
      System.out.println(i);
//      System.out.println(node.getNodeName() != null ? node.getNodeName() : "no node name");
//      System.out.println(node.getNodeName() != null ? node.getNodeValue() : "no node value");
//      System.out.println(node.getNodeName() != null ? node.getNodeType() : "no node type");
//      
//      if (node.getNodeValue() != null && node.getNodeValue().isEmpty()) {
//        System.out.println(" " + node.getNodeValue());
//      }
      if (node.getNodeType() == Node.TEXT_NODE) {
        System.out.println(node.getNodeValue().trim());
      }
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        boolean valid = false;
        for (com.sec.billing.service.Node t : template) {
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

  private String test(MultipartFile file) {
    if (file.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        line = br.readLine();
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return sb.toString().replaceAll(">\\s+<", "><");
  }
}
