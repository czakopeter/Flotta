package com.sec.billing.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sec.billing.Bill;
import com.sec.billing.FeeItem;
import com.sec.billing.SplittedFeeItem;
import com.sec.billing.exception.FileUploadException;
import com.sec.billing.repository.BillRepository;

@Service
public class BillService {

  private BillRepository billRepository;

  private BillTemplateService billTemplateService;

  @Autowired
  public void setBillRepository(BillRepository billRepository) {
    this.billRepository = billRepository;
  }

  @Autowired
  public void setBillTemplateService(BillTemplateService billTemplateService) {
    this.billTemplateService = billTemplateService;
    billTemplateService.createBasicTemplate();
  }

  public Bill uploadBill(MultipartFile file) throws FileUploadException {
    String xmlString = getXMLString(file);
    Element root = getTreeFromXMLString(xmlString);
    boolean valid = billTemplateService.validateBill(root);

    if (valid) {
      if (billRepository.findByInvoiceNumber(getFirstTagValue(root, "InvNb")) != null) {
        throw new FileUploadException("Already exists");
      }
      
      Bill bill = new Bill(xmlString, LocalDate.parse(getFirstTagValue(root, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")), LocalDate.parse(getFirstTagValue(root, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")), getFirstTagValue(root, "InvNb"), Double.valueOf(getFirstTagValue(root, "InvTotalNetA").replace(',', '.')), Double.valueOf(getFirstTagValue(root, "InvTotalTaxA").replace(',', '.')));

      NodeList nodes = root.getElementsByTagName("FeeItem");
      for (int i = 0; i < nodes.getLength(); i++) {
        Element feeItem = (Element) nodes.item(i);
        bill.addFee(new FeeItem(bill, getFirstTagValue(feeItem, "ItemNr"), getFirstTagValue(feeItem, "Desc"), LocalDate.parse(getFirstTagValue(feeItem, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")), LocalDate.parse(getFirstTagValue(feeItem, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")), Double.valueOf(getFirstTagValue(feeItem, "NetA").replace(',', '.')), Double.valueOf(getFirstTagValue(feeItem, "TaxA").replace(',', '.')), Double.valueOf(getFirstTagValue(feeItem, "TaxP").replace(',', '.').replace("%", ""))));
      }
      billRepository.save(bill);
      return bill;
    } else {
      throw new FileUploadException("Invalid structure");
    }
  }

  private String getFirstTagValue(Element root, String tagname) {
    return root.getElementsByTagName(tagname).item(0).getFirstChild().getNodeValue();
  }

  private Element getTreeFromXMLString(String xmlString) throws FileUploadException {

    // TODO convert to utf-8
    try {
      DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));

      doc.getDocumentElement().normalize();

      return doc.getDocumentElement();
    } catch (ParserConfigurationException e) {
      throw new FileUploadException("ParserConfigurationException");
    } catch (IOException e) {
      throw new FileUploadException("IOException");
    } catch (SAXException e) {
      throw new FileUploadException("SAXException");
    }
  }

  private String getXMLString(MultipartFile file) throws FileUploadException {
    if (file.isEmpty()) {
      throw new FileUploadException("Empty file");
    }

    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        line = br.readLine();
      }

      return sb.toString().replaceAll(">\\s+<", "><");
    } catch (IOException e) {
      throw new FileUploadException("IOException");
    }
  }

  public List<Bill> findAll() {
    return billRepository.findAll();
  }

  public Bill findByInvoiceNumber(String invoiceNumber) {
    return billRepository.findByInvoiceNumber(invoiceNumber);
  }

  public Bill findById(long id) {
    return billRepository.findOne(id);
  }

  public List<FeeItem> findAllFeeItemByBillId(long id) {
    Bill bill = billRepository.findOne(id);
    if(bill != null) {
      return bill.getFeeItems();
    }
    return null;
  }

}
