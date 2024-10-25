package tech.leonam.erp.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import tech.leonam.erp.model.DTO.EstoqueDTO;
import tech.leonam.erp.model.entity.Estoque;
import tech.leonam.erp.service.EstoqueService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/estoque")
public class EstoqueController {
    private final EstoqueService estoqueService;

    private final String PADRAO_LINHAS_POR_PAGINA = "20";
    private final String PADRAO_PAGINA = "0";
    private final String PADRAO_DE_ORDEM = "id";
    private final String PADRAO_DE_DIRECAO = "ASC";

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> salvarEstoque(@Valid EstoqueDTO dto,
            @RequestParam("imagem") MultipartFile imagem) throws IOException {

        estoqueService.salvarEstoque(dto,
                imagem);
        return ResponseEntity.ok().body("Estoque salvo com sucesso");
    }

    @GetMapping
    public ResponseEntity<Page<Estoque>> listarEstoque(
            @RequestParam(defaultValue = PADRAO_PAGINA) @Min(0) Integer pagina,
            @RequestParam(defaultValue = PADRAO_LINHAS_POR_PAGINA) @Min(1) Integer linhasPorPagina,
            @RequestParam(defaultValue = PADRAO_DE_ORDEM) String orderBy,
            @RequestParam(defaultValue = PADRAO_DE_DIRECAO) String direcao,
            @RequestParam(defaultValue = "2") Integer status) {
        return ResponseEntity.ok()
                .body(estoqueService.buscarTodoEstoque(pagina, linhasPorPagina, orderBy, direcao, status));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletarEstoque(@PathVariable Long id) {
        estoqueService.deletarEstoquePorId(id);
        return ResponseEntity.ok().body(String.format("O id: %d foi deletado com sucesso", id));
    }

    @PutMapping("{id}")
    public ResponseEntity<String> alterarEstoque(@PathVariable Long id, @RequestBody @Valid EstoqueDTO dto) {
        estoqueService.alteraEstoque(dto, id);
        return ResponseEntity.ok().body("Estoque alterado com sucesso");
    }
}
