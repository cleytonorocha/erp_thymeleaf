package tech.leonam.erp.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import tech.leonam.erp.exceptions.ClienteNaoDeletado;
import tech.leonam.erp.exceptions.ClienteNaoExiste;
import tech.leonam.erp.exceptions.ClienteNaoFoiSalvo;
import tech.leonam.erp.exceptions.CpfCadastrado;
import tech.leonam.erp.model.DTO.ClienteModeloDTO;
import tech.leonam.erp.model.entity.ClienteModelo;
import tech.leonam.erp.repository.ClienteRepositorio;

@Service
@AllArgsConstructor
public class ClienteServico {
    private final ClienteRepositorio repositorio;

    public Integer salvarCliente(ClienteModeloDTO dto) throws ClienteNaoFoiSalvo, CpfCadastrado {
        if (cpfExiste(dto.getCpfOrCnpj())) {
            throw new CpfCadastrado("CPF ou CNPJ não encontrado, não é possível salvar o cliente.");
        }

        Integer consulta = repositorio.salvaCliente(dto);

        if (consulta != 1) {
            throw new ClienteNaoFoiSalvo("Erro ao salvar o cliente. Verifique os dados e tente novamente.");
        }

        return consulta;
    }

    public ClienteModelo buscarPorID(int id) throws ClienteNaoExiste {
        return repositorio.buscarPorID(id)
                .orElseThrow(() -> new ClienteNaoExiste("Id não encontrado"));
    }

    public Integer deletaPorID(int id) throws ClienteNaoDeletado, ClienteNaoExiste {
        Integer consulta = repositorio.deletaPorID(id);

        if (consulta != 1) {
            throw new ClienteNaoExiste("Cliente do id " + id + " não encontrado");
        }

        return consulta;
    }

    public boolean cpfExiste(String cpf) {
        if (repositorio.cpfExiste(cpf) == 1)
            return true;
        else
            return false;
    }
}
