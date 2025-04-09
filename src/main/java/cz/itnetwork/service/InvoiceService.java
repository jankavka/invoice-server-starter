package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.statistics.InvoiceStatistics;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.filter.InvoiceFilter;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface InvoiceService {

    /**
     * Creates a new invoice
     *
     * @param invoiceDTO is object with invoice details
     * @return DTO with created invoice
     */
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);

    /**
     * Fetches invoices from database. Fetch is determined by filter.
     *
     * @param invoiceFilter is object which represents filter for fetching invoices
     * @return List of DTO's fetched from database
     */
    List<InvoiceDTO> getAllInvoices(InvoiceFilter invoiceFilter);

    InvoiceDTO getInvoice(Long invoiceId);

    InvoiceDTO updateInvoice(Long invoiceId, InvoiceDTO invoiceDTO);

    void deleteInvoice(Long invoiceId);

    /**
     * This method fetches certain statistics from database
     *
     * @return InvoiceStatistic object with concrete attributes fetched from database.
     */
    InvoiceStatistics getInvoiceStatistics();

    ByteArrayOutputStream createPDF(InvoiceDTO invoiceDTO);

    ResponseEntity<byte[]> getPdf(Long invoiceId);

    void generateQrCode(InvoiceDTO invoiceDTO, PersonEntity seller, String pathToStoreQrCode);
}
