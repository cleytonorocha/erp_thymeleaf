package tech.leonam.erp.controller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tech.leonam.erp.exceptions.ClienteNaoDeletado;
import tech.leonam.erp.exceptions.ClienteNaoExiste;
import tech.leonam.erp.exceptions.ClienteNaoFoiSalvo;
import tech.leonam.erp.exceptions.CpfCadastrado;
import tech.leonam.erp.model.DTO.ClienteModeloDTO;
import tech.leonam.erp.model.entity.ClienteModelo;
import tech.leonam.erp.service.ClienteServico;

@RestController
@RequestMapping("/api/cliente")
@AllArgsConstructor
public class ClienteControle {

    private final ClienteServico servico;

    @PostMapping
    public ResponseEntity<ClienteModelo> salvarCliente(@RequestBody ClienteModeloDTO dto) {
        try {
            servico.salvarCliente(dto);
        } catch (CpfCadastrado e) {
            ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ClienteNaoFoiSalvo e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteModelo> buscarPorID(@PathVariable int id) throws ClienteNaoExiste {
        try {
            return ResponseEntity.ok().body(servico.buscarPorID(id));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable int id) {
        try {
            servico.deletaPorID(id);
        } catch (ClienteNaoExiste e) {
            return ResponseEntity.badRequest().body("Cliente do id " + id + " não encontrado");
        } catch (ClienteNaoDeletado e) {
            return ResponseEntity.badRequest().body("Falha ao deletar o cliente do id: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarCliente(@PathVariable int id, @RequestBody ClienteModeloDTO dto) {
        return null;
    }

}
