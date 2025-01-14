package cz.itnetwork.entity.filter;

import lombok.Data;

@Data
public class InvoiceFilter {

    private Long buyerId;
    private Long sellerId;
    private String product;
    private Integer minPrice;
    private Integer maxPrice;
    private Integer limit = 10;

}
