package tech.leonam.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tech.leonam.erp.model.DTO.responseApi.CategoriaNomesDTO;
import tech.leonam.erp.model.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    @Query("SELECT DISTINCT new tech.leonam.erp.model.DTO.responseApi.CategoriaNomesDTO(c.id, c.nome) FROM Categoria c")
    public List<CategoriaNomesDTO> findAllNomesCategoria();
}
