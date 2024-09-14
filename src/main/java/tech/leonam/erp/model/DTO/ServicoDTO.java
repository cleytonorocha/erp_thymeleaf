package tech.leonam.erp.model.DTO;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.leonam.erp.model.entity.Cliente;
import tech.leonam.erp.model.entity.Servico;
import tech.leonam.erp.model.entity.TipoPagamento;
import tech.leonam.erp.model.enums.StatusServico;

/**
 * DTO que representa a entidade serviço.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "O nome do serviço é obrigatório e não pode estar em branco")
    @Size(min = 1, max = 250, message = "O nome do serviço deve conter entre 1 e 250 caracteres")
    private String nome;

    @NotNull(message = "O preço não pode ser nulo")
    @DecimalMin(value = "0.01", message = "O preço deve ser superior a 0")
    @Digits(integer = 12, fraction = 2, message = "O preço deve ter no máximo 6 dígitos inteiros e 2 decimais")
    private BigDecimal preco;

    @NotNull(message = "O cliente id é obrigatório e não pode ser nulo")
    private Long clienteId;

    @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private Long tipoPagamentoId;

    @NotNull(message = "O status do serviço é obrigatório")
    private StatusServico status;

    @NotNull(message = "A data de pagamento previsto é obrigatória")
    @Future(message = "A data de pagamento previsto deve ser uma data futura")
    private LocalDateTime pagamentoPrevisto;

    @NotNull(message = "A data de pagamento final é obrigatória")
    @FutureOrPresent(message = "A data de pagamento final deve ser no presente ou no futuro")
    private LocalDateTime pagamentoFinal;

    private String modificadoPor;
    private LocalDateTime dataUltimaModificacao;
    private LocalDateTime dataCriacao;
    private String criadoPor;

    public static Servico paraEntidade(ServicoDTO servicoDTO) {
        return Servico.builder()
                .id(servicoDTO.getId())
                .nome(servicoDTO.getNome())
                .preco(servicoDTO.getPreco())
                .cliente(Cliente.builder().id(servicoDTO.getClienteId()).build())
                .descricao(servicoDTO.getDescricao())
                .tipoPagamento(TipoPagamento.builder().id(servicoDTO.getId()).build())
                .status(servicoDTO.getStatus())
                .pagamentoPrevisto(servicoDTO.getPagamentoPrevisto())
                .pagamentoFinal(servicoDTO.getPagamentoFinal())
                .dataCriacao(null)
                .dataModificacao(null)
                .criadoPor(servicoDTO.getCriadoPor())
                .modificadoPor(servicoDTO.getModificadoPor())
                .build();
    }

    public static ServicoDTO paraDTO(Servico servico) {
        return ServicoDTO.builder()
                .id(servico.getId())
                .nome(servico.getNome())
                .preco(servico.getPreco())
                .clienteId(servico.getCliente()
                        .getId())
                .descricao(servico.getDescricao())
                .tipoPagamentoId(servico.getTipoPagamento().getId())
                .status(servico.getStatus())
                .pagamentoPrevisto(servico.getPagamentoPrevisto())
                .pagamentoFinal(servico.getPagamentoFinal())
                .modificadoPor(servico.getModificadoPor())
                .dataCriacao(servico.getDataCriacao())
                .criadoPor(servico.getCriadoPor())
                .dataUltimaModificacao(servico.getDataModificacao())
                .build();
    }
}
