package tech.leonam.erp.model.DTO.responseApi;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import tech.leonam.erp.model.entity.Categoria;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "nome")
public class CategoriaNomesDTO {
    
    private Long id;
    private String nome;
    
    public static ClienteNomesDTO nome(Categoria categoria){
        return new ClienteNomesDTO(categoria.getId(), categoria.getNome());
    }
}