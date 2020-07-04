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

import com.sec.billing.Invoice;
import com.sec.billing.FeeItem;
import com.sec.billing.exception.FileUploadException;
import com.sec.billing.repository.InvoiceRepository;
import com.sec.entity.User;
import com.sec.entity.viewEntity.OneCategoryOfUserFinance;
import com.sec.entity.viewEntity.InvoiceOfOneNumberOfUser;

@Service
public class InvoiceService {

  private InvoiceRepository invoiceRepository;

  private BillTemplateService billTemplateService;
  
  private FeeItemService feeItemService;

  @Autowired
  public void setInvoiceRepository(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @Autowired
  public void setBillTemplateService(BillTemplateService billTemplateService) {
    this.billTemplateService = billTemplateService;
    billTemplateService.createBasicTemplate();
  }
  
  @Autowired
  public void setFeeItemService(FeeItemService feeItemService) {
    this.feeItemService = feeItemService;
  }

  public Invoice uploadBill(MultipartFile file) throws FileUploadException {
    String xmlString = getXMLString(file);
    Element root = getTreeFromXMLString(xmlString);
    boolean valid = billTemplateService.validateBill(root);

    if (valid) {
      if (invoiceRepository.findByInvoiceNumber(getFirstTagValue(root, "InvNb")) != null) {
        throw new FileUploadException("Already exists");
      }
      
      Invoice invoice = new Invoice(xmlString,
          LocalDate.parse(getFirstTagValue(root, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
          LocalDate.parse(getFirstTagValue(root, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
          getFirstTagValue(root, "InvNb"), Double.valueOf(getFirstTagValue(root, "InvTotalNetA").replace(',', '.')),
          Double.valueOf(getFirstTagValue(root, "InvTotalTaxA").replace(',', '.')),
          Double.valueOf(getFirstTagValue(root, "InvTotalGrossA").replace(',', '.'))
          );

      NodeList nodes = root.getElementsByTagName("FeeItem");
      for (int i = 0; i < nodes.getLength(); i++) {
        Element feeItem = (Element) nodes.item(i);
        invoice.addFee(new FeeItem(
            invoice, 
            getFirstTagValue(feeItem, "ItemNr"),
            getFirstTagValue(feeItem, "Desc"),
            LocalDate.parse(getFirstTagValue(feeItem, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
            LocalDate.parse(getFirstTagValue(feeItem, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
            Double.valueOf(getFirstTagValue(feeItem, "NetA").replace(',', '.')), 
            Double.valueOf(getFirstTagValue(feeItem, "TaxA").replace(',', '.')),
            Double.valueOf(getFirstTagValue(feeItem, "TaxP").replace(',', '.').replace("%", "")),
            Double.valueOf(getFirstTagValue(feeItem, "GrossA").replace(',', '.'))));
      }
      invoiceRepository.save(invoice);
      return invoice;
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

  public List<Invoice> findAll() {
    return invoiceRepository.findAll();
  }

  public Invoice findByInvoiceNumber(String invoiceNumber) {
    return invoiceRepository.findByInvoiceNumber(invoiceNumber);
  }

  public Invoice findById(long id) {
    return invoiceRepository.findOne(id);
  }

  public List<FeeItem> findAllFeeItemByBillId(long id) {
    return feeItemService.findAllByBillId(id);
  }

  public void save(Invoice invoice) {
    invoiceRepository.save(invoice);
  }

  public List<OneCategoryOfUserFinance> getFinanceByUserId(long id) {
    return feeItemService.getFinanceByUserId(id);
  }

  public void save(List<FeeItem> fees) {
    feeItemService.save(fees);
  }
  
  public List<InvoiceOfOneNumberOfUser> getActualFinanceSummary() {
    return feeItemService.getActualFinanceSummary();
  }

  public InvoiceOfOneNumberOfUser getUnacceptedInvoiceOfOneNumberOfUser(String number) {
    return feeItemService.getUnacceptedInvoiceOfOneNumberOfUser(number);
  }

}
