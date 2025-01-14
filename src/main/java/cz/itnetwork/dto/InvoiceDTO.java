package cz.itnetwork.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    @NotNull
    private Long invoiceNumber;

    private PersonDTO seller;

    private PersonDTO buyer;

    @NotNull
    private LocalDate issued;

    @NotNull
    private LocalDate dueDate;

    @NotBlank
    private String product;

    @NotNull
    private Integer price;

    @NotNull
    private Integer vat;

    private String note;


}
