package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>, JpaSpecificationExecutor<InvoiceEntity> {

    /**
     * fetches a sum of all prices of all invoices in current year from database
     *
     * @return a number as Integer representing sum of all prices this year
     */
    @Query(value = "SELECT SUM(price) sum FROM invoice WHERE YEAR(issued) = YEAR(CURDATE())")
    Integer getCurrentYearSum();

    /**
     * fetches sum of all prices of all invoices from database
     *
     * @return number as Integer representing sum of all prices of all invoices from database
     */
    @Query(value = "SELECT SUM(price) sum FROM invoice")
    Integer getAllTimeSum();

    /**
     * fetches count of invoices
     *
     * @return number as Integer representing count of invoices
     */
    @Query(value = "SELECT COUNT(i) FROM invoice i")
    Integer getInvoicesCount();
}
