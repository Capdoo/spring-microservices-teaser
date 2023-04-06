package rafael.projects.store.springshoppingservice.shoppingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rafael.projects.store.springshoppingservice.shoppingservice.client.CustomerClient;
import rafael.projects.store.springshoppingservice.shoppingservice.client.ProductClient;
import rafael.projects.store.springshoppingservice.shoppingservice.entity.Invoice;
import rafael.projects.store.springshoppingservice.shoppingservice.entity.InvoiceItem;
import rafael.projects.store.springshoppingservice.shoppingservice.models.Customer;
import rafael.projects.store.springshoppingservice.shoppingservice.models.Product;
import rafael.projects.store.springshoppingservice.shoppingservice.repository.InvoiceItemsRepository;
import rafael.projects.store.springshoppingservice.shoppingservice.repository.InvoiceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService{

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemsRepository invoiceItemsRepository;

    //Usado en Feign
    @Autowired
    ProductClient productClient;
    @Autowired
    CustomerClient customerClient;



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
        invoiceDB = invoiceRepository.save(invoice);
        //Feign: Actualizar stock por cada producto
        invoiceDB.getItems().forEach( e -> {
            productClient.updateStockProduct(e.getProductId(), e.getQuantity() * -1);
        });

        return invoiceDB;
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
        //Feign: Actualizar obtener info
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if(invoice != null){
            //Recuperar usuario
            Customer customer = customerClient.getCustomer(invoice.getCustomerId()).getBody();
            //Asignar usuario en factura
            invoice.setCustomer(customer);
            //Recuperar datos de c/producto de nuestra factura
            List<InvoiceItem> listItems = invoice.getItems().stream().map(
                    item -> {
                        Product product = productClient.getProduct(item.getProductId()).getBody();
                        item.setProduct(product);
                        return item;
                    }
            ).collect(Collectors.toList());
            //Asignar nueva lista de items
            invoice.setItems(listItems);


        }
        return invoice;
    }
}
