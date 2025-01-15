package cz.itnetwork.dto.statistics;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InvoiceStatistics {

    private Integer currentYearSum;
    private Integer allTimeSum;
    private Integer InvoicesCount;
}
