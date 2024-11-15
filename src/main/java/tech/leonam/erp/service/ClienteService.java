package tech.leonam.erp.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import tech.leonam.erp.exceptions.ClienteNaoDeletado;
import tech.leonam.erp.exceptions.ClienteNaoFoiSalvo;
import tech.leonam.erp.exceptions.IdentificadorInvalidoException;
import tech.leonam.erp.model.DTO.ClienteDTO;
import tech.leonam.erp.model.DTO.responseApi.ClienteNomesDTO;
import tech.leonam.erp.model.entity.Cliente;
import tech.leonam.erp.model.enums.TipoPessoa;
import tech.leonam.erp.repository.ClienteRepository;

@Log4j2
@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    public void salvarCliente(ClienteDTO clienteDTO) throws ClienteNaoFoiSalvo, IdentificadorInvalidoException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Iniciando a operação de salvar cliente. Usuário: {}", username);
        
        try {
            Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
            cliente.setCriadoPor(username);
            cliente.setModificadoPor(username);
            clienteRepository.save(cliente);
            log.info("Cliente com o id {} salvo com sucesso.", cliente.getId());
        } catch (Exception e) {
            log.error("Erro ao salvar cliente: {}", e.getMessage(), e);
            throw new ClienteNaoFoiSalvo("Erro ao salvar o cliente.");
        }
    }

    public Cliente procuraAtravesDoId(Long id) throws IdentificadorInvalidoException {
        log.info("Buscando cliente com id {}", id);
        
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente com id {} não encontrado.", id);
                    return new IdentificadorInvalidoException("Cliente com o id " + id + " não foi encontrado");
                });
    }

    public void deletaClientePorId(Long id) throws ClienteNaoDeletado {
        log.info("Iniciando a deleção do cliente com id {}", id);
        
        try {
            clienteRepository.deleteById(id);
            log.info("Cliente com id {} deletado com sucesso.", id);
        } catch (Exception e) {
            log.error("Erro ao deletar cliente com id {}: {}", id, e.getMessage(), e);
            throw new ClienteNaoDeletado("Erro ao deletar o cliente.");
        }
    }

    public void atualizarCliente(ClienteDTO clienteDTO, Long id)
            throws ClienteNaoFoiSalvo, IdentificadorInvalidoException {
        log.info("Iniciando a atualização do cliente com id {}", id);
        
        verificaSeExisteIdOuDaThrow(id);

        Cliente clienteTratado = modelMapper.map(clienteDTO, Cliente.class);

        if (clienteDTO.getIdentificacao().length() == 11) {
            clienteTratado.setTipoPessoa(TipoPessoa.FISICA);
        }

        if (clienteDTO.getIdentificacao().length() == 14) {
            clienteTratado.setTipoPessoa(TipoPessoa.JURIDICA);
        }

        clienteRepository.findById(id).map(m -> {
            clienteTratado.setId(m.getId());
            clienteRepository.save(clienteTratado);
            log.info("Cliente com id {} atualizado com sucesso.", id);
            return Void.TYPE;
        }).orElseThrow(() -> {
            log.error("Cliente com id {} não encontrado para atualização.", id);
            return new IdentificadorInvalidoException("Cliente com o id " + id + " não foi encontrado");
        });
    }

    public Page<Cliente> buscarTodosOsClientes(Integer pagina, Integer linhasPorPagina, String orderBy,
                                               String direcao) {
        log.info("Buscando clientes - Página: {}, Linhas por página: {}, Ordenação por: {}, Direção: {}",
                pagina, linhasPorPagina, orderBy, direcao);
        
        PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Sort.Direction.valueOf(direcao), orderBy);
        Page<Cliente> clientes = clienteRepository.findAll(pageRequest);
        log.info("Número total de clientes encontrados: {}", clientes.getTotalElements());
        return clientes;
    }

    public List<Cliente> buscarTodosOsClientes() {
        log.info("Buscando todos os clientes");
        List<Cliente> clientes = clienteRepository.findAll();
        log.info("Número de clientes encontrados: {}", clientes.size());
        return clientes;
    }

    public void verificaSeExisteIdOuDaThrow(Long id) throws IdentificadorInvalidoException {
        log.info("Verificando se o cliente com id {} existe", id);
        
        if (!clienteRepository.existsById(id)) {
            log.error("Cliente com id {} não encontrado.", id);
            throw new IdentificadorInvalidoException("Cliente com o id " + id + " não foi encontrado");
        }
        log.info("Cliente com id {} encontrado.", id);
    }

    public List<ClienteNomesDTO> buscarTodosNomesDosClientes() {
        log.info("Buscando todos os nomes dos clientes");
        List<ClienteNomesDTO> nomes = clienteRepository.findAll()
                .stream()
                .map(m -> ClienteNomesDTO.nome(m))
                .toList();
        log.info("Número de nomes de clientes encontrados: {}", nomes.size());
        return nomes;
    }
}
