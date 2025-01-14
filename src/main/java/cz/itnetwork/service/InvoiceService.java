package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.statistics.StatisticsInvoiceDTO;
import cz.itnetwork.entity.filter.InvoiceFilter;

import java.util.List;

public interface InvoiceService {

    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);

    List<InvoiceDTO> getAllInvoices(InvoiceFilter invoiceFilter);

    List<InvoiceDTO> getAllInvoicesBySeller(String identificationNumber);

    List<InvoiceDTO> getAllInvoicesByBuyer(String identificationNumber);

    InvoiceDTO getInvoice(Long invoiceId);

    InvoiceDTO updateInvoice(Long invoiceId, InvoiceDTO invoiceDTO);

    void deleteInvoice(Long invoiceId);

    StatisticsInvoiceDTO getInvoiceStatistics();
}
