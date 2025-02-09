package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.statistics.InvoiceStatistics;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.filter.InvoiceFilter;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import cz.itnetwork.entity.repository.specification.InvoiceSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
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
        entity.setSeller(personRepository.getReferenceById(invoiceDTO.getSeller().getId()));
        entity.setBuyer(personRepository.getReferenceById(invoiceDTO.getBuyer().getId()));
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
        }catch(NotFoundException e){
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        try {
            invoiceRepository.delete(invoiceRepository.getReferenceById(invoiceId));
        }
        catch(NotFoundException e){
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
        }catch (RuntimeException e){
            throw new EntityNotFoundException();
        }
        return statistics;
    }

}
