package cz.itnetwork.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonStatistics {

    private Long personId;
    private String personName;
    private Long revenue;

}
