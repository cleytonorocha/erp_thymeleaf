package tech.leonam.erp.repository;

import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import tech.leonam.erp.exceptions.ClienteNaoExiste;
import tech.leonam.erp.model.DTO.ClienteModeloDTO;
import tech.leonam.erp.model.entity.ClienteModelo;
import tech.leonam.erp.query.ClientQuery;

@Repository
@AllArgsConstructor
public class ClienteRepositorio {

    private final JdbcClient jdbcClient;

    public Optional<ClienteModelo> buscarPorID(int id) throws ClienteNaoExiste {
        return jdbcClient.sql(ClientQuery.SELECT_BY_ID)
                .param("id", id)
                .query(ClienteModelo.class)
                .optional();
    }

    @Transactional
    public int salvaCliente(ClienteModeloDTO dto) {
        return jdbcClient.sql(ClientQuery.INSERT_INTO)
                .param("isCPF", dto.isCPF())
                .param("nome", dto.getNome())
                .param("cpfOrCnpj", dto.getCpfOrCnpj())
                .param("numeroContato", dto.getNumeroContato())
                .param("cep", dto.getCep())
                .param("endereco", dto.getEndereco())
                .param("bairro", dto.getBairro())
                .param("cidade", dto.getCidade())
                .param("uf", dto.getUf())
                .param("numeroCasa", dto.getNumeroCasa())
                .update();
    }

    @Transactional
    public Integer deletaPorID(int id) {
        return jdbcClient.sql(ClientQuery.DELETE_BY_ID)
                .param("id", id)
                .update();
    }

    public Integer cpfExiste(String cpf) {
        return jdbcClient.sql(ClientQuery.EXISTS_ID)
                .param("cpf_or_cnpj", cpf)
                .query(Integer.class)
                .optional()
                .orElseGet(() -> 0);
    }

}
