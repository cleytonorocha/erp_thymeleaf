package tech.leonam.erp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import tech.leonam.erp.model.DTO.EstoqueDTO;
import tech.leonam.erp.model.entity.Estoque;
import tech.leonam.erp.repository.EstoqueRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;
    private final ModelMapper modelMapper;

    public void deletarEstoquePorId(Long id) {
        log.info("Iniciando a deleção do estoque com id {}", id);
        try {
            estoqueRepository.deleteById(id);
            log.info("Estoque com id {} deletado com sucesso.", id);
        } catch (Exception e) {
            log.error("Erro ao deletar estoque com id {}: {}", id, e.getMessage(), e);
        }
    }

    public Page<Estoque> buscarTodoEstoque(@Min(0) Integer pagina, @Min(1) Integer linhasPorPagina,
            String orderBy, String direcao, Integer status) {
        log.info("Buscando todo o estoque - Página: {}, Linhas por página: {}, OrderBy: {}, Direção: {}",
                pagina, linhasPorPagina, orderBy, direcao);

        PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Sort.Direction.valueOf(direcao), orderBy);
        Page<Estoque> estoquePage = estoqueRepository.findAll(pageRequest);
        log.info("Total de itens no estoque: {}", estoquePage.getTotalElements());
        return estoquePage;
    }

    public void alteraEstoque(EstoqueDTO estoqueDTO, Long id) {
        log.info("Iniciando a atualização do estoque com id {}", id);
        Estoque estoque = modelMapper.map(estoqueDTO, Estoque.class);
        estoque.setId(id);
        try {
            estoqueRepository.save(estoque);
            log.info("Estoque com id {} atualizado com sucesso.", id);
        } catch (Exception e) {
            log.error("Erro ao atualizar estoque com id {}: {}", id, e.getMessage(), e);
        }
    }

    public void salvarEstoque(EstoqueDTO dto, MultipartFile imagem) {
        log.info("Iniciando o salvamento do estoque.");
        
        Estoque estoque = modelMapper.map(dto, Estoque.class);
        Estoque estoqueTratado = salvarImagem(estoque, imagem);
        try {
            estoqueRepository.save(estoqueTratado);
            log.info("Estoque com id {} salvo com sucesso.", estoqueTratado.getId());
        } catch (Exception e) {
            log.error("Erro ao salvar estoque: {}", e.getMessage(), e);
        }
    }

    private Estoque salvarImagem(Estoque estoque, MultipartFile imagem) {
        log.info("Iniciando o salvamento da imagem para o estoque com id {}", estoque.getId());
        
        try {
            String uploadDir = "src/main/resources/static/img/db/estoque/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
                log.info("Diretório para imagens de estoque criado: {}", uploadDir);
            }

            Path caminhoCompleto = Paths.get(uploadDir, estoque.getId() + "_" + estoque.getCriadoPor() + "_"
                    + estoque.getNome().strip() + Optional.of(imagem.getOriginalFilename()).get().stripLeading());
            byte[] bytes = imagem.getBytes();
            Files.write(caminhoCompleto, bytes);
            estoque.setPathImagem(caminhoCompleto.toString().replace("src/main/resources/static/", ""));
            log.info("Imagem do estoque com id {} salva em: {}", estoque.getId(), caminhoCompleto);
        } catch (IOException e) {
            log.error("Erro ao salvar a imagem para o estoque com id {}: {}", estoque.getId(), e.getMessage(), e);
        }
        return estoque;
    }
}
