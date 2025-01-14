package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>, JpaSpecificationExecutor<InvoiceEntity>, PagingAndSortingRepository<InvoiceEntity, Long> {

    @Query(value = "SELECT SUM(price) sum FROM invoice WHERE YEAR(issued) = YEAR(CURDATE())")
    List<Integer> getCurrentYearSum();

    @Query(value = "SELECT SUM(price) sum FROM invoice")
    List<Integer> getAllTimeSum();

    @Query(value = "SELECT COUNT(i) FROM invoice i")
    List<Integer> getInvoicesCount();
}
