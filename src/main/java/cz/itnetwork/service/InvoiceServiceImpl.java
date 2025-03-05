package cz.itnetwork.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.statistics.InvoiceStatistics;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.filter.InvoiceFilter;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import cz.itnetwork.entity.repository.specification.InvoiceSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PersonRepository personRepository;


    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {

        InvoiceEntity entity = invoiceMapper.toEntity(invoiceDTO);
        byte[] pdfDocument = createPDF(invoiceDTO).toByteArray();
        entity.setSeller(personRepository.getReferenceById(invoiceDTO.getSeller().getId()));
        entity.setBuyer(personRepository.getReferenceById(invoiceDTO.getBuyer().getId()));
        entity.setPdfContent(pdfDocument);
        InvoiceEntity savedInvoice;
        savedInvoice = invoiceRepository.save(entity);
        return invoiceMapper.toDTO(savedInvoice);
    }

    @Override
    public List<InvoiceDTO> getAllInvoices(InvoiceFilter invoiceFilter) {
        InvoiceSpecification invoiceSpecification = new InvoiceSpecification(invoiceFilter);

        return invoiceRepository.findAll(invoiceSpecification, PageRequest.of(0, invoiceFilter.getLimit()))
                .stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO getInvoice(Long invoiceId) {
        return invoiceMapper.toDTO(invoiceRepository.getReferenceById(invoiceId));
    }

    @Override
    public InvoiceDTO updateInvoice(Long invoiceId, InvoiceDTO invoiceDTO) {
        InvoiceEntity entity = invoiceMapper.toEntity(invoiceDTO);
        entity.setId(invoiceId);
        try {
            InvoiceEntity saved = invoiceRepository.save(entity);
            return invoiceMapper.toDTO(saved);
        } catch (NotFoundException e) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        try {
            invoiceRepository.delete(invoiceRepository.getReferenceById(invoiceId));
        } catch (NotFoundException e) {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public InvoiceStatistics getInvoiceStatistics() {
        InvoiceStatistics statistics = new InvoiceStatistics();
        try {
            statistics.setCurrentYearSum(invoiceRepository.getCurrentYearSum());
            statistics.setInvoicesCount(invoiceRepository.getInvoicesCount());
            statistics.setAllTimeSum(invoiceRepository.getAllTimeSum());
        } catch (RuntimeException e) {
            throw new EntityNotFoundException();
        }
        return statistics;
    }

    @Override
    public ByteArrayOutputStream createPDF(InvoiceDTO invoiceDTO) {

        final float lineHeight = 20F;
        PersonEntity seller = personRepository.getReferenceById(invoiceDTO.getSeller().getId());
        PersonEntity buyer = personRepository.getReferenceById(invoiceDTO.getBuyer().getId());
        //Path to store qrCode
        String pathToStoreQrCode = "/Users/mpmp/Desktop/qrCodeTest/qrCode" + invoiceDTO.getInvoiceNumber() + ".jpg";
        generateQrCode(invoiceDTO, seller, pathToStoreQrCode);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();

            //loading image from storage
            PDImageXObject image = PDImageXObject.createFromFile(pathToStoreQrCode, document);

            //new page of document
            document.addPage(page);

            //new font
            InputStream fontStream = getClass().getResourceAsStream("/fonts/Inter_18pt-Regular.ttf");

            //new font added into document
            PDType0Font font = PDType0Font.load(document, fontStream, true);

            //Creating a content for a page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(font, 25);
            contentStream.newLineAtOffset(55, 750);
            contentStream.showText("Faktura číslo: " + invoiceDTO.getInvoiceNumber());
            contentStream.newLineAtOffset(0, 2 * (-lineHeight));
            contentStream.setFont(font, 12);
            contentStream.showText("Dodavatel: " + seller.getName());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("IČO: " + seller.getIdentificationNumber());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Bydliště: " + seller.getStreet() + ", " + seller.getCity() + ", " + seller.getCountry());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Číslo účtu: " + seller.getAccountNumber() + "/" + buyer.getBankCode());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Email: " + seller.getMail());
            contentStream.newLineAtOffset(0, 3 * (-lineHeight));
            contentStream.showText("Odběratel: " + buyer.getName());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("IČO: " + buyer.getIdentificationNumber());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Bydliště: " + buyer.getStreet() + ", " + buyer.getCity() + ", " + buyer.getCountry());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Číslo účtu: " + buyer.getAccountNumber());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Email: " + buyer.getMail());
            contentStream.newLineAtOffset(0, 3 * (-lineHeight));
            contentStream.showText("Datum splatnosti: " + invoiceDTO.getDueDate());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Variabilní symbol: " + invoiceDTO.getInvoiceNumber());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Položky k fakturaci: " + invoiceDTO.getProduct());
            contentStream.newLineAtOffset(0, -lineHeight);
            contentStream.showText("Cena: " + invoiceDTO.getPrice() + " Kč");
            contentStream.newLineAtOffset(0, -3 * (lineHeight));
            contentStream.showText("QR platba:");
            contentStream.endText();

            contentStream.drawImage(image, 50, 180);

            contentStream.close();

            document.save(outputStream);
            return outputStream;

        } catch (IOException e) {
            throw new RuntimeException("Chyba během generování pdf faktury, " + e.getMessage());
        }

    }

    @Override
    public ResponseEntity<byte[]> getPdf(Long invoiceId) {
        byte[] pdfContent = invoiceRepository.getReferenceById(invoiceId).getPdfContent();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=faktura.pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(pdfContent);
    }

    @Override
    public void generateQrCode(InvoiceDTO invoiceDTO, PersonEntity seller, String pathToStoreQrCode) {
        String paymentInfo = "SPD*1.0*ACC:" + seller.getIban() + "*AM:" + invoiceDTO.getPrice()
                + "*CC:CZK" + "*MSG:" + invoiceDTO.getNote()
                + "*X-VS:" + invoiceDTO.getInvoiceNumber();

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(paymentInfo, BarcodeFormat.QR_CODE, 100, 100);
            MatrixToImageWriter.writeToPath(bitMatrix, "jpg", Paths.get(pathToStoreQrCode));
        } catch (Exception e) {
            throw new RuntimeException("Nepodařilo se vygenerovat QR kód");
        }
    }

}
