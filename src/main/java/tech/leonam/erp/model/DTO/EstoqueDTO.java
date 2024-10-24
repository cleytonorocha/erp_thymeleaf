package tech.leonam.erp.model.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {

    @NotBlank
    @Size(min = 3, max = 255, message = "Verifique o tamanho do nome.")
    private String nome;
    @PositiveOrZero(message = "Verifique se o Preço Unitário é maior ou igual que 0.")
    private BigDecimal precoUnitario;
    
    @PositiveOrZero(message = "Verifique se a quantidade é maior ou igual a 0.")
    private Integer quantidade;
    @Timestamp
    private LocalDate validade;
    @Timestamp
    private LocalDateTime dataCadastro;
    @Timestamp
    private LocalDateTime dataDaCompra;
    private Long idCategoria;

    private String pathImagem;


}
