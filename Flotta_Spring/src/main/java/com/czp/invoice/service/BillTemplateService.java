package com.czp.invoice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.czp.invoice.MyNode;
import com.czp.invoice.repository.BillTemplateRepository;

@Service
public class BillTemplateService {

  //TODO function to add new template or modify
  
  private BillTemplateRepository billTemplateRepository;

  @Autowired
  public void setBillTemplateRepository(BillTemplateRepository billTemplateRepository) {
    this.billTemplateRepository = billTemplateRepository;
  }

  public void createBasicTemplate() {

    MyNode root = MyNode.createRoot(1, "Account");

    MyNode feeItems = root.appendChild("FeeItems");
    MyNode feeItem = feeItems.appendChild("FeeItem");

    feeItem.appendChild("ItemNr");
    feeItem.appendChild("Desc");
    feeItem.appendChild("Begin");
    feeItem.appendChild("End");
    feeItem.appendChild("NetA");
    feeItem.appendChild("TaxP");
    feeItem.appendChild("TaxA");
    feeItem.appendChild("GrossA");

    MyNode customerData = root.appendChild("CustomerData");
    customerData.appendChild("Name");
    customerData.appendChild("City");

    MyNode companyData = root.appendChild("CompanyData");
    companyData.appendChild("Name");
    companyData.appendChild("City");

    MyNode invoiceData = root.appendChild("InvoiceData");
    invoiceData.appendChild("Begin");
    invoiceData.appendChild("End");
    invoiceData.appendChild("InvNb");
    invoiceData.appendChild("InvTotalNetA");
    invoiceData.appendChild("InvTotalTaxA");
    invoiceData.appendChild("InvTotalGrossA");

    billTemplateRepository.save(root);

//    root.show();
  }

  public List<MyNode> findAllRoot() {
    return billTemplateRepository.findAllByParentIsNull();
  }

  public List<MyNode> findAllRootByName(String name) {
    return billTemplateRepository.findAllByParentIsNullAndName(name);
  }

  public boolean invoiceTreeFormalCheck(Element root) {
    List<MyNode> templates = findAllRootByName(root.getNodeName());
    if (templates.isEmpty()) {
      return false;
    }
    
    for(MyNode template : templates) {
      if(equals(root.getChildNodes(), template.getChild())) {
        return true;
      }
    }

    return false;
  }

  private boolean equals(NodeList nodes, List<MyNode> subTemplates) {
    for (int i = 0; i < nodes.getLength(); i++) {
      Node tempNode = nodes.item(i);
      if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
        boolean valid = false;
        for (MyNode subTemplate : subTemplates) {
          if (tempNode.getNodeName().equalsIgnoreCase(subTemplate.getName())) {
            if (equals(tempNode.getChildNodes(), subTemplate.getChild())) {
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
}
