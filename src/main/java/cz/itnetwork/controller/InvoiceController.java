package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.statistics.InvoiceStatistics;
import cz.itnetwork.entity.filter.InvoiceFilter;
import cz.itnetwork.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping("/invoices")
    public InvoiceDTO createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.createInvoice(invoiceDTO);
    }

    @GetMapping("/invoices")
    public List<InvoiceDTO> getAllInvoices(InvoiceFilter invoiceFilter) {
        return invoiceService.getAllInvoices(invoiceFilter);
    }

    @GetMapping("identification/{identificationNumber}/sales")
    public List<InvoiceDTO> getAllInvoicesBySeller(@PathVariable String identificationNumber) {
        return invoiceService.getAllInvoicesBySeller(identificationNumber);
    }

    @GetMapping("identification/{identificationNumber}/purchases")
    public List<InvoiceDTO> getAllInvoicesByBuyer(@PathVariable String identificationNumber) {
        return invoiceService.getAllInvoicesByBuyer(identificationNumber);
    }

    @GetMapping("/invoices/{invoiceId}")
    public InvoiceDTO getInvoiceDetail(@PathVariable Long invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    @PutMapping("/invoices/{invoiceId}")
    public InvoiceDTO editInvoice(@PathVariable Long invoiceId, @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.updateInvoice(invoiceId, invoiceDTO);
    }

    @DeleteMapping("/invoices/{invoiceId}")
    public HttpStatus deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping("/invoices/statistics")
    public InvoiceStatistics showInvoiceStatistics(){
        return invoiceService.getInvoiceStatistics();
    }
}
