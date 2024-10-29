package tech.leonam.erp.config;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.javafaker.Faker;

import lombok.AllArgsConstructor;
import tech.leonam.erp.model.entity.Categoria;
import tech.leonam.erp.model.entity.Cliente;
import tech.leonam.erp.model.entity.Estoque;
import tech.leonam.erp.model.entity.Servico;
import tech.leonam.erp.model.entity.TipoPagamento;
import tech.leonam.erp.model.enums.StatusServico;
import tech.leonam.erp.model.enums.UF;
import tech.leonam.erp.repository.CategoriaRepository;
import tech.leonam.erp.repository.ClienteRepository;
import tech.leonam.erp.repository.EstoqueRepository;
import tech.leonam.erp.repository.ServicoRepository;
import tech.leonam.erp.repository.TipoPagamentoRepository;
import tech.leonam.erp.util.DocumentoGerador;

@Configuration
@Profile("dev")
@AllArgsConstructor
public class DbMockConfig {

    private final ClienteRepository clienteRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;
    private final ServicoRepository servicoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstoqueRepository estoqueRepository;
    private final Faker faker;
    private static final List<String> BAIRROS = Arrays.asList(
            "Jardim Paulista", "Vila Madalena", "Centro", "Moema", "Pinheiros",
            "Brooklin", "Santana", "Aclimação", "Vila Mariana", "Itaim Bibi");
    private final Integer quantidade = 500;

    @Bean
    public String saveMock() {
        List<Cliente> fakeClientes = geradorDeClienteFake();
        List<Servico> fakeServico = geradorDeServicoFake();
        List<TipoPagamento> fakeTipoPagamento = geradorDeTipoPagamentoFake();
        List<Estoque> fakeEstoques = geradorDeEstoquesFake();
        List<Categoria> fakeCategoria = geradorDeCategoriaFake();

        clienteRepository.saveAll(fakeClientes);
        tipoPagamentoRepository.saveAll(fakeTipoPagamento);
        servicoRepository.saveAll(fakeServico);
        categoriaRepository.saveAll(fakeCategoria);
        estoqueRepository.saveAll(fakeEstoques);

        return null;
    }

    private List<Cliente> geradorDeClienteFake() {
        List<Cliente> clientes = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (var i = 0; i < quantidade; i++) {
            Cliente cliente = new Cliente();

            cliente.setNome(faker.name().fullName());
            cliente.setNumeroContato(faker.phoneNumber().phoneNumber());
            cliente.setCep(faker.address().zipCode());

            if (random.nextBoolean())
                cliente.setIdentificacao(DocumentoGerador.gerarCPF());
            else
                cliente.setIdentificacao(DocumentoGerador.gerarCNPJ());

            cliente.setEndereco(faker.address().streetName());
            cliente.setBairro(BAIRROS.get(random.nextInt(BAIRROS.size())));
            cliente.setCidade(faker.address().city());
            cliente.setUf(UF.estadoAleatorio());
            cliente.setNumeroCasa(random.nextInt(0, 2000));

            cliente.setCriadoPor(faker.name().nameWithMiddle());
            cliente.setModificadoPor(faker.name().nameWithMiddle());
            cliente.setDataModificacao(LocalDateTime.now().minusDays(random.nextInt(0, 365)));

            clientes.add(cliente);
        }
        return clientes;
    }

    private List<Servico> geradorDeServicoFake() {
        List<Servico> servicos = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (var i = 0; i < quantidade; i++) {
            Servico servico = new Servico();

            servico.setNome(faker.commerce().productName());
            servico.setPreco(BigDecimal.valueOf(faker.number().randomDouble(2, 50, 500)));
            servico.setCliente(
                    Cliente.builder()
                            .id(longSobreQuatidade())
                            .build());
            servico.setDescricao(faker.lorem().sentence());
            servico.setTipoPagamento(
                    TipoPagamento.builder()
                            .id(longSobreQuatidade())
                            .build());
            servico.setStatus(StatusServico.values()[random.nextInt(StatusServico.values().length)]);
            servico.setPagamentoPrevisto(LocalDate.now().plusDays(random.nextInt(1, 30)));
            servico.setPagamentoFinal(LocalDate.now().plusDays(random.nextInt(31, 60)));
            servico.setCriadoPor(faker.name().fullName());
            servico.setModificadoPor(faker.name().fullName());

            servicos.add(servico);
        }
        return servicos;
    }

    private List<TipoPagamento> geradorDeTipoPagamentoFake() {
        List<TipoPagamento> tiposPagamento = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (var i = 0; i < quantidade; i++) {
            TipoPagamento tipoPagamento = new TipoPagamento();

            tipoPagamento.setNome(faker.job().seniority());
            tipoPagamento.setDescricao(faker.lorem().sentence());
            tipoPagamento.setAtivo(faker.bool().bool());
            tipoPagamento.setCriadoPor(faker.name().fullName());
            tipoPagamento.setModificadoPor(faker.name().fullName());
            tipoPagamento.setDataCriacao(LocalDateTime.now().minusDays(random.nextInt(0, 365)));
            tipoPagamento.setDataModificacao(LocalDateTime.now().minusDays(random.nextInt(0, 365)));

            tiposPagamento.add(tipoPagamento);
        }
        return tiposPagamento;
    }

    private List<Estoque> geradorDeEstoquesFake() {
        List<Estoque> estoques = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (var i = 0; i < quantidade; i++) {
            Estoque estoque = new Estoque();

            estoque.setNome(faker.commerce().productName());
            estoque.setPrecoUnitario(
                    BigDecimal.valueOf(
                            faker.number()
                                    .randomDouble(2, 1, 100)));
            estoque.setQuantidade(faker.number().randomNumber());

            estoque.setValidade(
                    LocalDate.now()
                            .plusYears(
                                    faker.number()
                                            .numberBetween(1, 100)));

            estoque.setDataDaCompra(
                    LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(
                                    faker.date()
                                            .future(2080, TimeUnit.DAYS)
                                            .getTime()),
                            ZoneOffset.UTC));

            estoque.setCategoria(
                    Categoria.builder()
                            .id(longSobreQuatidade())
                            .build());

            estoque.setPathImagem("img/db/estoque/sample.jpg");
            estoque.setCriadoPor(faker.name().fullName());
            estoque.setModificadoPor(faker.name().fullName());
            estoque.setDataCriacao(LocalDateTime.now().minusDays(random.nextInt(0, 365)));
            estoque.setDataModificacao(LocalDateTime.now().minusDays(random.nextInt(0, 365)));

            estoques.add(estoque);
        }

        return estoques;
    }

    private List<Categoria> geradorDeCategoriaFake() {
        List<Categoria> tipoCategorias = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (var i = 0; i < quantidade; i++) {
            Categoria categoria = new Categoria();

            categoria.setNome(faker.commerce().material());
            categoria.setDescricao(faker.lorem().characters(0, 200));
            categoria.setAtivo(Boolean.valueOf(faker.bool().bool()));

            categoria.setCriadoPor(faker.name().fullName());
            categoria.setModificadoPor(faker.name().fullName());
            categoria.setDataCriacao(LocalDateTime.now().minusDays(random.nextInt(0, 365)));
            categoria.setDataModificacao(LocalDateTime.now().minusDays(random.nextInt(0, 365)));

            tipoCategorias.add(categoria);
        }

        return tipoCategorias;
    }

    private Long longSobreQuatidade() {
        return faker.number().numberBetween(1l, quantidade);
    }

}
