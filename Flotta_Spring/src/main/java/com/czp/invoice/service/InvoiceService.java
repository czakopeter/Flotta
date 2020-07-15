package com.czp.invoice.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.czp.entity.User;
import com.czp.entity.viewEntity.InvoiceOfUserByNumber;
import com.czp.entity.viewEntity.OneCategoryOfUserFinance;
import com.czp.invoice.FeeItem;
import com.czp.invoice.Invoice;
import com.czp.invoice.InvoiceByUserAndPhoneNumber;
import com.czp.invoice.exception.FileUploadException;
import com.czp.invoice.repository.InvoiceRepository;
import com.czp.service.SubscriptionServiceOnlyInfo;

@Service
public class InvoiceService {

  private InvoiceRepository invoiceRepository;

  private BillTemplateService billTemplateService;
  
  private FeeItemService feeItemService;
  
  private SubscriptionServiceOnlyInfo subscriptionInfo;

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

  @Autowired
  public void setSubscriptionInfo(SubscriptionServiceOnlyInfo subscriptionInfo) {
    this.subscriptionInfo = subscriptionInfo;
  }

  public Invoice uploadBill(MultipartFile file) throws FileUploadException {
    String xmlString = getXMLString(file);
    
    Invoice invoice = processInvoiceXmlString(xmlString);
    
    if(invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber()) == null) {
      return invoiceRepository.save(invoice);
    }
    throw new FileUploadException("Already exists!");
    
  }
  
  private Invoice processInvoiceXmlString(String xml) throws FileUploadException {
    Element root = getTreeFromXMLString(xml);
    boolean valid = billTemplateService.invoiceTreeFormalCheck(root);

    if (valid) {
      
      Invoice invoice = new Invoice(xml,
          LocalDate.parse(getFirstTagValue(root, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
          LocalDate.parse(getFirstTagValue(root, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
          getFirstTagValue(root, "InvNb"), Double.valueOf(getFirstTagValue(root, "InvTotalNetA").replace(',', '.')),
          Double.valueOf(getFirstTagValue(root, "InvTotalTaxA").replace(',', '.')),
          Double.valueOf(getFirstTagValue(root, "InvTotalGrossA").replace(',', '.'))
          );

      NodeList nodes = root.getElementsByTagName("FeeItem");
      for (int i = 0; i < nodes.getLength(); i++) {
        Element feeItem = (Element) nodes.item(i);
        invoice.addFeeItem(
            subscriptionInfo.findByNumber(getFirstTagValue(feeItem, "ItemNr")), 
            new FeeItem(
                getFirstTagValue(feeItem, "ItemNr"),
                getFirstTagValue(feeItem, "Desc"),
                LocalDate.parse(getFirstTagValue(feeItem, "Begin"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
                LocalDate.parse(getFirstTagValue(feeItem, "End"), DateTimeFormatter.ofPattern("uuuu.MM.dd.")),
                Double.valueOf(getFirstTagValue(feeItem, "NetA").replace(',', '.')), 
                Double.valueOf(getFirstTagValue(feeItem, "TaxA").replace(',', '.')),
                Double.valueOf(getFirstTagValue(feeItem, "TaxP").replace(',', '.').replace("%", "")),
                Double.valueOf(getFirstTagValue(feeItem, "GrossA").replace(',', '.'))));
      }
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

  public List<FeeItem> findAllFeeItemByInvoiceId(long id) {
    List<FeeItem> result = new LinkedList<>();
    Invoice invoice = invoiceRepository.findOne(id);
    if(invoice != null) {
      for(InvoiceByUserAndPhoneNumber part : invoice.getInvoicePart()) {
        result.addAll(part.getFees());
      }
    }
    return result;
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
  
  public List<InvoiceOfUserByNumber> getPendingInvoicesOfCurrentUser(User user) {
    return feeItemService.getPendingInvoicesOfCurrentUser(user);
  }

  public InvoiceOfUserByNumber getPendingInvoiceOfCurrentUserBySubscription(User user, String number) {
    return feeItemService.getPendingInvoiceOfCurrentUserByNumber(user, number);
  }

  public boolean acceptInvoiceOfCurrentUserByNumber(User user, String number) {
    return feeItemService.acceptInvoiceOfCurrentUserByNumber(user, number);
  }

  public boolean acceptInvoicesOfCurrentUserByNumbers(User user, List<String> numbers) {
    for(String number : numbers) {
      feeItemService.acceptInvoiceOfCurrentUserByNumber(user, number);
    }
    return true;
  }

  public void askForRevision(User user, String number, Map<String, String> map) {
    feeItemService.askForRevision(user, number, map);
  }

  public void resetInvoiceByInvoiceNumber(String invoiceNumber) {
    Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    
    if(invoice != null) {
      try {
        Invoice reprocess = processInvoiceXmlString(invoice.getXmlString());
        for(FeeItem item : reprocess.getFeeItems()) {
          System.out.println(item);
        }
        invoiceRepository.delete(invoice);
        invoiceRepository.save(reprocess);
      } catch (FileUploadException e) {
        System.err.println(e);
      }
    }
    
  }

  public void deleteInvoiceByInvoiceNumber(String invoiceNumber) {
    Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
    if(invoice.canDelete()) {
      invoiceRepository.delete(invoice);
    }
  }

}
