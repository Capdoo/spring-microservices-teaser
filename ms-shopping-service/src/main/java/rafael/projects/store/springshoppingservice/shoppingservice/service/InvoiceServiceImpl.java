package rafael.projects.store.springshoppingservice.shoppingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rafael.projects.store.springshoppingservice.shoppingservice.entity.Invoice;
import rafael.projects.store.springshoppingservice.shoppingservice.repository.InvoiceItemsRepository;
import rafael.projects.store.springshoppingservice.shoppingservice.repository.InvoiceRepository;

import java.util.List;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService{

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemsRepository invoiceItemsRepository;

    //@Autowired


    @Override
    public List<Invoice> findInvoiceAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        //Indempotencia
        if (null != invoiceDB){
            return invoiceDB;
        }
        invoice.setState("CREATED");
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null){
            return null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        //Clear all items
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());

        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoiceDB = getInvoice(invoice.getId());
        if (invoiceDB == null){
            return null;
        }
        invoiceDB.setState("DELETED");
        return invoiceRepository.save(invoiceDB);
    }

    @Override
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }
}
