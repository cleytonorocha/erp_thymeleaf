package tech.leonam.erp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.leonam.erp.model.DTO.EstoqueDTO;
import tech.leonam.erp.model.entity.Estoque;
import tech.leonam.erp.repository.EstoqueRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;
    private final ModelMapper modelMapper;

    public void deletarEstoquePorId(Long id) {
        estoqueRepository.deleteById(id);
    }

    public Page<Estoque> buscarTodoEstoque(@Min(0) Integer pagina, @Min(1) Integer linhasPorPagina,
            String orderBy,
            String direcao, Integer status) {
        log.info("Buscando todo o estoque - Página: {}, Linhas por página: {}, OrderBy: {}, Direção: {}",
                pagina, linhasPorPagina, orderBy, direcao);
        PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Sort.Direction.valueOf(direcao), orderBy);
        return estoqueRepository.findAll(pageRequest);
    }

    public void alteraEstoque(EstoqueDTO estoqueDTO, Long id) {
        Estoque estoque = modelMapper.map(estoqueDTO, Estoque.class);
        estoque.setId(id);

        estoqueRepository.save(estoque);
    }

    public void salvarEstoque(EstoqueDTO dto, MultipartFile imagem) {

        Estoque estoque = modelMapper.map(dto, Estoque.class);
        Estoque estoqueTratado = salvarImagem(estoque, imagem);
        estoqueRepository.save(estoqueTratado);
    }

    @SuppressWarnings("null")
    private Estoque salvarImagem(Estoque estoque, MultipartFile imagem) {
        try {
            String uploadDir = "src/main/resources/static/img/db/estoque/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path caminhoCompleto = Paths.get(uploadDir, estoque.getId() + "_" + estoque.getCriadoPor() + "_"
                    + estoque.getNome().strip() + imagem.getOriginalFilename().stripLeading());
            byte[] bytes = imagem.getBytes();
            Files.write(caminhoCompleto, bytes);

            estoque.setPathImagem(caminhoCompleto.toString().replace("src/main/resources/static/", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return estoque;

    }

}
