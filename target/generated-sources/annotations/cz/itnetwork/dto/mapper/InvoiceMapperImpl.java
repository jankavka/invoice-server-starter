package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.entity.InvoiceEntity;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class InvoiceMapperImpl implements InvoiceMapper {

    @Autowired
    private PersonMapper personMapper;

    @Override
    public InvoiceEntity toEntity(InvoiceDTO source) {
        if ( source == null ) {
            return null;
        }

        InvoiceEntity invoiceEntity = new InvoiceEntity();

        invoiceEntity.setId( source.getId() );
        if ( source.getInvoiceNumber() != null ) {
            invoiceEntity.setInvoiceNumber( source.getInvoiceNumber().intValue() );
        }
        invoiceEntity.setSeller( personMapper.toEntity( source.getSeller() ) );
        invoiceEntity.setBuyer( personMapper.toEntity( source.getBuyer() ) );
        invoiceEntity.setIssued( source.getIssued() );
        invoiceEntity.setDueDate( source.getDueDate() );
        invoiceEntity.setProduct( source.getProduct() );
        if ( source.getPrice() != null ) {
            invoiceEntity.setPrice( source.getPrice().longValue() );
        }
        invoiceEntity.setVat( source.getVat() );
        invoiceEntity.setNote( source.getNote() );

        return invoiceEntity;
    }

    @Override
    public InvoiceDTO toDTO(InvoiceEntity source) {
        if ( source == null ) {
            return null;
        }

        InvoiceDTO invoiceDTO = new InvoiceDTO();

        if ( source.getInvoiceNumber() != null ) {
            invoiceDTO.setInvoiceNumber( source.getInvoiceNumber().longValue() );
        }
        invoiceDTO.setSeller( personMapper.toDTO( source.getSeller() ) );
        invoiceDTO.setBuyer( personMapper.toDTO( source.getBuyer() ) );
        invoiceDTO.setIssued( source.getIssued() );
        invoiceDTO.setDueDate( source.getDueDate() );
        invoiceDTO.setProduct( source.getProduct() );
        if ( source.getPrice() != null ) {
            invoiceDTO.setPrice( source.getPrice().intValue() );
        }
        invoiceDTO.setVat( source.getVat() );
        invoiceDTO.setNote( source.getNote() );
        invoiceDTO.setId( source.getId() );

        return invoiceDTO;
    }
}
